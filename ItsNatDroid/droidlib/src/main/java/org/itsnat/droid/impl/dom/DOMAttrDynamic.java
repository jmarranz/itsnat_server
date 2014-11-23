package org.itsnat.droid.impl.dom;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.HttpUtil;

/**
 * Created by jmarranz on 3/11/14.
 */
public class DOMAttrDynamic extends DOMAttr
{
    protected String resType;
    protected String extension;
    protected String mime;
    protected String location;
    protected volatile Object resource;

    public DOMAttrDynamic(String namespaceURI, String name, String value)
    {
        super(namespaceURI, name, value);

        // Ej. @assets:drawable/res/drawable/file.png   Path: res/drawable/file.png

        // Ej. @remote:drawable/res/drawable/file.png   Remote Path: res/drawable/file.png
        //     @remote:drawable//res/drawable/file.png  Remote Path: /res/drawable/file.png
        //     @remote:drawable/http://somehost/res/drawable/file.png  Remote Path: http://somehost/res/drawable/file.png
        //     @remote:drawable/ItsNatDroidServletExample?itsnat_doc_name=test_droid_remote_drawable

        int pos1 = value.indexOf(':');
        int pos2 = value.indexOf('/');
        this.resType = value.substring(pos1 + 1,pos2); // Ej. "drawable"
        this.location = value.substring(pos2 + 1);
        int pos3 = value.lastIndexOf('.');
        if (pos3 != -1)
        {
            this.extension = value.substring(pos3 + 1).toLowerCase(); // xml, png...
            // http://www.sitepoint.com/web-foundations/mime-types-complete-list/
            String mime = HttpUtil.MIME_BY_EXT.get(extension);
            if (mime == null)
                throw new ItsNatDroidException("Unexpected extension: \"" + extension + "\" Remote resource: " + value);
            this.mime = mime;
        }
        else
        {
            // Por ejemplo:  @remote:drawable/ItsNatDroidServletExample?itsnat_doc_name=test_droid_remote_drawable
            // Suponemos que se genera el XML por ej del drawable
            this.extension = null;
            this.mime = HttpUtil.MIME_XML;
        }
    }

    public String getResourceType()
    {
        return resType;
    }

    public String getExtension()
    {
        return extension;
    }

    public String getLocation()
    {
        return location;
    }

    public String getResourceMime()
    {
        return mime;
    }

    public Object getResource()
    {
        // Es sólo llamado en el hilo UI pero setResource se llama en multihilo
        return resource;
    }

    public void setResource(Object resource)
    {
        // Es llamado en multihilo
        // No pasa nada porque se llame e inmediatamente después se cambie el valor, puede ocurrir que se esté procesando
        // el mismo atributo a la vez por dos hilos, ten en cuenta que el template puede estar cacheado y reutilizado, pero no pasa nada
        // porque el nuevo remoteResource NUNCA es null y es siempre el mismo recurso como mucho actualizado si ha cambiado
        // en el servidor
        this.resource = resource;
    }
}
