package org.itsnat.droid.impl.model.layout;

import org.itsnat.droid.ItsNatDroidException;

/**
 * Created by jmarranz on 27/10/14.
 */
public class AttributeParsed
{
    protected String namespaceURI;
    protected String name;
    protected String value;

    public AttributeParsed(String namespaceURI, String name, String value)
    {
        this.namespaceURI = namespaceURI;
        this.name = name;
        this.value = value;
        if ("".equals(namespaceURI)) throw new ItsNatDroidException("Internal error: empty string not allowed"); // Debe ser null o una cadena no vacía
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
