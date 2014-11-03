package org.itsnat.droid.impl.model.layout;

import org.itsnat.droid.impl.model.XMLParsed;
import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;

import java.util.ArrayList;

/**
 * Created by jmarranz on 27/10/14.
 */
public class LayoutParsed extends XMLParsed
{
    protected long timestamp;
    protected MapLight<String,String> namespacesByPrefix = new MapLight<String,String>();
    protected String androidNSPrefix;
    protected String loadScript;
    protected ArrayList<ScriptParsed> scriptList;

    public LayoutParsed()
    {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public long updateTimestamp()
    {
        long timestampOld = this.timestamp;
        this.timestamp = System.currentTimeMillis();
        return timestampOld;
    }

    public String getAndroidNSPrefix()
    {
        return androidNSPrefix;
    }

    public void addNamespace(String prefix,String ns)
    {
        namespacesByPrefix.put(prefix,ns);
        if (XMLLayoutInflateService.XMLNS_ANDROID.equals(ns))
            this.androidNSPrefix = prefix;
    }

    public MapLight<String,String> getNamespacesByPrefix()
    {
        return namespacesByPrefix;
    }

    public String getNamespace(String prefix)
    {
        return namespacesByPrefix.get(prefix);
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
