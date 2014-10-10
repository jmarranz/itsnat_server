package org.itsnat.droid.impl.browser.clientdoc;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.impl.browser.HttpResult;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ProcessingAsyncTask;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGenericImpl;
import org.itsnat.droid.impl.util.ValueUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpPostEventAsyncTask extends ProcessingAsyncTask<HttpResult>
{
    protected EventSender eventSender;
    protected EventGenericImpl evt;
    protected String servletPath;
    protected HttpContext httpContext;
    protected HttpParams httpParamsRequest;
    protected HttpParams httpParamsDefault;
    protected Map<String,String> httpHeaders;
    protected boolean sslSelfSignedAllowed;
    protected List<NameValuePair> params;

    public HttpPostEventAsyncTask(EventSender eventSender, EventGenericImpl evt, String servletPath,
            HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault,Map<String,String> httpHeaders, boolean sslSelfSignedAllowed, List<NameValuePair> params)
    {
        this.eventSender = eventSender;
        this.evt = evt;
        this.servletPath = servletPath;
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest;
        this.httpParamsDefault = httpParamsDefault;
        this.httpHeaders = httpHeaders;
        this.sslSelfSignedAllowed = sslSelfSignedAllowed;
        this.params = params;
    }

    protected HttpResult executeInBackground() throws Exception
    {
        HttpResult result = HttpUtil.httpPost(servletPath, httpContext, httpParamsRequest, httpParamsDefault, httpHeaders, sslSelfSignedAllowed, params);
        result.contentStr = ValueUtil.toString(result.contentArr,result.encoding);
        return result;
    }

    @Override
    protected void onFinishOk(HttpResult postResult)
    {
        try
        {
            StatusLine status = postResult.status;
            String result = postResult.contentStr;

            eventSender.processResult(evt, status, result, true);
        }
        catch(Exception ex)
        {
            OnEventErrorListener errorListener = eventSender.getEventManager().getItsNatDocImpl().getPageImpl().getOnEventErrorListener();
            if (errorListener != null)
            {
                errorListener.onError(ex, evt);
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
        ItsNatDroidException exFinal = eventSender.processException(evt,ex);

        OnEventErrorListener errorListener = eventSender.getEventManager().getItsNatDocImpl().getPageImpl().getOnEventErrorListener();
        if (errorListener != null)
        {
            errorListener.onError(exFinal, evt);
            return;
        }
        else
        {
            if (exFinal instanceof ItsNatDroidException) throw (ItsNatDroidException)exFinal;
            else throw new ItsNatDroidException(exFinal);
        }

    }
}

