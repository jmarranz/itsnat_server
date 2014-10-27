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
    protected ViewParsed rootView;
    protected MapLight<String,String> namespacesByPrefix = new MapLight<String,String>();
    protected String androidNSPrefix;
    protected String loadScript;
    protected List<String> scriptList;

    public TreeViewParsed()
    {
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

    public List<String> getScriptList()
    {
        return scriptList;
    }

    public void addScript(String script)
    {
        if (scriptList == null) this.scriptList = new LinkedList<String>();
        this.scriptList.add(script);
    }
}
