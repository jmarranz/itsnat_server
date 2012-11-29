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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import org.apache.xml.serialize.DOMSerializer;
import org.apache.xml.serialize.LineSeparator;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class DOMRenderXercesOldImpl extends DOMRenderImpl
{
    protected transient OutputFormat format; // transient ya no es necesario, no se serializa

    /** Creates a new instance of DOMRenderXercesOldImpl */
    public DOMRenderXercesOldImpl(Document doc,String mime,String encoding,boolean nodeOnlyRender)
    {
        super(doc,mime,encoding,nodeOnlyRender);

        this.format = createOutputFormat();
    }
/*
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        this.format = createOutputFormat();

        in.defaultReadObject();
    }
*/
    public OutputFormat createOutputFormat()
    {
        boolean omitDocType = nodeOnlyRender || (docRef.getDoctype() == null);  // Respetamos el original pues el render tiende a añadir el DOCTYPE aunque no exista en el documento.

        OutputFormat format = new OutputFormat(docRef,encoding,false);
        // El objetivo es que se conserve el original lo más exacto posible
        format.setLineSeparator(LineSeparator.Web);
        format.setPreserveSpace(true);
        format.setPreserveEmptyAttributes(true);
        format.setOmitDocumentType(omitDocType);
        format.setOmitXMLDeclaration(nodeOnlyRender);

        return format;
    }

    public DOMSerializer getDOMSerializer(Writer out,boolean docFragment)
    {
        // Estos métodos de serialización están deprecated, las alternativas son:
        // Xalan, el del JDK 1.4 (Xalan?), o W3C DOM Level 3 Load & Save
        // El problema de Xalan (incluido en Xerces y que se utiliza para application/xhtml+xml) es que
        // no serializa bien cuando el documento es servidor como text/html

        if (mime.equals("text/html"))
            return new ItsNatXercesHTMLSerializerOld(false,out,format,docFragment);
        //else if (mime.equals("application/xhtml+xml"))
        //    return new ItsNatXercesHTMLSerializerOld(true,out,format,docFragment);
        else
            throw new ItsNatException("INTERNAL ERROR"); // No usamos XMLSerializer
    }

    public void serializeDocument(Document doc,Writer out)
    {
        DOMSerializer serializer = getDOMSerializer(out,false);

        try
        {
            serializer.serialize(doc);
        }
        catch(IOException ex)
        {
            throw new ItsNatException(ex);
        }
    }

    public void serializeNode(Node node, Writer out)
    {
        try
        {
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                DOMSerializer serializer = getDOMSerializer(out,false);
                serializer.serialize((Element)node);
            }
            else
            {
                DOMSerializer serializer = getDOMSerializer(out,true);
                DocumentFragment docFrag = node.getOwnerDocument().createDocumentFragment();
                // Tenemos que clonar porque la inserción en el fragmento eliminaría el nodo del documento
                docFrag.appendChild(node.cloneNode(true));
                serializer.serialize(docFrag);
            }
        }
        catch(IOException ex)
        {
            throw new ItsNatDOMException(ex,node);
        }
    }

}
