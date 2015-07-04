package org.itsnat.droid.impl.browser;

import org.apache.http.NameValuePair;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnHttpRequestErrorListener;
import org.itsnat.droid.OnHttpRequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpActionGenericAsyncTask extends ProcessingAsyncTask<HttpRequestResultImpl>
{
    protected GenericHttpClientImpl parent;
    protected String method;
    protected String url;
    protected HttpRequestData httpRequestData;
    protected List<NameValuePair> params;
    protected OnHttpRequestListener listener;
    protected OnHttpRequestErrorListener errorListener;
    protected int errorMode;
    protected String overrideMime;


    public HttpActionGenericAsyncTask(GenericHttpClientImpl parent,String method, String url, List<NameValuePair> params, OnHttpRequestListener listener, OnHttpRequestErrorListener errorListener, int errorMode, String overrideMime)
    {
        PageImpl page = parent.getPageImpl();

        this.parent = parent;
        this.method = method;
        this.url = url;
        this.httpRequestData = new HttpRequestData(page);
        this.params = new ArrayList<NameValuePair>(params); // hace una copia, los NameValuePair son de sólo lectura por lo que no hay problema compartirlos en hilos
        this.listener = listener;
        this.errorListener = errorListener;
        this.errorMode = errorMode;
        this.overrideMime = overrideMime;
    }

    protected HttpRequestResultOKImpl executeInBackground() throws Exception
    {
        return HttpUtil.httpAction(method,url, httpRequestData, params,overrideMime);
    }

    @Override
    protected void onFinishOk(HttpRequestResultImpl result)
    {
        try
        {
            parent.processResult(result,listener,errorMode);
        }
        catch(Exception ex)
        {
            if (errorListener != null)
            {
                errorListener.onError(parent.getPageImpl(), ex, result);
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
        ItsNatDroidException exFinal = parent.convertException(ex);

        if (errorListener != null)
        {
            HttpRequestResult result = (exFinal instanceof ItsNatDroidServerResponseException) ?
                    ((ItsNatDroidServerResponseException)exFinal).getHttpRequestResult() : null;
            errorListener.onError(parent.getPageImpl(),exFinal, result);
            return;
        }
        else
        {
            if (exFinal instanceof ItsNatDroidException) throw (ItsNatDroidException)exFinal;
            else throw new ItsNatDroidException(exFinal);
        }
    }
}

