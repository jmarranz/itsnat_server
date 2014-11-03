package org.itsnat.droid.impl.model;

/**
 * Created by jmarranz on 3/11/14.
 */
public abstract class XMLParsed
{
    protected ElementParsed rootElement;

    public XMLParsed()
    {
    }

    public ElementParsed getRootElement()
    {
        return rootElement;
    }

    public void setRootElement(ElementParsed rootElement)
    {
        this.rootElement = rootElement;
    }
}
