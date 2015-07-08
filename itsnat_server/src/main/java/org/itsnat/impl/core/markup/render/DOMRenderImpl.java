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

import java.io.StringWriter;
import java.io.Writer;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.itsnat.impl.core.domimpl.XMLDecImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class DOMRenderImpl
{
    protected Document docRef;
    protected String mime;
    protected String encoding;
    protected boolean nodeOnlyRender;

    /** Creates a new instance of DOMRenderImpl */
    public DOMRenderImpl(Document doc,String mime,String encoding,boolean nodeOnlyRender)
    {
        this.docRef = doc;
        this.mime = mime;
        this.encoding = encoding;
        this.nodeOnlyRender = nodeOnlyRender;
    }

    public static DOMRenderImpl createDOMRender(Document doc,String mime,String encoding,boolean nodeOnlyRender)
    {
        // Aquí se puede cambiar fácilmente el método elegido.
        // Internet Explorer no soporta XHTML (6.0 al menos) es decir recibir un documento
        // con MIME application/xhtml+xml en la cabecera HTTP (ContentType), pero Firefox sí.
        // http://en.wikipedia.org/wiki/Criticisms_of_Internet_Explorer#XHTML
        // http://msdn.microsoft.com/library/default.asp?url=/workshop/networking/moniker/overview/appendix_a.asp

        // El soporte de XHTML como tal sólo tiene sentido para Firefox y similares, aunque
        // siempre se puede serializar como HTML un XHTML eligiendo como MIME text/html
        // al registrar la página, aunque el resultado no sea un XML (al menos la plantilla sí es un XML-XHTML).

        // return new DOMRenderXalanImpl(doc,mime,encoding,nodeOnlyRender);

        if (mime.equals("text/html"))
            return new DOMRenderXercesOldImpl(doc,mime,encoding,nodeOnlyRender);
        else // application/xhtml+xml , text/xml, svg, xul etc
            return new DOMRenderXalanImpl(doc,mime,encoding,nodeOnlyRender);
    }

    /*
    public String serializeNode(Node node)
    {
        StringWriter out = new StringWriter();
        serializeNode(node,out);
        return out.toString();
    }
     */

    public String serializeDocument(Document doc)
    {
        StringWriter out = new StringWriter();
        serializeDocument(doc,out);
        String markup = out.toString();

        XMLDecImpl xmlDec = ((DocumentImpl)doc).getXMLDec();
        if (xmlDec != null)
        {
            // Añadimos la declaración XML puesta en el template original
            StringBuilder buffer = new StringBuilder( "<?xml" );
            String version = xmlDec.getVersion();
            if (version != null) buffer.append( " version=\"" + version + "\"");
            // Podríamos poner "1.0" en caso null pero vamos a ser respetuosos con el original
            String encoding = xmlDec.getEncoding();
            if (encoding != null)
                buffer.append(" encoding=\"" + encoding + "\"");
            String standalone = xmlDec.getStandalone();
            if (standalone != null)
                buffer.append( " standalone=\"" + standalone + "\"");
            buffer.append( "?>\n" );
            buffer.append(markup);

            markup = buffer.toString();
        }
        return markup;
    }

    protected abstract void serializeDocument(Document doc,Writer out);
    public abstract void serializeNode(Node node,Writer out);
}
