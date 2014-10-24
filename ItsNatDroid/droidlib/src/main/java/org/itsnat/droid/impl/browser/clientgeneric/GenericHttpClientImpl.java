package org.itsnat.droid.impl.browser.clientgeneric;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ClientErrorMode;
import org.itsnat.droid.GenericHttpClient;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnHttpRequestErrorListener;
import org.itsnat.droid.OnHttpRequestListener;
import org.itsnat.droid.impl.browser.HttpRequestResultImpl;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.util.ValueUtil;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 9/10/14.
 */
public class GenericHttpClientImpl implements GenericHttpClient
{
    protected ItsNatDocImpl itsNatDoc;
    protected HttpParams httpParamsRequest;
    protected OnHttpRequestErrorListener errorListener;
    protected int errorMode = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS;
    protected String pageUrl;
    protected String userUrl;
    protected List<NameValuePair> paramList = new ArrayList<NameValuePair>(10);
    protected OnHttpRequestListener listener;
    protected String overrideMime;
    protected String method = "POST";

    public GenericHttpClientImpl(ItsNatDocImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        PageImpl page = itsNatDoc.getPageImpl();
        HttpParams httpParamsRequest = page.getHttpParams();
        this.httpParamsRequest = httpParamsRequest.copy();
        this.pageUrl = getBasePathOfURL(page.getURL());
    }

    public PageImpl getPageImpl()
    {
        return itsNatDoc.getPageImpl();
    }

    public ItsNatDocImpl getItsNatDocImpl()
    {
        return itsNatDoc;
    }

    public int getErrorMode()
    {
        return errorMode;
    }

    @Override
    public GenericHttpClient setErrorMode(int errorMode)
    {
        if (errorMode == ClientErrorMode.NOT_CATCH_ERRORS) throw new ItsNatDroidException("ClientErrorMode.NOT_CATCH_ERRORS is not supported"); // No tiene mucho sentido porque el objetivo es dejar fallar y si el usuario no ha registrado "error listeners" ItsNat Droid deja siempre fallar lanzando la excepción
        this.errorMode = errorMode;
        return this;
    }

    @Override
    public GenericHttpClient setOnHttpRequestListener(OnHttpRequestListener listener)
    {
        this.listener = listener;
        return this;
    }

    @Override
    public GenericHttpClient setOnHttpRequestErrorListener(OnHttpRequestErrorListener httpErrorListener)
    {
        this.errorListener = httpErrorListener;
        return this;
    }

    @Override
    public GenericHttpClient setMethod(String method)
    {
        this.method = method;
        return this;
    }

    @Override
    public GenericHttpClient setURL(String url)
    {
        this.userUrl = url;
        return this;
    }

    @Override
    public GenericHttpClient addParam(String name,String value)
    {
        // Si se añade el mismo parámetro varias veces, es el caso de multivalor
        paramList.add(new BasicNameValuePair(name,value));
        return this;
    }

    @Override
    public GenericHttpClient clearParams()
    {
        paramList.clear();
        return this;
    }

    @Override
    public GenericHttpClient setRequestHeader(String header, Object value)
    {
        if (value instanceof Boolean)
            httpParamsRequest.setBooleanParameter(header,(Boolean)value);
        else if (value instanceof Integer)
            httpParamsRequest.setIntParameter(header,(Integer)value);
        else if (value instanceof Long)
            httpParamsRequest.setLongParameter(header,(Long)value);
        else if (value instanceof Double)
            httpParamsRequest.setDoubleParameter(header,(Double)value);
        else
            httpParamsRequest.setParameter(header,value);

        return this;
    }

    @Override
    public GenericHttpClient setOverrideMimeType(String mime)
    {
        this.overrideMime = mime;
        return this;
    }

    public String getPageURL()
    {
        return pageUrl;
    }

    private String getFinalURL()
    {
        return ValueUtil.isEmpty(userUrl) ? pageUrl : userUrl; // Como se puede ver seguridad de "single server" ninguna
    }

    @Override
    public HttpRequestResult requestSync()
    {
        PageImpl page = getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();

        String url = getFinalURL();
        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsDefault = browser.getHttpParams();
        HttpParams httpParamsRequest = this.httpParamsRequest;
        Map<String,String> httpHeaders = page.getPageRequestImpl().createHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();
        List<NameValuePair> params = this.paramList;

        HttpRequestResultImpl result = null;
        try
        {
            result = HttpUtil.httpPost(url, httpContext, httpParamsRequest, httpParamsDefault, httpHeaders, sslSelfSignedAllowed, params,overrideMime);
        }
        catch (Exception ex)
        {
            ItsNatDroidException exFinal = processException(ex);
            throw exFinal;

            // No usamos aquí el OnEventErrorListener porque la excepción es capturada por un catch anterior que sí lo hace
        }

        processResult(result,listener,errorMode);

        return result;
    }

    @Override
    public void requestAsync()
    {
        String url = getFinalURL();
        HttpActionGenericAsyncTask postTask = new HttpActionGenericAsyncTask(this,method,url, httpParamsRequest, paramList, listener,errorListener,errorMode,overrideMime);
        postTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // Con execute() a secas se ejecuta en un "pool" de un sólo hilo sin verdadero paralelismo
    }

    public ItsNatDroidException processException(Exception ex)
    {
        if (ex instanceof SocketTimeoutException) // Esperamos este error en el caso de timeout
             return new ItsNatDroidException(ex);
        else if (ex instanceof ItsNatDroidException)
            return (ItsNatDroidException)ex;
        else
            return new ItsNatDroidException(ex);
    }

    public void processResult(HttpRequestResultImpl result,OnHttpRequestListener listener,int errorMode)
    {
        StatusLine status = result.getStatusLine();
        int statusCode = status.getStatusCode();
        if (statusCode == 200)
        {
            if (listener != null) listener.onRequest(itsNatDoc.getPage(),result);
        }
        else // Error del servidor, lo normal es que haya lanzado una excepción
        {
            ItsNatDocImpl itsNatDoc = getItsNatDocImpl();
            itsNatDoc.showErrorMessage(true, result.responseText,errorMode);
            throw new ItsNatDroidServerResponseException(result);
        }
    }

    protected static String getBasePathOfURL(String urlStr)
    {
        URL u = null;
        try { u = new URL(urlStr); }
        catch (MalformedURLException ex) { throw new ItsNatDroidException(ex); }

        // Vale, sí, este código está basado en el código fuente de java.net.URLStreamHandler.toExternalForm()

        // pre-compute length of StringBuilder
        int len = u.getProtocol().length() + 1;
        if (u.getAuthority() != null && u.getAuthority().length() > 0)
            len += 2 + u.getAuthority().length();
        if (u.getPath() != null) {
            len += u.getPath().length();
        }
        /*
        if (u.getQuery() != null) {
            len += 1 + u.getQuery().length();
        }
        if (u.getRef() != null)
            len += 1 + u.getRef().length();
        */
        StringBuilder result = new StringBuilder(len);
        result.append(u.getProtocol());
        result.append(":");
        if (u.getAuthority() != null && u.getAuthority().length() > 0) {
            result.append("//");
            result.append(u.getAuthority());
        }
        if (u.getPath() != null) {
            result.append(u.getPath());
        }
        /*
        if (u.getQuery() != null) {
            result.append('?');
            result.append(u.getQuery());
        }
        if (u.getRef() != null) {
            result.append("#");
            result.append(u.getRef());
        }
        */
        return result.toString();
    }


}
