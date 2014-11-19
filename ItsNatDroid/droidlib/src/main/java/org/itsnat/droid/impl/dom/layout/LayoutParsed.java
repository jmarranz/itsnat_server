package org.itsnat.droid.impl.dom.layout;

import org.itsnat.droid.impl.dom.XMLParsed;

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

    public static String extractLoadScriptMarkup(String markup,String[] markupNoLoadScript)
    {
        markupNoLoadScript[0] = markup;

        // Este es el patrón esperado: <script id="itsnat_load_script"><![CDATA[ code ]]></script></lastElement>
        String patternStart = "<script id=\"itsnat_load_script\"><![CDATA[";
        int start = markup.lastIndexOf(patternStart);
        if (start == -1) return null; // No hay script de carga
        start += patternStart.length();

        String patternEnd = "]]></script>";
        int end = markup.lastIndexOf(patternEnd);
        if (end == -1) return null; // No hay script de carga

        String loadScript = markup.substring(start,end);
        markupNoLoadScript[0] = markup.substring(0,start) + markup.substring(end);
        return loadScript;
    }

    public LayoutParsed partialClone()
    {
        // Reutilizamos t_odo excepto el loadScript pues es la única parte que cambia
        LayoutParsed cloned = new LayoutParsed();
        partialClone(cloned);
        cloned.scriptList = this.scriptList;
        return cloned;
    }
}
