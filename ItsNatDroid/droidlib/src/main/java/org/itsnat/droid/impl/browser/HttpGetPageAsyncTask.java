package org.itsnat.droid.impl.browser;

import org.apache.http.StatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.impl.util.ValueUtil;

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
    protected boolean sslSelfSignedAllowed;

    public HttpGetPageAsyncTask(PageRequestImpl pageRequest,String url, HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault, boolean sslSelfSignedAllowed)
    {
        this.pageRequest = pageRequest;
        this.url = url;
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest;
        this.httpParamsDefault = httpParamsDefault;
        this.sslSelfSignedAllowed = sslSelfSignedAllowed;
    }

    protected String executeInBackground() throws Exception
    {
        StatusLine[] status = new StatusLine[1];
        String[] encoding = new String[1];
        byte[] resultArr = HttpUtil.httpGet(url, httpContext, httpParamsRequest,httpParamsDefault,sslSelfSignedAllowed,status,encoding);
        String result = ValueUtil.toString(resultArr,encoding[0]);
        if (status[0].getStatusCode() != 200)
            throw new ItsNatDroidServerResponseException(status[0].getStatusCode(),status[0].getReasonPhrase(), result);

        return result;
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
