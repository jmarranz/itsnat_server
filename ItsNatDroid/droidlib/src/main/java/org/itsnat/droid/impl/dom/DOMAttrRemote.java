package org.itsnat.droid.impl.dom;

import org.itsnat.droid.impl.xmlinflated.InflatedXML;

/**
 * Created by jmarranz on 3/11/14.
 */
public class DOMAttrRemote extends DOMAttrDynamic
{
    public DOMAttrRemote(String namespaceURI, String name, String value)
    {
        super(namespaceURI, name, value);
    }

    public boolean isDownloaded()
    {
        return resource != null;
    }

    public static boolean isRemote(String namespaceURI,String value)
    {
        return (InflatedXML.XMLNS_ANDROID.equals(namespaceURI) && value.startsWith("@remote:"));
    }

    public String getRemoteLocation()
    {
        return getLocation();
    }

    public Object getRemoteResource()
    {
        // Es sólo llamado en el hilo UI pero setRemoteResource se ha llama en multihilo
        return getResource();
    }

    public void setRemoteResource(Object remoteResource)
    {
        setResource(remoteResource);
    }
}
