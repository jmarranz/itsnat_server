/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.markup.render;

import java.util.Properties;
import javax.xml.transform.OutputKeys;
import org.apache.xml.serializer.Method;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

/**
 *
 * @author jmarranz
 */
public abstract class DOMRenderJAXPBaseImpl extends DOMRenderImpl
{
    protected Properties defaultProps;

    /**
     * Creates a new instance of DOMRenderJAXPBaseImpl
     */
    public DOMRenderJAXPBaseImpl(Document doc,String mime,String encoding,boolean nodeOnlyRender)
    {
        super(doc,mime,encoding,nodeOnlyRender);

        this.defaultProps = getOutputProperties();
    }

    public Properties getOutputProperties()
    {
        String method = "";
        if (mime.equals("text/html"))
            method = Method.HTML;
        else if (mime.equals("application/xhtml+xml"))
            method = Method.XHTML;
        else // Casos: "text/xml" "image/svg+xml" etc
            method = Method.XML;

        // http://www.w3.org/TR/1999/REC-xslt-19991116#output
        Properties props = new Properties();
        props.put(OutputKeys.METHOD,method);
        props.put(OutputKeys.MEDIA_TYPE,mime);
        props.put(OutputKeys.ENCODING,encoding);
        props.put(OutputKeys.INDENT,"no");

        if (!nodeOnlyRender)
        {
            DocumentType documentType = docRef.getDoctype();
            if (documentType != null)
            {
                String publicId = documentType.getPublicId();
                if (publicId != null)
                    props.put(OutputKeys.DOCTYPE_PUBLIC,publicId); // Ej. "-//W3C//DTD HTML 4.01//EN"
                String systemId = documentType.getSystemId();
                if (systemId != null)
                    props.put(OutputKeys.DOCTYPE_SYSTEM,systemId); // Ej. "http://www.w3.org/TR/html4/strict.dtd"
            }
        }

        props.put(OutputKeys.OMIT_XML_DECLARATION,"yes"); // Siempre omitimos pues la inserción de la declaración XML se hace en otro lugar, además puede ser un renderer de nodos (no de documento)

        // En OutputPropertiesFactory hay propiedades no estándar pero alguna muy útil
        props.put(OutputPropertiesFactory.S_OMIT_META_TAG,"yes"); // Si no ponemos esto por defecto añade un META debajo del HEAD tal y como: <META http-equiv="Content-Type" content="text/html; charset=UTF-8" >

//        props.put(OutputKeys.CDATA_SECTION_ELEMENTS,"SCRIPT script STYLE style");

        return props;
    }
}
