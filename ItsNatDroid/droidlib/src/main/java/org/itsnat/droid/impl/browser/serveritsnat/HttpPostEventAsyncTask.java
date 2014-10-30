package org.itsnat.droid.impl.browser.serveritsnat;

import org.apache.http.NameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.impl.browser.HttpRequestResultImpl;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.ProcessingAsyncTask;
import org.itsnat.droid.impl.browser.serveritsnat.event.EventGenericImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpPostEventAsyncTask extends ProcessingAsyncTask<HttpRequestResultImpl>
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
            List<NameValuePair> params,long timeout)
    {
        PageImpl page = eventSender.getItsNatDocImpl().getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();
        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = eventSender.buildHttpParamsRequest(timeout);
        HttpParams httpParamsDefault = browser.getHttpParams();
        Map<String,String> httpHeaders = page.getPageRequestImpl().createHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();


        // Hay que tener en cuenta que estos objetos se acceden en multihilo
        this.eventSender = eventSender;
        this.evt = evt;
        this.servletPath = servletPath;
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest; // No hace falta copy, buildHttpParamsRequest crea uno nuevo
        this.httpParamsDefault = httpParamsDefault != null ? httpParamsDefault.copy() : null;
        this.httpHeaders = httpHeaders; // No hace falta clone porque createHttpHeaders() crea un Map
        this.sslSelfSignedAllowed = sslSelfSignedAllowed;
        this.params = new ArrayList<NameValuePair>(params); // hace una copia, los NameValuePair son de sólo lectura por lo que no hay problema compartirlos en hilos
    }

    protected HttpRequestResultImpl executeInBackground() throws Exception
    {
        return HttpUtil.httpPost(servletPath, httpContext, httpParamsRequest, httpParamsDefault, httpHeaders, sslSelfSignedAllowed, params,null);
    }

    @Override
    protected void onFinishOk(HttpRequestResultImpl result)
    {
        try
        {
            eventSender.processResult(evt, result, true);
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

