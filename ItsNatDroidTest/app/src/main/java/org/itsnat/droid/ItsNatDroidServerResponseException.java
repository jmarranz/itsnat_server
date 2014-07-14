package org.itsnat.droid;

/**
 * Created by jmarranz on 11/06/14.
 */
public class ItsNatDroidServerResponseException extends ItsNatDroidException
{
    protected int statusCode;
    protected String reasonPhrase;
    protected String content;

    public ItsNatDroidServerResponseException(int statusCode, String reasonPhrase, String content)
    {
        super("Server response error");
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.content = content;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public String getReasonPhrase()
    {
        return reasonPhrase;
    }

    public String getContent()
    {
        return content;
    }
}

