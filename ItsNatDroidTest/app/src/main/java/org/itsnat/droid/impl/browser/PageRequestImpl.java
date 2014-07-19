package org.itsnat.droid.impl.browser;

import android.content.Context;

import org.apache.http.StatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.OnPageLoadListener;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.InflateRequestImpl;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;

import java.io.StringReader;
import java.net.SocketTimeoutException;

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

    @Override
    public void execute(String url)
    {
        // El PageRequestImpl debe poder ser reutilizado si quiere el usuario
        if (sync) executeSync(url);
        else executeAsync(url);
    }

    public void executeSync(String url)
    {
        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = this.httpParams;
        HttpParams httpParamsDefault = browser.getHttpParams();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        String result;
        try
        {
            StatusLine[] status = new StatusLine[1];
            String[] encoding = new String[1];
            byte[] resultArr = HttpUtil.httpGet(url, httpContext, httpParamsRequest, httpParamsDefault, sslSelfSignedAllowed, status, encoding);
            result = ValueUtil.toString(resultArr, encoding[0]);
            if (status[0].getStatusCode() != 200)
                throw new ItsNatDroidServerResponseException(status[0].getStatusCode(), status[0].getReasonPhrase(), result);
        }
        catch(Exception ex)
        {
            // Aunque la excepción pueda ser capturada por el usuario al llamar al método público síncrono, tenemos
            // que respetar su decisión de usar un listener
            OnPageLoadErrorListener errorListener = getOnPageLoadErrorListener();
            if (errorListener != null)
            {
                errorListener.onError(ex, this); // Para poder recogerla desde fuera
                return;
            }
            else
            {
                if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException)ex;
                else throw new ItsNatDroidException(ex);
            }
        }


        processResponse(url,result);
    }

    public void executeAsync(String url)
    {
        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = this.httpParams;
        HttpParams httpParamsDefault = browser.getHttpParams();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        HttpGetPageAsyncTask task = new HttpGetPageAsyncTask(this,url,httpContext, httpParamsRequest, httpParamsDefault,sslSelfSignedAllowed);
        task.execute();
    }

    public void processResponse(String url,String result)
    {
        StringReader input = new StringReader(result);

        ItsNatDroidBrowserImpl browser = getItsNatDroidBrowserImpl();
        InflateRequestImpl inflateRequest = new InflateRequestImpl(browser.getItsNatDroidImpl());
        inflateRequest.setContext(getContext());
        AttrCustomInflaterListener inflateListener = getAttrCustomInflaterListener();
        if (inflateListener != null) inflateRequest.setAttrCustomInflaterListener(inflateListener);

        String[] scriptArr = new String[1];
        InflatedLayoutImpl inflated = inflateRequest.inflateInternal(input, scriptArr);

        HttpParams httpParamsRequest = httpParams != null ? httpParams.copy() : null;

        String loadScript = scriptArr[0];
        PageImpl page = new PageImpl(browser, url, httpParams, inflated, result, loadScript);
        OnPageLoadListener pageListener = getOnPageLoadListener();
        if (pageListener != null) pageListener.onPageLoad(page);
    }
}
