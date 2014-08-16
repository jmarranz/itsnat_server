/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.template.web;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.itsnat.impl.core.template.StfulTemplateVersionDelegateImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableElement;


/**
 *
 * @author jmarranz
 */
public abstract class StfulWebTemplateVersionDelegateImpl extends StfulTemplateVersionDelegateImpl
{
    public static final Set<String> HTML_ELEMS_NOT_USE_CHILD_TEXT = new HashSet<String>();
    public static final Set<String> HTML_ELEMS_NOT_VALID_CHILD_COMMENT = new HashSet<String>();

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

    public StfulWebTemplateVersionDelegateImpl(MarkupTemplateVersionImpl parent)
    {
        super(parent);
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
        LinkedList<Node> htmlTables = DOMUtilInternal.getChildElementListWithTagNameNS(doc,NamespaceUtil.XHTML_NAMESPACE,"table", true);
        if (htmlTables != null)
        {
            for(Iterator<Node> it = htmlTables.iterator(); it.hasNext(); )
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

        for(String localName : HTML_ELEMS_NOT_USE_CHILD_TEXT)
        {
            removeUnusefulHTMLChildTextNodes(localName,doc);
        }

        for(String localName : HTML_ELEMS_NOT_VALID_CHILD_COMMENT)
        {
            removeUnusefulHTMLChildComments(localName,doc);
        }
    }
    
    public static void removeUnusefulHTMLChildTextNodes(String localName,Document doc)
    {
        LinkedList<Node> elements = DOMUtilInternal.getChildElementListWithTagNameNS(doc,NamespaceUtil.XHTML_NAMESPACE,localName,true);
        if (elements != null)
        {
            for(Iterator<Node> it = elements.iterator(); it.hasNext(); )
            {
                Element elem = (Element)it.next();
                DOMUtilInternal.removeAllUnusefulChildTextNodes(elem);
            }
        }
    }

    public static void removeUnusefulHTMLChildComments(String localName,Document doc)
    {
        LinkedList<Node> elements = DOMUtilInternal.getChildElementListWithTagNameNS(doc,NamespaceUtil.XHTML_NAMESPACE,localName,true);
        if (elements != null)
        {
            for(Iterator<Node> it = elements.iterator(); it.hasNext(); )
            {
                Element elem = (Element)it.next();
                DOMUtilInternal.removeAllDirectChildComments(elem);
            }
        }
    }    
    
    @Override
    public void normalizeDocument(Document doc)
    {
        super.normalizeDocument(doc);

        normalizeHTMLElements(doc);
    }    
    
}
