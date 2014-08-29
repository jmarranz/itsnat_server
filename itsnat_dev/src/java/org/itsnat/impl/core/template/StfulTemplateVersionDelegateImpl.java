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

package org.itsnat.impl.core.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.markup.render.DOMRenderImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *
 * @author jmarranz
 */
public abstract class StfulTemplateVersionDelegateImpl extends MarkupTemplateVersionDelegateImpl
{

    /**
     * Creates a new instance of OtherNSTemplateVersionDelegateImpl
     */
    public StfulTemplateVersionDelegateImpl(MarkupTemplateVersionImpl parent)
    {
        super(parent);
    }


    @Override
    public void serializeNode(Node node,Writer out,DOMRenderImpl nodeRender)
    {
        if (is_SCRIPT_or_STYLE_Text(node))
            serialize_SCRIPT_or_STYLE_Text((Text)node,out,nodeRender);
        else
            super.serializeNode(node,out,nodeRender);
    }

    public static boolean is_SCRIPT_or_STYLE_Text(Node node)
    {
        // No solo HTML, pueden ser de SVG o XUL
        if (node.getNodeType() != Node.TEXT_NODE)
            return false;

        Node parent = node.getParentNode();
        if (parent == null) return false;

        if (parent.getNodeType() != Node.ELEMENT_NODE)
            return false;

        Element parentElem = (Element)parent;
        String localName = DOMUtilInternal.getLocalName(parentElem); // No usamos Element.getLocalName() porque en Android es null (los elementos no tienen namespace)
        if (localName.equals("script"))
            return true;

        if (localName.equals("style"))
            return true;

        return false;
    }

    public void serialize_SCRIPT_or_STYLE_Text(Text content,Writer out,DOMRenderImpl nodeRender)
    {
        // Los elementos <script> y <style> son tratados
        // de forma especial tal que su contenido es SIEMPRE un único nodo Text hijo
        // aunque contengan comentarios <!-- --> o <![CDATA[ ]]>
        // Los comentarios y CDATA fuera de <script> y <style> son creados en DOM normalmente.
        // El problema es que al pasar al DOMRenderImpl
        // el nodo Text, el render no comprueba que es hijo de <script> o <style>
        // y el texto lo trata normalmente convirtiendo los < y > en &lt; y gt;
        // fastidiando en el cliente dichos elementos.
        // Sin embargo cuando se envía el propio <script> o <style> el render
        // sabe que tiene que tratar de forma especial el contenido de ambos respetando
        // los comentarios y los CDATA.
        // Por tanto serializamos el elemento padre también y luego quitamos los tag de inicio y final

        // Esto también es aplicable a SVG y XUL pues ambos pueden ser introducidos
        // como elementos HTML pero también con otros namespaces, por ejemplo
        // <script> existe nativamente en (XUL) o bien añadido via xlink namespace (SVG).
        // El <style> por otra parte forma parte de SVG

        Element parentElem = (Element)content.getParentNode();

        StringWriter strWriter = new StringWriter();
        super.serializeNode(parentElem,strWriter,nodeRender);
        String code = strWriter.toString();

        // Quitamos el tag de abrir y cerrar <tag>...</tag>
        int pos = code.indexOf('>');
        code = code.substring(pos + 1); // quitamos <tag>
        pos = code.lastIndexOf('<');
        code = code.substring(0,pos); // quitamos </tag>

        try
        {
            out.write(code);
        }
        catch(IOException ex)
        {
            throw new ItsNatException(ex);
        }
    }

    @Override
    public void normalizeDocument(Document doc)
    {
        super.normalizeDocument(doc);

        cleanDocumentChildren(doc);
    }

    protected static void cleanDocumentChildren(Document doc)
    {
        /* Eliminamos los nodos bajo el Document que no sean
         * el DocumentType, processing instructions (útil en XUL), el root (<html>) y nodos de texto, por ejemplo comentarios.
         * La razón NO es por un problema de cálculo de paths pues el cálculo
         * de paths empieza desde <html> (usando "de") y es rarísimo que queramos
         * acceder a los elementos de esa zona, tampoco el problema es los nodos
         * de texto (espacios y fines de línea) que por ejemplo son filtrados en FireFox,MSIE 6, Safari 3 y Opera 9
         * El problema son los comentarios, no tanto porque son filtrados por FireFox por ejemplo,
         * el problema es S40WebKit por ejemplo que NO lo filtra, lo inserta BAJO <head>
         * distorsionando el sistema de paths y FireFox bajo <html>.
         *
         * Por otra parte en Opera (en HTML, no XHTML o XML) y Safari 3
         * no incluyen en el document.childNodes el DocumentType por lo que
         * tenemos que añadirlo en JavaScript y con comentarios no sabríamos
         * cual es el orden de inserción respecto a los mismos.
         *
         * Esto es aplicable a SVG aunque S40WebKit no tenga soporte de SVG
         *
         * También lo hacemos para XML, en general suena raro tener comentarios
         *
         */

        Node node = doc.getFirstChild();
        while (node != null)
        {
            int type = node.getNodeType();
            if ((type != Node.ELEMENT_NODE) &&
                (type != Node.DOCUMENT_TYPE_NODE) &&
                (type != Node.TEXT_NODE) &&
                (type != Node.PROCESSING_INSTRUCTION_NODE) )
            {
                Node next = node.getNextSibling();
                doc.removeChild(node);
                node = next;
            }
            else
            {
                node = node.getNextSibling();
            }
        }
    }



}
