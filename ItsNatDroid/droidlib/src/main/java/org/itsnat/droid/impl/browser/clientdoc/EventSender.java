package org.itsnat.droid.impl.browser.clientdoc;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.impl.browser.HttpPostEventAsyncTask;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGenericImpl;
import org.itsnat.droid.impl.util.ValueUtil;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;

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

    private HttpParams buildHttpParamsRequest(long timeout)
    {
        ItsNatDocImpl itsNatDoc = evtManager.getItsNatDocImpl();
        PageImpl page = itsNatDoc.getPageImpl();
        HttpParams httpParamsRequest = page.getHttpParams();
        httpParamsRequest = httpParamsRequest.copy();
        int soTimeout = timeout < 0 ? Integer.MAX_VALUE : (int) timeout;
        HttpConnectionParams.setSoTimeout(httpParamsRequest, soTimeout);
        return httpParamsRequest;
    }

    public void requestSync(EventGenericImpl evt, String servletPath, List<NameValuePair> params, long timeout)
    {
        ItsNatDocImpl itsNatDoc = evtManager.getItsNatDocImpl();
        PageImpl page = itsNatDoc.getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();

        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = buildHttpParamsRequest(timeout);
        HttpParams httpParamsDefault = browser.getHttpParams();
        Map<String,String> httpHeaders = page.getPageRequestImpl().getHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        StatusLine[] status = new StatusLine[1];
        String[] encoding = new String[1];
        String result = null;
        try
        {
            byte[] resultArr = HttpUtil.httpPost(servletPath, httpContext, httpParamsRequest, httpParamsDefault,httpHeaders,sslSelfSignedAllowed, params, status,encoding);
            result = ValueUtil.toString(resultArr,encoding[0]);
        }
        catch (Exception ex)
        {
            ItsNatDroidException exFinal = processException(evt, ex);
            throw exFinal;

            // No usamos aquí el OnEventErrorListener porque la excepción es capturada por un catch anterior que sí lo hace
        }

        processResult(evt,status[0],result,false);
    }

    public void requestAsync(EventGenericImpl evt, String servletPath, List<NameValuePair> params, long timeout)
    {
        ItsNatDocImpl itsNatDoc = evtManager.getItsNatDocImpl();
        PageImpl page = itsNatDoc.getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();

        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = buildHttpParamsRequest(timeout);
        HttpParams httpParamsDefault = browser.getHttpParams();
        Map<String,String> httpHeaders = page.getPageRequestImpl().getHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        HttpPostEventAsyncTask postTask = new HttpPostEventAsyncTask(this, evt, servletPath, httpContext, httpParamsRequest, httpParamsDefault,httpHeaders, sslSelfSignedAllowed, params);
        postTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // Con execute() a secas se ejecuta en un "pool" de un sólo hilo sin verdadero paralelismo
    }

    public void processResult(EventGenericImpl evt,StatusLine status,String result,boolean async)
    {
        ItsNatDocImpl itsNatDoc = evtManager.getItsNatDocImpl();
        PageImpl page = itsNatDoc.getPageImpl();
        page.fireEventMonitors(false,false,evt);

        int statusCode = status.getStatusCode();
        if (statusCode == 200)
        {
            Interpreter interp = page.getInterpreter();
            try
            {
//long start = System.currentTimeMillis();

                interp.eval(result);

//long end = System.currentTimeMillis();
//System.out.println("LAPSE" + (end - start));
            }
            catch (EvalError ex)
            {
                showErrorMessage(false, ex.getMessage());
                throw new ItsNatDroidScriptException(ex, result);
            }
            catch (Exception ex)
            {
                showErrorMessage(false, ex.getMessage());
                throw new ItsNatDroidScriptException(ex, result);
            }

            if (async) evtManager.returnedEvent(evt);

        }
        else // Error del servidor, lo normal es que haya lanzado una excepción
        {
            showErrorMessage(true, result);
            throw new ItsNatDroidServerResponseException(statusCode, status.getReasonPhrase(), result);
        }
    }

    public ItsNatDroidException processException(EventGenericImpl evt,Exception ex)
    {
        ItsNatDocImpl itsNatDoc = evtManager.getItsNatDocImpl();
        PageImpl page = itsNatDoc.getPageImpl();

        if (ex instanceof SocketTimeoutException) // Esperamos este error en el caso de timeout
        {
            page.fireEventMonitors(false, true, evt);

            return new ItsNatDroidException(ex);
        }
        else
        {
            page.fireEventMonitors(false, false, evt);

            if (ex instanceof ItsNatDroidException)
                return (ItsNatDroidException)ex;
            else
                return new ItsNatDroidException(ex);
        }
    }

    public void showErrorMessage(boolean serverErr, String msg)
    {
        ItsNatDocImpl itsNatDoc = evtManager.getItsNatDocImpl();
        int errorMode = itsNatDoc.getErrorMode();
        if (errorMode == ClientErrorMode.NOT_SHOW_ERRORS) return;

        if (serverErr) // Pagina HTML con la excepcion del servidor
        {
            if ((errorMode == ClientErrorMode.SHOW_SERVER_ERRORS) ||
                (errorMode == ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS)) // 2 = ClientErrorMode.SHOW_SERVER_ERRORS, 4 = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS
                itsNatDoc.alert("SERVER ERROR: " + msg);
        }
        else
        {
            if ((errorMode == ClientErrorMode.SHOW_CLIENT_ERRORS) ||
                (errorMode == ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS)) // 3 = ClientErrorMode.SHOW_CLIENT_ERRORS, 4 = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS
            {
                // Ha sido un error Beanshell
                itsNatDoc.alert(msg);
            }
        }
    }
}
