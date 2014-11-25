package org.itsnat.droid.impl.dom;

import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;

import java.util.LinkedList;

/**
 * Created by jmarranz on 3/11/14.
 */
public abstract class XMLDOM
{
    protected long timestamp;
    protected MapLight<String,String> namespacesByPrefix = new MapLight<String,String>();
    protected String androidNSPrefix;
    protected DOMElement rootElement;
    protected LinkedList<DOMAttrRemote> remoteAttribList;

    public XMLDOM()
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

    public DOMElement getRootElement()
    {
        return rootElement;
    }

    public void setRootElement(DOMElement rootElement)
    {
        this.rootElement = rootElement;
    }

    public LinkedList<DOMAttrRemote> getDOMAttrRemoteList()
    {
        return remoteAttribList;
    }

    public void addDOMAttrRemote(DOMAttrRemote attr)
    {
        if (remoteAttribList == null) this.remoteAttribList = new LinkedList<DOMAttrRemote>();
        remoteAttribList.add(attr);
    }

    public void partialClone(XMLDOM cloned)
    {
        cloned.timestamp = this.timestamp; // Antes de clonar se ha actualizado
        cloned.namespacesByPrefix = this.namespacesByPrefix;
        cloned.androidNSPrefix = this.androidNSPrefix;
        cloned.rootElement = this.rootElement;
        cloned.remoteAttribList = this.remoteAttribList;
    }
}
