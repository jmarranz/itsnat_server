package org.itsnat.droid.impl.parser;

/**
 * Created by jmarranz on 27/10/14.
 */
public class Attribute
{
    protected String namespaceURI;
    protected String name;
    protected String value;

    public Attribute(String namespaceURI, String name, String value)
    {
        this.namespaceURI = namespaceURI;
        this.name = name;
        this.value = value;
    }
}
