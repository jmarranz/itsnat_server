package org.itsnat.droid.impl.model;

import org.itsnat.droid.ItsNatDroidException;

/**
 * Created by jmarranz on 27/10/14.
 */
public abstract class AttrParsed
{
    protected String namespaceURI;
    protected String name;
    protected String value;

    public AttrParsed(String namespaceURI, String name, String value)
    {
        this.namespaceURI = namespaceURI;
        this.name = name;
        this.value = value;
        if ("".equals(namespaceURI)) throw new ItsNatDroidException("Internal error: empty string not allowed"); // Debe ser null o una cadena no vacía
    }

    public static AttrParsed createAttrParsed(String namespaceURI,String name,String value)
    {
        if (AttrParsedRemote.isRemote(namespaceURI, value))
            return new AttrParsedRemote(namespaceURI,name,value);
        else
            return new AttrParsedDefault(namespaceURI,name,value);
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
