package org.itsnat.droid.impl.browser.clientdoc;

/**
 * Created by jmarranz on 25/06/14.
 */
public class AttrImpl
{
    protected String namespaceURI;
    protected String name;
    protected String value;

    public AttrImpl(String namespaceURI,String name,String value)
    {
        this.namespaceURI = namespaceURI;
        this.name = name;
        this.value = value;
    }

    public String getNamespaceURI()
    {
        return namespaceURI;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }
}
