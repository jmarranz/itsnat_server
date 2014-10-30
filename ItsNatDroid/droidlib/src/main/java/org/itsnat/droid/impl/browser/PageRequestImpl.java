package org.itsnat.droid.impl.browser;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;

import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.OnPageLoadListener;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.parser.TreeViewParsedCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 5/06/14.
 */
public class PageRequestImpl implements PageRequest
{
    protected ItsNatDroidBrowserImpl browser;
    protected Context ctx;
    protected HttpParams httpParams;
    protected OnPageLoadListener pageListener;
    protected OnPageLoadErrorListener errorListener;
    protected AttrCustomInflaterListener inflateListener;
    protected boolean sync = false;
    protected String url;
    protected String urlBase;

    public PageRequestImpl(ItsNatDroidBrowserImpl browser)
    {
        this.browser = browser;
    }

    public ItsNatDroidBrowserImpl getItsNatDroidBrowserImpl()
    {
        return browser;
    }

    public Context getContext()
    {
        return ctx;
    }

    @Override
    public PageRequest setContext(Context ctx)
    {
        this.ctx = ctx;
        return this;
    }

    public OnPageLoadListener getOnPageLoadListener()
    {
        return pageListener;
    }

    @Override
    public PageRequest setOnPageLoadListener(OnPageLoadListener pageListener)
    {
        this.pageListener = pageListener;
        return this;
    }

    public OnPageLoadErrorListener getOnPageLoadErrorListener()
    {
        return errorListener;
    }

    @Override
    public PageRequest setOnPageLoadErrorListener(OnPageLoadErrorListener errorListener)
    {
        this.errorListener = errorListener;
        return this;
    }

    public AttrCustomInflaterListener getAttrCustomInflaterListener()
    {
        return inflateListener;
    }

    @Override
    public PageRequest setAttrCustomInflaterListener(AttrCustomInflaterListener inflateListener)
    {
        this.inflateListener = inflateListener;
        return this;
    }

    @Override
    public PageRequest setHttpParams(HttpParams httpParams)
    {
        this.httpParams = httpParams;
        return this;
    }

    @Override
    public PageRequest setSynchronous(boolean sync)
    {
        this.sync = sync;
        return this;
    }

    public String getURL()
    {
        return url;
    }

    @Override
    public PageRequest setURL(String url)
    {
        this.url = url;
        this.urlBase = HttpUtil.getBasePathOfURL(url);
        return this;
    }

    public String getURLBase()
    {
        return urlBase;
    }

    public Map<String,String> createHttpHeaders()
    {
        // http://stackoverflow.com/questions/17481341/how-to-get-android-screen-size-programmatically-once-and-for-all
        // Recuerda que cambia con la orientación por eso hay que enviarlos "frescos"
        Resources resources = ctx.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        ItsNatDroidImpl itsNatDroid = browser.getItsNatDroidImpl();
        int libVersionCode = itsNatDroid.getVersionCode();
        String libVersionName = itsNatDroid.getVersionName();
        PackageInfo pInfo = null;
        try { pInfo = ctx.getPackageManager().getPackageInfo( ctx.getPackageName(), 0); }
        catch(PackageManager.NameNotFoundException ex) { throw new ItsNatDroidException(ex); }

        Map<String, String> httpHeaders = new HashMap<String, String>();

        httpHeaders.put("ItsNat-model", "" + Build.MODEL);
        httpHeaders.put("ItsNat-sdk-int", "" + Build.VERSION.SDK_INT);
        httpHeaders.put("ItsNat-lib-version-name", "" + libVersionName);
        httpHeaders.put("ItsNat-lib-version-code", "" + libVersionCode);
        httpHeaders.put("ItsNat-app-version-name", "" + pInfo.versionName);
        httpHeaders.put("ItsNat-app-version-code", "" + pInfo.versionCode);
        httpHeaders.put("ItsNat-display-width", "" + dm.widthPixels);
        httpHeaders.put("ItsNat-display-height", "" + dm.heightPixels);
        httpHeaders.put("ItsNat-display-density", "" + dm.density);

        return httpHeaders;
    }

    @Override
    public void execute()
    {
        // El PageRequestImpl debe poder ser reutilizado si quiere el usuario
        if (url == null) throw new ItsNatDroidException("Missing URL");
        if (sync) executeSync(url);
        else executeAsync(url);
    }

    private void executeSync(String url)
    {
        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = this.httpParams;
        HttpParams httpParamsDefault = browser.getHttpParams();
        Map<String,String> httpHeaders = createHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        TreeViewParsedCache treeViewParsedCache = browser.getItsNatDroidImpl().getXMLLayoutInflateService().getTreeViewParsedCache();

        PageRequestResult result = null;
        try
        {
            HttpRequestResultImpl httpReqResult = HttpUtil.httpGet(url, httpContext, httpParamsRequest, httpParamsDefault,httpHeaders, sslSelfSignedAllowed,null,null);
            result = new PageRequestResult(httpReqResult,treeViewParsedCache);
        }
        catch(Exception ex)
        {
            // Aunque la excepción pueda ser capturada por el usuario al llamar al método público síncrono, tenemos
            // que respetar su decisión de usar un listener
            OnPageLoadErrorListener errorListener = getOnPageLoadErrorListener();
            if (errorListener != null)
            {
                errorListener.onError(ex, this, result.getHttpRequestResult()); // Para poder recogerla desde fuera
                return;
            }
            else
            {
                if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException)ex;
                else throw new ItsNatDroidException(ex);
            }
        }

        processResponse(result);
    }

    private void executeAsync(String url)
    {
        HttpParams httpParamsRequest = this.httpParams;

        HttpGetPageAsyncTask task = new HttpGetPageAsyncTask(this,url,httpParamsRequest);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // Con execute() a secas se ejecuta en un "pool" de un sólo hilo sin verdadero paralelismo
    }

    public void processResponse(PageRequestResult result)
    {
        HttpRequestResultImpl httpReqResult = result.getHttpRequestResult();

        if (!HttpUtil.MIME_ANDROID_LAYOUT.equals(httpReqResult.getMimeType()))
            throw new ItsNatDroidServerResponseException("Expected " + HttpUtil.MIME_ANDROID_LAYOUT + " MIME in Content-Type:" + httpReqResult.getMimeType(),httpReqResult);

        PageRequestImpl pageRequest = clone(); // De esta manera conocemos como se ha creado pero podemos reutilizar el PageRequestImpl original
        HttpParams httpParamsRequest = httpParams != null ? httpParams.copy() : null;
        AttrCustomInflaterListener inflateListener = getAttrCustomInflaterListener();

        PageImpl page = new PageImpl(pageRequest,httpParamsRequest,result,inflateListener);
        OnPageLoadListener pageListener = getOnPageLoadListener();
        if (pageListener != null) pageListener.onPageLoad(page);
    }

    public PageRequestImpl clone()
    {
        HttpParams httpParams = this.httpParams != null ? this.httpParams.copy() : null;

        PageRequestImpl request = new PageRequestImpl(browser);
        request.setContext(ctx)
               .setHttpParams(httpParams)
               .setOnPageLoadListener(pageListener)
               .setOnPageLoadErrorListener(errorListener)
               .setAttrCustomInflaterListener(inflateListener)
               .setSynchronous(sync)
               .setURL(url);
        return request;
    }
}
