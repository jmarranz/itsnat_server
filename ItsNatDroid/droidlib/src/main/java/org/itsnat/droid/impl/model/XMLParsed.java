package org.itsnat.droid.impl.model;

import java.util.LinkedList;

/**
 * Created by jmarranz on 3/11/14.
 */
public abstract class XMLParsed
{
    protected long timestamp;
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
}
