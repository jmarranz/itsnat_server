package org.itsnat.droid.impl.browser;

import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnPageLoadErrorListener;

import java.util.Map;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpGetPageAsyncTask extends ProcessingAsyncTask<String>
{
    protected PageRequestImpl pageRequest;
    protected String url;
    protected HttpContext httpContext;
    protected HttpParams httpParamsRequest;
    protected HttpParams httpParamsDefault;
    protected Map<String,String> httpHeaders;
    protected boolean sslSelfSignedAllowed;

    public HttpGetPageAsyncTask(PageRequestImpl pageRequest,String url, HttpContext httpContext,
                    HttpParams httpParamsRequest, HttpParams httpParamsDefault,Map<String,String> httpHeaders, boolean sslSelfSignedAllowed)
    {
        this.pageRequest = pageRequest;
        this.url = url;
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest;
        this.httpParamsDefault = httpParamsDefault;
        this.httpHeaders = httpHeaders;
        this.sslSelfSignedAllowed = sslSelfSignedAllowed;
    }

    protected String executeInBackground() throws Exception
    {
        HttpRequestResultImpl result = HttpUtil.httpGet(url, httpContext, httpParamsRequest,httpParamsDefault, httpHeaders,sslSelfSignedAllowed);
        if (result.status.getStatusCode() != 200)
            throw new ItsNatDroidServerResponseException(result.status.getStatusCode(),result.status.getReasonPhrase(),result.responseText);

        return result.responseText;
    }

    @Override
    protected void onFinishOk(String result)
    {
        try
        {
            pageRequest.processResponse(url,result);
        }
        catch(Exception ex)
        {
            OnPageLoadErrorListener errorListener = pageRequest.getOnPageLoadErrorListener();
            if (errorListener != null)
            {
                errorListener.onError(ex, pageRequest); // Para poder recogerla desde fuera
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
        OnPageLoadErrorListener errorListener = pageRequest.getOnPageLoadErrorListener();
        if (errorListener != null)
        {
            errorListener.onError(ex, pageRequest); // Para poder recogerla desde fuera
            return;
        }
        else
        {
            if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException)ex;
            else throw new ItsNatDroidException(ex);
        }
    }
}
