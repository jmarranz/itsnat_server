package org.itsnat.droid.impl.model;

import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;

import java.util.LinkedList;

/**
 * Created by jmarranz on 3/11/14.
 */
public abstract class XMLParsed
{
    protected long timestamp;
    protected MapLight<String,String> namespacesByPrefix = new MapLight<String,String>();
    protected String androidNSPrefix;
    protected ElementParsed rootElement;
    protected LinkedList<AttrParsedRemote> remoteAttribs;

    public XMLParsed()
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
        if (InflatedXML.XMLNS_ANDROID.equals(ns))
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

    public ElementParsed getRootElement()
    {
        return rootElement;
    }

    public void setRootElement(ElementParsed rootElement)
    {
        this.rootElement = rootElement;
    }

    public LinkedList<AttrParsedRemote> getAttributeRemoteList()
    {
        return remoteAttribs;
    }

    public void addAttributeRemote(AttrParsedRemote attr)
    {
        if (remoteAttribs == null) this.remoteAttribs = new LinkedList<AttrParsedRemote>();
        remoteAttribs.add(attr);
    }

    public void partialClone(XMLParsed cloned)
    {
        cloned.timestamp = this.timestamp; // Antes de clonar se ha actualizado
        cloned.namespacesByPrefix = this.namespacesByPrefix;
        cloned.androidNSPrefix = this.androidNSPrefix;
        cloned.rootElement = this.rootElement;
        cloned.remoteAttribs = this.remoteAttribs;
    }
}
