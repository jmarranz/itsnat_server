package org.itsnat.droid.impl.browser.serveritsnat;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.impl.browser.HttpConfig;
import org.itsnat.droid.impl.browser.HttpFileCache;
import org.itsnat.droid.impl.browser.HttpRequestResultImpl;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.EventGenericImpl;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 10/07/14.
 */
public class EventSender
{
    protected EventManager evtManager;

    public EventSender(EventManager evtManager)
    {
        this.evtManager = evtManager;
    }

    public EventManager getEventManager()
    {
        return evtManager;
    }

    public ItsNatDocImpl getItsNatDocImpl()
    {
        return evtManager.getItsNatDocImpl();
    }

    public void requestSync(EventGenericImpl evt, String servletPath, List<NameValuePair> params, long timeout)
    {
        ItsNatDocImpl itsNatDoc = getItsNatDocImpl();
        PageImpl page = itsNatDoc.getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();
        HttpFileCache httpFileCache = browser.getHttpFileCache();

        // No hace falta clonar porque es síncrona la llamada
        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = page.getHttpParams();
        httpParamsRequest = httpParamsRequest != null ? httpParamsRequest : new BasicHttpParams();
        HttpConfig.setTimeout(timeout,httpParamsRequest);
        HttpParams httpParamsDefault = browser.getHttpParams();
        Map<String,String> httpHeaders = page.getPageRequestImpl().createHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        HttpRequestResultImpl result = null;
        try
        {
            result = HttpUtil.httpPost(servletPath,httpFileCache, httpContext, httpParamsRequest, httpParamsDefault,httpHeaders,sslSelfSignedAllowed, params,null);
        }
        catch (Exception ex)
        {
            ItsNatDroidException exFinal = convertException(evt, ex);
            throw exFinal;

            // No usamos aquí el OnEventErrorListener porque la excepción es capturada por un catch anterior que sí lo hace
        }

        processResult(evt,result,false);
    }

    public void requestAsync(EventGenericImpl evt, String servletPath, List<NameValuePair> params, long timeout)
    {
        HttpPostEventAsyncTask postTask = new HttpPostEventAsyncTask(this, evt, servletPath, params,timeout);
        postTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // Con execute() a secas se ejecuta en un "pool" de un sólo hilo sin verdadero paralelismo
    }

    public void processResult(EventGenericImpl evt,HttpRequestResultImpl result,boolean async)
    {
        ItsNatDocImpl itsNatDoc = getItsNatDocImpl();
        itsNatDoc.fireEventMonitors(false,false,evt);

        String responseText = result.getResponseText();
        if (result.isStatusOK())
        {
            itsNatDoc.eval(responseText);

            if (async) evtManager.returnedEvent(evt);
        }
        else // Error del servidor, lo normal es que haya lanzado una excepción
        {
            itsNatDoc.showErrorMessage(true, responseText);
            throw new ItsNatDroidServerResponseException(result);
        }
    }

    public ItsNatDroidException convertException(EventGenericImpl evt, Exception ex)
    {
        ItsNatDocImpl itsNatDoc = getItsNatDocImpl();

        if (ex instanceof SocketTimeoutException) // Esperamos este error en el caso de timeout
        {
            itsNatDoc.fireEventMonitors(false, true, evt);

            return new ItsNatDroidException(ex);
        }
        else
        {
            itsNatDoc.fireEventMonitors(false, false, evt);

            if (ex instanceof ItsNatDroidException) return (ItsNatDroidException)ex;
            else return new ItsNatDroidException(ex);
        }
    }


}
