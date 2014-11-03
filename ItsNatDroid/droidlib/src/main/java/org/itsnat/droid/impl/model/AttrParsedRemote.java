package org.itsnat.droid.impl.model;

import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;

/**
 * Created by jmarranz on 3/11/14.
 */
public class AttrParsedRemote extends AttrParsed
{
    protected Object remoteResource;

    public AttrParsedRemote(String namespaceURI, String name, String value)
    {
        super(namespaceURI, name, value);
    }

    public static boolean isRemote(String namespaceURI,String value)
    {
        return (XMLLayoutInflateService.XMLNS_ANDROID.equals(namespaceURI) && value.startsWith("@remote:"));
    }
}
