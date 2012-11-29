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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.markup.render.DOMRenderImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableElement;

/**
 *
 * @author jmarranz
 */
public abstract class StfulTemplateVersionDelegateImpl extends MarkupTemplateVersionDelegateImpl
{
    public static final Set HTML_ELEMS_NOT_USE_CHILD_TEXT = new HashSet();
    public static final Set HTML_ELEMS_NOT_VALID_CHILD_COMMENT = new HashSet();

    static
    {
        // Elementos que pueden tener child nodes tipo Text pero que son inútiles
        // funcional y visualmente, es decir como mucho pueden tener espacios/fines de línea.
        // De esta manera aceleramos y reducimos el consumo de memoria
        // En algún caso es además imprescindible por ejemplo los nodos
        // bajo <tr> debido a que cuando
        // se añaden via JavaScript en FireFox (creo que v2) existe un bug en el que
        // indenta visualmente este nodo de texto como si fuera un <td>
        // (aunque no aparezca en el DOM como td)

        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("dl");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("frameset");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("head");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("html");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("map");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("menu");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("ol");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("optgroup");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("select");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("table");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("tbody");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("tfoot");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("thead");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("tr");
        HTML_ELEMS_NOT_USE_CHILD_TEXT.add("ul");

        // Elementos que NO pueden tener comentarios como hijo:
        // Los comentarios bajo <html> son llevados bajo <head> en Firefox 3
        // Los comentarios bajo DL, OL o UL son problemáticos en IE 6 pues
        // los mete en el elemento hijo más cercano, por ejemplo un comentario
        // bajo OL lo mete en el LI más cercano.
        // Los comentarios bajo SELECT son filtrados en IE 6, no merece la pena
        // una posible reinserción pues como estos problemas ocurren en IE 6 y este navegador ha sido (y es) muy popular
        // es altísimamente improbable que una página web necesite dichos comentarios
        // para algo en el cliente si se quiere que funcione en el IE 6.
        
        // Los comentarios bajo elementos tal y como TABLE, TBODY, TFOOT, THEAD y TR
        // no dan problemas y no son filtrados en IE 6 pero he descubierto que su presencia
        // en la construcción de la table con JavaScript (por ejemplo en carga de trees basados en tables con fastLoad = false)
        // pueden ser problemática (provoca errores raros), aparte de impedir
        // el uso de innerHTML (el innerHTML filtra los comentarios), en general el TABLE en MSIE 6 es muy delicado 
        // y esos comentarios no aportan nada.

        // Hay navegadores tal y como BlackBerryOld y S60WebKit FP 1 que filtran todos
        // los comentarios en tiempo de carga, este problema es abordado específicamente
        // reinsertando dichos comentarios.
        // FRAMESET y OPTGROUP no han sido testeados pero por si acaso los incluimos.

        HTML_ELEMS_NOT_VALID_CHILD_COMMENT.add("frameset");
        HTML_ELEMS_NOT_VALID_CHILD_COMMENT.add("html");
        HTML_ELEMS_NOT_VALID_CHILD_COMMENT.add("dl");
        HTML_ELEMS_NOT_VALID_CHILD_COMMENT.add("ol");
        HTML_ELEMS_NOT_VALID_CHILD_COMMENT.add("optgroup");
        HTML_ELEMS_NOT_VALID_CHILD_COMMENT.add("select");
        HTML_ELEMS_NOT_VALID_CHILD_COMMENT.add("table");
        HTML_ELEMS_NOT_VALID_CHILD_COMMENT.add("td");
        HTML_ELEMS_NOT_VALID_CHILD_COMMENT.add("tbody");
        HTML_ELEMS_NOT_VALID_CHILD_COMMENT.add("ul");
    }

    /**
     * Creates a new instance of OtherNSTemplateVersionDelegateImpl
     */
    public StfulTemplateVersionDelegateImpl(MarkupTemplateVersionImpl parent)
    {
        super(parent);
    }

