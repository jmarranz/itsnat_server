package org.itsnat.droid.impl.browser;

import org.apache.http.params.HttpParams;
import org.itsnat.droid.ClientErrorMode;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnHttpRequestErrorListener;
import org.itsnat.droid.OnHttpRequestListener;
import org.itsnat.droid.impl.browser.HttpRequestResultImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;
import org.itsnat.droid.impl.util.ValueUtil;

import java.net.SocketTimeoutException;

/**
 * Created by jmarranz on 9/10/14.
 */
public abstract class GenericHttpClientBaseImpl //implements GenericHttpClient
{
    protected ItsNatDocImpl itsNatDoc;
    protected HttpParams httpParamsRequest;
    protected OnHttpRequestErrorListener errorListener;
    protected int errorMode = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS;
    protected String userUrl;
    protected OnHttpRequestListener listener;
    protected String method = "POST"; // Por defecto

    public GenericHttpClientBaseImpl(ItsNatDocImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        PageImpl page = itsNatDoc.getPageImpl();
        HttpParams httpParamsRequest = page.getHttpParams();
        this.httpParamsRequest = httpParamsRequest != null ? httpParamsRequest.copy() : null;
    }

    protected GenericHttpClientBaseImpl()
    {
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

    public void setErrorModeNotFluid(int errorMode)
    {
        if (errorMode == ClientErrorMode.NOT_CATCH_ERRORS) throw new ItsNatDroidException("ClientErrorMode.NOT_CATCH_ERRORS is not supported"); // No tiene mucho sentido porque el objetivo es dejar fallar y si el usuario no ha registrado "error listeners" ItsNat Droid deja siempre fallar lanzando la excepción
        this.errorMode = errorMode;
    }

    public void setOnHttpRequestListenerNotFluid(OnHttpRequestListener listener)
    {
        this.listener = listener;
    }

    public void setOnHttpRequestErrorListenerNotFluid(OnHttpRequestErrorListener httpErrorListener)
    {
        this.errorListener = httpErrorListener;
    }

    public void setMethodNotFluid(String method)
    {
        this.method = method;
    }

    public void setURLNotFluid(String url)
    {
        this.userUrl = url;
    }

    public void setRequestHeaderNotFluid(String header, Object value)
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
    }

    public String getPageURL()
    {
        return itsNatDoc.getPageURLBase();
    }

    protected String getFinalURL()
    {
        return ValueUtil.isEmpty(userUrl) ? itsNatDoc.getPageURLBase() : userUrl; // Como se puede ver seguridad de "single server" ninguna
    }

    public ItsNatDroidException convertException(Exception ex)
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
        if (result.isStatusOK())
        {
            if (listener != null) listener.onRequest(itsNatDoc.getPage(),result);
        }
        else // Error del servidor, lo normal es que haya lanzado una excepción
        {
            ItsNatDocImpl itsNatDoc = getItsNatDocImpl();
            itsNatDoc.showErrorMessage(true, result.getResponseText(),errorMode);
            throw new ItsNatDroidServerResponseException(result);
        }
    }

}
