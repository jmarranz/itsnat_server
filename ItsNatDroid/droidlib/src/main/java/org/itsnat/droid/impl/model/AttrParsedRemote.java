package org.itsnat.droid.impl.model;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;

/**
 * Created by jmarranz on 3/11/14.
 */
public class AttrParsedRemote extends AttrParsed
{
    protected String resType;
    protected String extension;
    protected String mime;
    protected String remoteLocation;
    protected Object remoteResource;

    public AttrParsedRemote(String namespaceURI, String name, String value)
    {
        super(namespaceURI, name, value);

        // Ej. @remote:drawable/res/drawable/file.png   Remote Path: res/drawable/file.png
        //     @remote:drawable//res/drawable/file.png  Remote Path: /res/drawable/file.png
        //     @remote:drawable/http://somehost/res/drawable/file.png  Remote Path: http://somehost/res/drawable/file.png
        int pos1 = value.indexOf(':');
        int pos2 = value.indexOf('/');
        int pos3 = value.indexOf('.');
        this.resType = value.substring(pos1 + 1,pos2); // Ej. "drawable"
        this.remoteLocation = value.substring(pos2 + 1);
        this.extension = value.substring(pos3 + 1).toLowerCase(); // xml, png, 9.png

        // http://www.sitepoint.com/web-foundations/mime-types-complete-list/
        String mime = HttpUtil.MIME_BY_EXT.get(extension);
        if (mime == null)
            throw new ItsNatDroidException("Unexpected extension: " + extension);
        this.mime = mime;
    }

    public static boolean isRemote(String namespaceURI,String value)
    {
        return (InflatedLayoutImpl.XMLNS_ANDROID.equals(namespaceURI) && value.startsWith("@remote:"));
    }

    public String getResourceType()
    {
        return resType;
    }

    public String getExtension()
    {
        return extension;
    }

    public String getRemoteLocation()
    {
        return remoteLocation;
    }

    public String getResourceMime()
    {
        return mime;
    }

    public Object getRemoteResource()
    {
        return remoteResource;
    }

    public void setRemoteResource(Object remoteResource)
    {
        this.remoteResource = remoteResource;
    }
}
