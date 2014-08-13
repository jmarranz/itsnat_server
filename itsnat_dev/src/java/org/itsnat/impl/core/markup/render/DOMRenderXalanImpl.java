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
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import org.apache.xml.serializer.DOMSerializer;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * El código serializador de Xalan ya incluido en Xerces (serializer.jar)
 * 
 * @author jmarranz
 */
public class DOMRenderXalanImpl extends DOMRenderJAXPBaseImpl
{
    /** Creates a new instance of DOMRenderXalanImpl */
    public DOMRenderXalanImpl(Document doc,String mime,String encoding,boolean nodeOnlyRender)
    {
        super(doc,mime,encoding,nodeOnlyRender);
    }

    @Override
    public Properties getOutputProperties()
    {
        Properties props = super.getOutputProperties();

        // En OutputPropertiesFactory hay propiedades no estándar pero alguna muy útil
        props.put(OutputPropertiesFactory.S_OMIT_META_TAG,"yes"); // Si no ponemos esto por defecto añade un META debajo del HEAD tal y como: <META http-equiv="Content-Type" content="text/html; charset=UTF-8" >

        return props;
    }

    public DOMSerializer getDOMSerializer(Writer out)
    {
        // Notar que las clases deben estar en:
        // org.apache.xml.serializer (con r al final)
        // el mismo sin "r" al final tiene la mayoría de clases
        // despreciadas

        Serializer serializer = SerializerFactory.getSerializer(defaultProps);
        serializer.setWriter(out);
        try
        {
            return serializer.asDOMSerializer();
        }
        catch(IOException ex)
        {
            throw new ItsNatException(ex);
        }
    }

    public void serializeDocument(Document doc,Writer out)
    {
        serializeNode(doc,out);
    }

    public void serializeNode(Node node,Writer out)
    {
        DOMSerializer serializer = getDOMSerializer(out);
        try
        {
            serializer.serialize(node);
        }
        catch(IOException ex)
        {
            throw new ItsNatDOMException(ex,node);
        }
    }
}
