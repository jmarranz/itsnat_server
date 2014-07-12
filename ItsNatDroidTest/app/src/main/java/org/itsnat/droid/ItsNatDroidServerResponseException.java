package org.itsnat.droid;

/**
 * Created by jmarranz on 11/06/14.
 */
public class ItsNatDroidServerResponseException extends ItsNatDroidException
{
    protected int statusCode;
    protected String reasonPhrase;
    protected byte[] content;

    public ItsNatDroidServerResponseException(int statusCode, String reasonPhrase, byte[] content)
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

    public byte[] getContent()
    {
        return content;
    }
}

