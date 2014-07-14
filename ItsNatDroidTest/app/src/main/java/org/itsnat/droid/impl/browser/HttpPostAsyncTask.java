package org.itsnat.droid.impl.browser;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.util.ValueUtil;

import java.util.List;

/**
 * Created by jmarranz on 4/06/14.
 */
public abstract class HttpPostAsyncTask extends ProcessingAsyncTask<String>
{
    protected String servletPath;
    protected HttpContext httpContext;
    protected HttpParams httpParamsRequest;
    protected HttpParams httpParamsDefault;
    protected boolean sslSelfSignedAllowed;
    protected List<NameValuePair> params;

    public HttpPostAsyncTask(String servletPath, String url, HttpContext httpContext, HttpParams httpParamsRequest,
                             HttpParams httpParamsDefault,boolean sslSelfSignedAllowed,List<NameValuePair> params)
    {
        this.servletPath = servletPath;
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest;
        this.httpParamsDefault = httpParamsDefault;
        this.sslSelfSignedAllowed = sslSelfSignedAllowed;
        this.params = params;
    }

    protected String executeInBackground()
    {
        StatusLine[] status = new StatusLine[1];
        byte[] result = HttpUtil.httpPost(servletPath, httpContext, httpParamsRequest,httpParamsDefault,sslSelfSignedAllowed,params,status);
        if (status[0].getStatusCode() != 200)
            throw new ItsNatDroidServerResponseException(status[0].getStatusCode(),status[0].getReasonPhrase(),ValueUtil.toString(result));

        return ValueUtil.toString(result);
    }

}
