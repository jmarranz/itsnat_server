package org.itsnat.droid.impl.browser.clientgeneric;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ClientErrorMode;
import org.itsnat.droid.GenericHttpClient;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.impl.browser.HttpResult;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.util.ValueUtil;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Created by jmarranz on 9/10/14.
 */
public class GenericHttpClientImpl implements GenericHttpClient
{
    protected PageImpl page;
    protected int errorMode = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS;

    public GenericHttpClientImpl(PageImpl page)
    {
        this.page = page;
    }

    public PageImpl getPageImpl()
    {
        return page;
    }

    @Override
    public int getErrorMode()
    {
        return errorMode;
    }

    @Override
    public void setErrorMode(int errorMode)
    {
        if (errorMode == ClientErrorMode.NOT_CATCH_ERRORS) throw new ItsNatDroidException("ClientErrorMode.NOT_CATCH_ERRORS is not supported"); // No tiene mucho sentido porque el objetivo es dejar fallar y si el usuario no ha registrado "error listeners" ItsNat Droid deja siempre fallar lanzando la excepción
        this.errorMode = errorMode;
    }

    public void requestSync(String servletPath, List<NameValuePair> params, long timeout)
    {
        PageImpl page = getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();

        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = buildHttpParamsRequest(timeout);
        HttpParams httpParamsDefault = browser.getHttpParams();
        Map<String,String> httpHeaders = page.getPageRequestImpl().getHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        HttpResult result = null;
        try
        {
            result = HttpUtil.httpPost(servletPath, httpContext, httpParamsRequest, httpParamsDefault, httpHeaders, sslSelfSignedAllowed, params);
            result.contentStr = ValueUtil.toString(result.contentArr,result.encoding);
        }
        catch (Exception ex)
        {
            ItsNatDroidException exFinal = processException(ex);
            throw exFinal;

            // No usamos aquí el OnEventErrorListener porque la excepción es capturada por un catch anterior que sí lo hace
        }

        processResult(result.status,result.contentStr,false);
    }

    public void requestAsync(String servletPath, List<NameValuePair> params, long timeout)
    {
        PageImpl page = getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();

        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = buildHttpParamsRequest(timeout);
        HttpParams httpParamsDefault = browser.getHttpParams();
        Map<String,String> httpHeaders = page.getPageRequestImpl().getHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        HttpPostGenericAsyncTask postTask = new HttpPostGenericAsyncTask(this, servletPath, httpContext, httpParamsRequest, httpParamsDefault,httpHeaders, sslSelfSignedAllowed, params);
        postTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // Con execute() a secas se ejecuta en un "pool" de un sólo hilo sin verdadero paralelismo

    }

    private HttpParams buildHttpParamsRequest(long timeout)
    {
        PageImpl page = getPageImpl();
        HttpParams httpParamsRequest = page.getHttpParams();
        httpParamsRequest = httpParamsRequest.copy();
        int soTimeout = timeout < 0 ? Integer.MAX_VALUE : (int) timeout;
        HttpConnectionParams.setSoTimeout(httpParamsRequest, soTimeout);
        return httpParamsRequest;
    }

    public ItsNatDroidException processException(Exception ex)
    {
        if (ex instanceof SocketTimeoutException) // Esperamos este error en el caso de timeout
        {
            return new ItsNatDroidException(ex);
        }
        else if (ex instanceof ItsNatDroidException)
        {
            return (ItsNatDroidException)ex;
        }
        else return new ItsNatDroidException(ex);
    }

    public void processResult(StatusLine status,String result,boolean async)
    {
        PageImpl page = getPageImpl();

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
        }
        else // Error del servidor, lo normal es que haya lanzado una excepción
        {
            showErrorMessage(true, result);
            throw new ItsNatDroidServerResponseException(statusCode, status.getReasonPhrase(), result);
        }
    }

    public void showErrorMessage(boolean serverErr, String msg)
    {
        if (errorMode == ClientErrorMode.NOT_SHOW_ERRORS) return;

        ItsNatDocImpl itsNatDoc = page.getItsNatDocImpl();
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
