package org.itsnat.droid.impl.dom.layout;

import org.itsnat.droid.impl.dom.XMLDOM;

import java.util.ArrayList;

/**
 * Created by jmarranz on 27/10/14.
 */
public class XMLDOMLayout extends XMLDOM
{
    protected String loadScript;
    protected ArrayList<DOMScript> scriptList;

    public XMLDOMLayout()
    {
    }

    public DOMView getRootView()
    {
        return (DOMView)getRootElement();
    }

    public String getLoadScript()
    {
        return loadScript;
    }

    public void setLoadScript(String loadScript)
    {
        this.loadScript = loadScript;
    }

    public ArrayList<DOMScript> getDOMScriptList()
    {
        return scriptList;
    }

    public void addDOMScript(DOMScript script)
    {
        if (scriptList == null) this.scriptList = new ArrayList<DOMScript>();
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

    public XMLDOMLayout partialClone()
    {
        // Reutilizamos t_odo excepto el loadScript pues es la única parte que cambia
        XMLDOMLayout cloned = new XMLDOMLayout();
        partialClone(cloned);
        cloned.loadScript = null; // Para que quede claro que no se clona
        cloned.scriptList = this.scriptList;
        return cloned;
    }
}
