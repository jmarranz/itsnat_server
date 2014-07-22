package org.itsnat.droid;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ItsNatDroidException extends RuntimeException
{
    public ItsNatDroidException()
    {
    }

    public ItsNatDroidException(String detailMessage)
    {
        super(detailMessage);
    }

    public ItsNatDroidException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    public ItsNatDroidException(Throwable throwable)
    {
        super(throwable);
    }
}
