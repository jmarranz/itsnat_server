package org.itsnat.droid.impl.browser;

import org.apache.http.StatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidServerResponseException;

/**
 * Created by jmarranz on 4/06/14.
 */
public abstract class HttpGetAsyncTask extends ProcessingAsyncTask<byte[]>
{
    protected String url;
    protected HttpContext httpContext;
    protected HttpParams httpParamsRequest;
    protected HttpParams httpParamsDefault;

    public HttpGetAsyncTask(String url, HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault)
    {
        this.url = url;
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest;
        this.httpParamsDefault = httpParamsDefault;
    }

    protected byte[] executeInBackground()
    {
        StatusLine[] status = new StatusLine[1];
        byte[] result = HttpUtil.httpGet(url, httpContext, httpParamsRequest,httpParamsDefault,status);
        if (status[0].getStatusCode() != 200)
            throw new ItsNatDroidServerResponseException(status[0].getStatusCode(),status[0].getReasonPhrase(),result);

        return result;
    }

}
