package org.itsnat.droid.impl.dom.layout;

/**
 * Created by jmarranz on 29/10/14.
 */
public class ScriptRemoteParsed extends ScriptParsed
{
    protected String src;

    public ScriptRemoteParsed(String src)
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
