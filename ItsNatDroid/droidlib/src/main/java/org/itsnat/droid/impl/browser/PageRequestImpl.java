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
import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.OnPageLoadListener;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.AttrParsedRemote;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.model.layout.ScriptParsed;
import org.itsnat.droid.impl.model.layout.ScriptRemoteParsed;
import org.itsnat.droid.impl.parser.layout.LayoutParserPage;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
    protected AttrLayoutInflaterListener inflateLayoutListener;
    protected AttrDrawableInflaterListener inflateDrawableListener;
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

    public AttrLayoutInflaterListener getAttrLayoutInflaterListener()
    {
        return inflateLayoutListener;
    }

    @Override
    public PageRequest setAttrLayoutInflaterListener(AttrLayoutInflaterListener inflateLayoutListener)
    {
        this.inflateLayoutListener = inflateLayoutListener;
        return this;
    }

    public AttrDrawableInflaterListener getAttrDrawableInflaterListener()
    {
        return inflateDrawableListener;
    }

    @Override
    public PageRequest setAttrDrawableInflaterListener(AttrDrawableInflaterListener inflateDrawableListener)
    {
        this.inflateDrawableListener = inflateDrawableListener;
        return this;
    }

    @Override
    public PageRequest setHttpParams(HttpParams httpParams)
    {
        this.httpParams = httpParams;
        return this;
    }

    public boolean isSynchronous()
    {
        return sync;
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
        // No hace falta clonar porque es síncrono el método

        HttpConfig httpConfig = new HttpConfig(this);
        httpConfig.httpContext = browser.getHttpContext();
        httpConfig.httpParamsRequest = this.httpParams;
        httpConfig.httpParamsDefault = browser.getHttpParams();
        httpConfig.httpHeaders = createHttpHeaders();
        httpConfig.sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        XMLInflateRegistry xmlInflateRegistry = browser.getItsNatDroidImpl().getXMLInflateRegistry();

        String pageURLBase = getURLBase();

        PageRequestResult result = null;
        try
        {
            HttpRequestResultImpl httpReqResult = HttpUtil.httpGet(url,httpConfig.httpContext,httpConfig.httpParamsRequest,
                    httpConfig.httpParamsDefault,httpConfig.httpHeaders,httpConfig.sslSelfSignedAllowed,null,null);

            result = processHttpRequestResult(httpReqResult,pageURLBase,httpConfig,xmlInflateRegistry);
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

    public static PageRequestResult processHttpRequestResult(HttpRequestResultImpl result,
                                                             String pageURLBase,HttpConfig httpConfig,XMLInflateRegistry xmlInflateRegistry) throws Exception
    {
        if (!result.isStatusOK())
        {
            // Normalmente será el texto del error que envía el servidor, por ejemplo el stacktrace
            result.setResponseText(ValueUtil.toString(result.getResponseByteArray(), result.getEncoding()));
            throw new ItsNatDroidServerResponseException(result);
        }

        String markup = result.getResponseText();
        String itsNatServerVersion = result.getItsNatServerVersion();
        LayoutParsed layoutParsed = xmlInflateRegistry.getLayoutParsedCache(markup, itsNatServerVersion, true, true);


        PageRequestResult pageReqResult = new PageRequestResult(result, layoutParsed);

        if (!LayoutParserPage.PRELOAD_SCRIPTS || result.getItsNatServerVersion() == null)
        {
            // Página NO servida por ItsNat o bien se especifica que no se precargan, tenemos que descargar los <script src="..."> remótamente
            ArrayList<ScriptParsed> scriptList = layoutParsed.getScriptList();
            if (scriptList != null)
            {
                for (int i = 0; i < scriptList.size(); i++)
                {
                    ScriptParsed script = scriptList.get(i);
                    if (script instanceof ScriptRemoteParsed)
                    {
                        ScriptRemoteParsed scriptRemote = (ScriptRemoteParsed) script;
                        String code = downloadScript(scriptRemote.getSrc(),pageURLBase,httpConfig);
                        scriptRemote.setCode(code);
                    }
                }
            }
        }


        LinkedList<AttrParsedRemote> attrRemoteList = layoutParsed.getAttributeRemoteList();
        if (attrRemoteList != null)
        {
            HttpResourceDownloader resDownloader = new HttpResourceDownloader(pageURLBase, httpConfig.httpContext, httpConfig.httpParamsRequest, httpConfig.httpParamsDefault, httpConfig.httpHeaders, httpConfig.sslSelfSignedAllowed, xmlInflateRegistry);
            resDownloader.downloadResources(attrRemoteList);
        }

        return pageReqResult;
    }


    public void processResponse(PageRequestResult result)
    {
        HttpRequestResultImpl httpReqResult = result.getHttpRequestResult();

        if (!HttpUtil.MIME_ANDROID_LAYOUT.equals(httpReqResult.getMimeType()))
            throw new ItsNatDroidServerResponseException("Expected " + HttpUtil.MIME_ANDROID_LAYOUT + " MIME in Content-Type:" + httpReqResult.getMimeType(),httpReqResult);

        AttrLayoutInflaterListener inflateLayoutListener = getAttrLayoutInflaterListener();

        PageImpl page = new PageImpl(this,httpParams,result,inflateLayoutListener);
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
               .setAttrLayoutInflaterListener(inflateLayoutListener)
               .setAttrDrawableInflaterListener(inflateDrawableListener)
               .setSynchronous(sync)
               .setURL(url);
        return request;
    }

    private static String downloadScript(String src,String pageURLBase,HttpConfig httpConfig) throws SocketTimeoutException
    {
        src = HttpUtil.composeAbsoluteURL(src,pageURLBase);
        HttpRequestResultImpl result = HttpUtil.httpGet(src,httpConfig.httpContext,httpConfig.httpParamsRequest,httpConfig.httpParamsDefault,httpConfig.httpHeaders,httpConfig.sslSelfSignedAllowed, null,HttpUtil.MIME_BEANSHELL);
        return result.getResponseText();
    }
}