    public boolean declaredAsComponent(Element elem)
    {
        boolean decAsComp = super.declaredAsComponent(elem);
        if (decAsComp) return true;

        return ItsNatStfulDocComponentManagerImpl.declaredAsHTMLComponent(elem);
    }

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

        if (parent.getNodeType() != node.ELEMENT_NODE)
            return false;

        Element parentElem = (Element)parent;
        String localName = parentElem.getLocalName();
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

    public void normalizeDocument(Document doc)
    {
        super.normalizeDocument(doc);

        cleanDocumentChildren(doc);

        normalizeHTMLElements(doc);
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

    public static void normalizeHTMLElements(Document doc)
    {
        // Buscamos que el DOM del navegador sea idéntico al del servidor,
        // los navegadores a veces hacen modificaciones por su cuenta
        // En teoría este método se llama antes de que haya mutation listeners
        // asociados.

        // Firefox y MSIE añaden automáticamente TBODY cuando se inserta
        // un "<table><tr>..." serializado (no lo hace si es por DOM)
        // por eso lo añadimos nosotros porque sino los paths fallan
        LinkedList htmlTables = DOMUtilInternal.getChildElementListWithTagNameNS(doc,NamespaceUtil.XHTML_NAMESPACE,"table", true);
        if (htmlTables != null)
        {
            for(Iterator it = htmlTables.iterator(); it.hasNext(); )
            {
                HTMLTableElement table = (HTMLTableElement)it.next();
                boolean hasTBody = (ItsNatTreeWalker.getFirstChildElementWithTagNameNS(table,NamespaceUtil.XHTML_NAMESPACE,"tbody") != null);

                if (!hasTBody)
                {
                    // No tiene TBODY, añadimos (suponemos que tampoco hay un THEAD etc)
                    HTMLElement tbody = (HTMLElement)doc.createElementNS(NamespaceUtil.XHTML_NAMESPACE,"tbody");
                    // Soportamos la existencia de COLGROUP antes del primer TR, a partir del primer TR
                    // copiaremos todos los nodos que siguen al primer TR bajo TBODY
                    Node child = ItsNatTreeWalker.getFirstChildElementWithTagNameNS(table,NamespaceUtil.XHTML_NAMESPACE,"tr");
                    while(child != null)
                    {
                        Node next = child.getNextSibling();
                        tbody.appendChild(child); // lo quita también de table
                        child = next;
                    }
                    table.appendChild(tbody);
                }
            }
        }

        for(Iterator it = HTML_ELEMS_NOT_USE_CHILD_TEXT.iterator(); it.hasNext(); )
        {
            String localName = (String)it.next();
            removeUnusefulHTMLChildTextNodes(localName,doc);
        }

        for(Iterator it = HTML_ELEMS_NOT_VALID_CHILD_COMMENT.iterator(); it.hasNext(); )
        {
            String localName = (String)it.next();
            removeUnusefulHTMLChildComments(localName,doc);
        }
    }

    public static void removeUnusefulHTMLChildTextNodes(String localName,Document doc)
    {
        LinkedList elements = DOMUtilInternal.getChildElementListWithTagNameNS(doc,NamespaceUtil.XHTML_NAMESPACE,localName,true);
        if (elements != null)
        {
            for(Iterator it = elements.iterator(); it.hasNext(); )
            {
                Element elem = (Element)it.next();
                DOMUtilInternal.removeAllUnusefulChildTextNodes(elem);
            }
        }
    }

    public static void removeUnusefulHTMLChildComments(String localName,Document doc)
    {
        LinkedList elements = DOMUtilInternal.getChildElementListWithTagNameNS(doc,NamespaceUtil.XHTML_NAMESPACE,localName,true);
        if (elements != null)
        {
            for(Iterator it = elements.iterator(); it.hasNext(); )
            {
                Element elem = (Element)it.next();
                DOMUtilInternal.removeAllDirectChildComments(elem);
            }
        }
    }
}
