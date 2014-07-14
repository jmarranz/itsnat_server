package org.itsnat.droid.impl.browser.clientdoc;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGeneric;
import org.itsnat.droid.impl.util.ValueUtil;

import java.util.List;

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

    public String requestSyncText(EventGeneric evt,String servletPath,List<NameValuePair> params,long timeout)
    {
        ItsNatDocImpl itsNatDoc = evtManager.getItsNatDocImpl();
        PageImpl page = itsNatDoc.getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();


        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = page.getHttpParams();
        httpParamsRequest = httpParamsRequest.copy();
        int soTimeout = timeout < 0 ? Integer.MAX_VALUE : (int)timeout;
        HttpConnectionParams.setSoTimeout(httpParamsRequest,soTimeout);
        HttpParams httpParamsDefault = browser.getHttpParams();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        StatusLine[] status = new StatusLine[1];
        byte[] result = HttpUtil.httpPost(servletPath, httpContext, httpParamsRequest, httpParamsDefault,sslSelfSignedAllowed,params,status);
        itsNatDoc.fireEventMonitors(false, false, evt);
        String resultStr = ValueUtil.toString(result);
        int statusCode = status[0].getStatusCode();

        if (statusCode == 200)
        {
            Interpreter interp = page.getInterpreter();
            try
            {
                interp.eval(resultStr);
            }
            catch (EvalError ex)
            {
                showErrorMessage(false, resultStr);
                throw new ItsNatDroidScriptException(ex, resultStr);
            }
            catch (Exception ex)
            {
                showErrorMessage(false, resultStr);
                throw new ItsNatDroidScriptException(ex, resultStr);
            }
        }
        else
        {
            showErrorMessage(true,resultStr);
            throw new ItsNatDroidServerResponseException(statusCode, status[0].getReasonPhrase(), resultStr);
        }


        return resultStr;
    }

    private void showErrorMessage(boolean serverErr,String msg)
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
