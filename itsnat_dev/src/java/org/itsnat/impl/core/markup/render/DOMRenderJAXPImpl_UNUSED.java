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

import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatException;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Es la más estándar pero tiene el problema de que introduce un META no deseado y no se como
 * quitarlo pues aunque usa Xalan no detecta la orden de quitarlo.
 *
 * @author jmarranz
 */
public class DOMRenderJAXPImpl_UNUSED extends DOMRenderJAXPBaseImpl
{
    public static final TransformerFactory transformFact = TransformerFactory.newInstance();

    public Transformer serializer;

    /**
     * Creates a new instance of DOMRenderJAXPImpl_UNUSED
     */
    public DOMRenderJAXPImpl_UNUSED(Document doc,String mime,String encoding,boolean nodeOnlyRender)
    {
        super(doc,mime,encoding,nodeOnlyRender);

        this.serializer = getDOMSerializer();
    }

    public Properties getOutputProperties()
    {
        Properties props = super.getOutputProperties();

        // El problema es que no funciona el omit-meta-tag por esta vía

        // En OutputPropertiesFactory hay propiedades no estándar pero alguna muy útil
        // Si no ponemos esto por defecto añade un META debajo del HEAD tal y como: <META http-equiv="Content-Type" content="text/html; charset=UTF-8" >
        props.put("{http\u003a//xml.apache.org/xalan}omit-meta-tag","yes");
        props.put("{http\u003a//xml.apache.org/xslt}omit-meta-tag","yes");
        props.put("{http://xml.apache.org/xalan}omit-meta-tag","yes");
        props.put("{http://xml.apache.org/xslt}omit-meta-tag","yes");
        // NO funciona ninguna

        return props;
    }

    public Transformer getDOMSerializer()
    {
        Transformer serializer;
        try
        {
            serializer = transformFact.newTransformer();
        }
        catch (TransformerConfigurationException ex)
        {
            throw new ItsNatException(ex);
        }
        serializer.setOutputProperties(defaultProps);
        return serializer;
    }

    public void serializeDocument(Document doc,Writer out)
    {
        serializeNode(doc,out);
    }

    public void serializeNode(Node node,Writer out)
    {
        // Se usa Xalan por debajo
        DOMSource source = new DOMSource(node);
        StreamResult result = new StreamResult(out);
        try
        {
            serializer.transform(source,result);
        }
        catch (TransformerException ex)
        {
            throw new ItsNatDOMException(ex,node);
        }
    }
}
