package org.itsnat.droid.impl.model.layout;

import org.itsnat.droid.impl.model.XMLParsed;

import java.util.ArrayList;

/**
 * Created by jmarranz on 27/10/14.
 */
public class LayoutParsed extends XMLParsed
{
    protected String loadScript;
    protected ArrayList<ScriptParsed> scriptList;

    public LayoutParsed()
    {
    }

    public ViewParsed getRootView()
    {
        return (ViewParsed)getRootElement();
    }

    public void setRootView(ViewParsed rootView)
    {
        setRootElement(rootView);
    }

    public String getLoadScript()
    {
        return loadScript;
    }

    public void setLoadScript(String loadScript)
    {
        this.loadScript = loadScript;
    }

    public ArrayList<ScriptParsed> getScriptList()
    {
        return scriptList;
    }

    public void addScript(ScriptParsed script)
    {
        if (scriptList == null) this.scriptList = new ArrayList<ScriptParsed>();
        scriptList.add(script);
    }
}
