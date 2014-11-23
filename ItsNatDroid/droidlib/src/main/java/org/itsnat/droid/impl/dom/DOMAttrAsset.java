package org.itsnat.droid.impl.dom;

import org.itsnat.droid.impl.xmlinflated.InflatedXML;

/**
 * Created by jmarranz on 3/11/14.
 */
public class DOMAttrAsset extends DOMAttrDynamic
{
    public DOMAttrAsset(String namespaceURI, String name, String value)
    {
        super(namespaceURI, name, value);
    }

    public static boolean isAsset(String namespaceURI,String value)
    {
        return (InflatedXML.XMLNS_ANDROID.equals(namespaceURI) && value.startsWith("@assets:"));
    }
}
