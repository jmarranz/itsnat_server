package org.itsnat.droid.impl.browser.serveritsnat;

import org.apache.http.NameValuePair;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.impl.browser.HttpConfig;
import org.itsnat.droid.impl.browser.HttpFileCache;
import org.itsnat.droid.impl.browser.HttpRequestResultImpl;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.ProcessingAsyncTask;
import org.itsnat.droid.impl.browser.serveritsnat.event.EventGenericImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpPostEventAsyncTask extends ProcessingAsyncTask<HttpRequestResultImpl>
{
    protected EventSender eventSender;
    protected EventGenericImpl evt;
    protected String servletPath;
    protected HttpFileCache httpFileCache;
    protected HttpConfig httpConfig;
    protected List<NameValuePair> params;

    public HttpPostEventAsyncTask(EventSender eventSender, EventGenericImpl evt, String servletPath,
            List<NameValuePair> params,long timeout)
    {
        PageImpl page = eventSender.getItsNatDocImpl().getPageImpl();

        // Hay que tener en cuenta que estos objetos se acceden en multihilo
        this.eventSender = eventSender;
        this.evt = evt;
        this.servletPath = servletPath;
        this.httpFileCache = page.getItsNatDroidBrowserImpl().getHttpFileCache();
        this.httpConfig = new HttpConfig(page);
        httpConfig.setTimeout(timeout);
        this.params = new ArrayList<NameValuePair>(params); // hace una copia, los NameValuePair son de sólo lectura por lo que no hay problema compartirlos en hilos
    }

    protected HttpRequestResultImpl executeInBackground() throws Exception
    {
        return HttpUtil.httpPost(servletPath, httpFileCache,httpConfig.httpContext,httpConfig.httpParamsRequest,httpConfig.httpParamsDefault,httpConfig.httpHeaders,httpConfig.sslSelfSignedAllowed, params,null);
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
        ItsNatDroidException exFinal = eventSender.convertException(evt, ex);

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

