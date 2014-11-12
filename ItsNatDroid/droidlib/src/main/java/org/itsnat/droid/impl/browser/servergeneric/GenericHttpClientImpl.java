package org.itsnat.droid.impl.browser.servergeneric;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.GenericHttpClient;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnHttpRequestErrorListener;
import org.itsnat.droid.OnHttpRequestListener;
import org.itsnat.droid.impl.browser.HttpRequestResultImpl;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 9/10/14.
 */
public class GenericHttpClientImpl extends GenericHttpClientBaseImpl implements GenericHttpClient
{
    protected List<NameValuePair> paramList = new ArrayList<NameValuePair>(10);
    protected String overrideMime;

    public GenericHttpClientImpl(ItsNatDocImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    @Override
    public GenericHttpClient setErrorMode(int errorMode)
    {
        setErrorModeNotFluid(errorMode);
        return this;
    }

    @Override
    public GenericHttpClient setOnHttpRequestListener(OnHttpRequestListener listener)
    {
        setOnHttpRequestListenerNotFluid(listener);
        return this;
    }

    @Override
    public GenericHttpClient setOnHttpRequestErrorListener(OnHttpRequestErrorListener httpErrorListener)
    {
        setOnHttpRequestErrorListenerNotFluid(httpErrorListener);
        return this;
    }

    @Override
    public GenericHttpClient setMethod(String method)
    {
        setMethodNotFluid(method);
        return this;
    }

    @Override
    public GenericHttpClient setURL(String url)
    {
        setURLNotFluid(url);
        return this;
    }

    public void addParamNotFluid(String name,String value)
    {
        // Si se añade el mismo parámetro varias veces, es el caso de multivalor
        paramList.add(new BasicNameValuePair(name,value));
    }

    public void clearParamsNotFluid()
    {
        paramList.clear();
    }

    @Override
    public GenericHttpClient addParam(String name,String value)
    {
        addParamNotFluid(name,value);
        return this;
    }

    @Override
    public GenericHttpClient clearParams()
    {
        clearParamsNotFluid();
        return this;
    }

    @Override
    public GenericHttpClient setRequestHeader(String header, Object value)
    {
        setRequestHeaderNotFluid(header,value);
        return this;
    }

    public void setOverrideMimeTypeNotFluid(String mime)
    {
        this.overrideMime = mime;
    }

    @Override
    public GenericHttpClient setOverrideMimeType(String mime)
    {
        setOverrideMimeTypeNotFluid(mime);
        return this;
    }

    public HttpRequestResult request(boolean async) // No es público
    {
        if (async)
        {
            requestAsync();
            return null;
        }
        else return requestSync();
    }

    @Override
    public HttpRequestResult requestSync()
    {
        PageImpl page = getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();

        // No hace falta clonar porque es síncrona la llamada
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
            ItsNatDroidException exFinal = convertException(ex);
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

}
