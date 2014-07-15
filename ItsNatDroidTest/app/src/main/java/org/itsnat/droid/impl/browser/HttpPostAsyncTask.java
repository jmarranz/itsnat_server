package org.itsnat.droid.impl.browser;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.clientdoc.EventSender;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGeneric;
import org.itsnat.droid.impl.util.ValueUtil;

import java.util.List;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpPostAsyncTask extends ProcessingAsyncTask<HttpPostResult>
{
    protected EventSender eventSender;
    protected EventGeneric evt;
    protected String servletPath;
    protected HttpContext httpContext;
    protected HttpParams httpParamsRequest;
    protected HttpParams httpParamsDefault;
    protected boolean sslSelfSignedAllowed;
    protected List<NameValuePair> params;

    public HttpPostAsyncTask(EventSender eventSender,EventGeneric evt,String servletPath, HttpContext httpContext,
            HttpParams httpParamsRequest,HttpParams httpParamsDefault,boolean sslSelfSignedAllowed,List<NameValuePair> params)
    {
        this.eventSender = eventSender;
        this.evt = evt;
        this.servletPath = servletPath;
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest;
        this.httpParamsDefault = httpParamsDefault;
        this.sslSelfSignedAllowed = sslSelfSignedAllowed;
        this.params = params;
    }

    protected HttpPostResult executeInBackground() throws Exception
    {
        StatusLine[] status = new StatusLine[1];
        byte[] resultArr = HttpUtil.httpPost(servletPath, httpContext, httpParamsRequest, httpParamsDefault, sslSelfSignedAllowed, params, status);
        String result = ValueUtil.toString(resultArr);

        return new HttpPostResult(result,status[0]);
    }

    @Override
    protected void onFinishOk(HttpPostResult postResult)
    {
        StatusLine status = postResult.status;
        String result = postResult.result;

        eventSender.processResult(evt,status,result,true);
    }

    @Override
    protected void onFinishError(Exception ex)
    {
        ItsNatDroidException exFinal = eventSender.processException(evt,ex);
        throw exFinal;
    }
}

class HttpPostResult
{
    public HttpPostResult(String result, StatusLine status)
    {
        this.result = result;
        this.status = status;
    }

    public String result;
    public StatusLine status;
}