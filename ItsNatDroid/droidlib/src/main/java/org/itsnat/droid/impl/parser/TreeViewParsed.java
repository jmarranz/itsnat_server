package org.itsnat.droid.impl.parser;

import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jmarranz on 27/10/14.
 */
public class TreeViewParsed
{
    protected String markup;
    protected long timestamp;
    protected ViewParsed rootView;
    protected MapLight<String,String> namespacesByPrefix = new MapLight<String,String>();
    protected String androidNSPrefix;
    protected String loadScript;
    protected List<ScriptParsed> scriptList;

    public TreeViewParsed(String markup)
    {
        this.markup = markup;
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
        return rootView;
    }

    public void setRootView(ViewParsed rootView)
    {
        this.rootView = rootView;
    }

    public String getLoadScript()
    {
        return loadScript;
    }

    public void setLoadScript(String loadScript)
    {
        this.loadScript = loadScript;
    }

    public List<ScriptParsed> getScriptList()
    {
        return scriptList;
    }

    public void addScript(ScriptParsed script)
    {
        if (scriptList == null) this.scriptList = new LinkedList<ScriptParsed>();
        scriptList.add(script);
    }
}