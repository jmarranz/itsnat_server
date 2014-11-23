package org.itsnat.droid.impl.dom.layout;

/**
 * Created by jmarranz on 29/10/14.
 */
public class DOMScriptRemote extends DOMScript
{
    protected String src;

    public DOMScriptRemote(String src)
    {
        this.src = src;
    }

    public String getSrc()
    {
        return src;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
