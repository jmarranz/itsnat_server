package org.itsnat.droid.impl.browser.clientgeneric;

import org.apache.http.NameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnHttpRequestErrorListener;
import org.itsnat.droid.OnHttpRequestListener;
import org.itsnat.droid.impl.browser.HttpRequestResultImpl;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.ProcessingAsyncTask;

import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpPostGenericAsyncTask extends ProcessingAsyncTask<HttpRequestResultImpl>
{
    protected GenericHttpClientImpl parent;
    protected String servletPath;
    protected HttpContext httpContext;
    protected HttpParams httpParamsRequest;
    protected HttpParams httpParamsDefault;
    protected Map<String,String> httpHeaders;
    protected boolean sslSelfSignedAllowed;
    protected List<NameValuePair> params;
    protected OnHttpRequestListener listener;

    public HttpPostGenericAsyncTask(GenericHttpClientImpl parent,String servletPath,
                HttpParams httpParamsRequest, List<NameValuePair> params,OnHttpRequestListener listener)
    {
        PageImpl page = parent.getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();

        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsDefault = browser.getHttpParams();
        Map<String,String> httpHeaders = page.getPageRequestImpl().createHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        this.parent = parent;
        this.servletPath = servletPath;
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest != null ? httpParamsRequest.copy() : null;
        this.httpParamsDefault = httpParamsDefault != null ? httpParamsDefault.copy() : null;
        this.httpHeaders = httpHeaders;
        this.sslSelfSignedAllowed = sslSelfSignedAllowed;
        this.params = params;
        this.listener = listener;
    }

    protected HttpRequestResultImpl executeInBackground() throws Exception
    {
        return HttpUtil.httpPost(servletPath, httpContext, httpParamsRequest, httpParamsDefault, httpHeaders, sslSelfSignedAllowed, params);
    }

    @Override
    protected void onFinishOk(HttpRequestResultImpl postResult)
    {
        try
        {
            parent.processResult(postResult,listener);
        }
        catch(Exception ex)
        {
            OnHttpRequestErrorListener errorListener = parent.getOnHttpRequestErrorListener();
            if (errorListener != null)
            {
                errorListener.onError(ex, postResult);
                return;
            }
            else
            {
                if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException)ex;
                else throw new ItsNatDroidException(ex);
            }
        }
    }

    @Override
    protected void onFinishError(Exception ex)
    {
        ItsNatDroidException exFinal = parent.processException(ex);

        OnHttpRequestErrorListener errorListener = parent.getOnHttpRequestErrorListener();
        if (errorListener != null)
        {
            errorListener.onError(exFinal, null);
            return;
        }
        else
        {
            if (exFinal instanceof ItsNatDroidException) throw (ItsNatDroidException)exFinal;
            else throw new ItsNatDroidException(exFinal);
        }
    }
}

