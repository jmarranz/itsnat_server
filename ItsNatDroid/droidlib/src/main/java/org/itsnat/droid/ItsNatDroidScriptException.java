package org.itsnat.droid;

/**
 * Created by jmarranz on 11/06/14.
 */
public class ItsNatDroidScriptException extends ItsNatDroidException
{
    protected String code;

    public ItsNatDroidScriptException(Throwable throwable,String code)
    {
        super(throwable);
        this.code = code;
    }

    public String getScript()
    {
        return code;
    }
}
