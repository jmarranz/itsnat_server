package org.itsnat.droid.impl.dom;

/**
 * Created by jmarranz on 3/11/14.
 */
public class DOMAttrLocalResource extends DOMAttr
{
    public DOMAttrLocalResource(String namespaceURI, String name, String value)
    {
        super(namespaceURI, name, value);
    }

    public static DOMAttrLocalResource createDOMAttrLocalResource(DOMAttrLocalResource attr, String newValue)
    {
        return new DOMAttrLocalResource(attr.getNamespaceURI(),attr.getName(),newValue);
    }
}
