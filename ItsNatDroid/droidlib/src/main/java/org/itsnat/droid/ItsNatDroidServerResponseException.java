package org.itsnat.droid;

/**
 * Created by jmarranz on 11/06/14.
 */
public class ItsNatDroidServerResponseException extends ItsNatDroidException
{
    protected HttpRequestResult result;

    public ItsNatDroidServerResponseException(String msg,HttpRequestResult result)
    {
        super(msg);
        this.result = result;
    }

    public ItsNatDroidServerResponseException(HttpRequestResult result)
    {
        super("Server response error");
        this.result = result;
    }

    public ItsNatDroidServerResponseException(Throwable cause,HttpRequestResult result)
    {
        super("Server response error",cause);
        this.result = result;
    }

    public HttpRequestResult getHttpRequestResult()
    {
        return result;
    }
}

