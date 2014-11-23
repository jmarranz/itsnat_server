package org.itsnat.droid.impl.dom;

import org.itsnat.droid.ItsNatDroidException;

/**
 * Created by jmarranz on 27/10/14.
 */
public abstract class DOMAttr
{
    protected String namespaceURI;
    protected String name;
    protected String value;

    protected DOMAttr(String namespaceURI, String name, String value)
    {
        this.namespaceURI = namespaceURI;
        this.name = name;
        this.value = value;
        if ("".equals(namespaceURI)) throw new ItsNatDroidException("Internal error: empty string not allowed"); // Debe ser null o una cadena no vacía
    }

    public static DOMAttr create(String namespaceURI, String name, String value)
    {
        if (DOMAttrRemote.isRemote(namespaceURI, value))
            return new DOMAttrRemote(namespaceURI,name,value);
        else if (DOMAttrAsset.isAsset(namespaceURI, value))
            return new DOMAttrAsset(namespaceURI,name,value);
        else
            return new DOMAttrLocalResource(namespaceURI,name,value);
    }

    public static DOMAttr create(DOMAttr attr, String newValue)
    {
        return new DOMAttrLocalResource(attr.getNamespaceURI(),attr.getName(),newValue);
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
