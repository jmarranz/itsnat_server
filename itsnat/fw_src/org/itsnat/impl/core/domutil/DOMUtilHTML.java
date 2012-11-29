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

package org.itsnat.impl.core.domutil;

import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLHtmlElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public class DOMUtilHTML
{
    public static HTMLHeadElement getHTMLHead(HTMLDocument doc)
    {
        HTMLHtmlElement html = (HTMLHtmlElement)doc.getDocumentElement();
        if (html == null) return null;
        Node head = html.getFirstChild();
        while ( (head != null) && !(head instanceof HTMLHeadElement) )
            head = head.getNextSibling();
        return (HTMLHeadElement)head; // Puede ser null
    }

    public static boolean isChildOfHTMLHead(Element elem)
    {
        Node parent = elem.getParentNode();
        if (parent == null) return false;
        Document doc = elem.getOwnerDocument();
        if (!(doc instanceof HTMLDocument)) return false; // No es ni siquiera un documento HTML
        HTMLDocument htmDoc = (HTMLDocument)doc;
        HTMLHeadElement head = getHTMLHead(htmDoc);
        while(parent != null)
        {
            if (parent == head)
                return true;

            parent = parent.getParentNode();
        }

        return false;
    }

    public static boolean isHTMLNode(Node node)
    {
        if (node == null) return false;

        if (node instanceof Element)
            return isHTMLElement((Element)node);
        else if (node instanceof CharacterData) // CDATASection, Comment y Text
            return isHTMLCharacterData((CharacterData)node);
        else  if (node instanceof ProcessingInstruction) // RARISIMO
            return isHTMLNode(node.getParentNode());
        else  if (node instanceof Document) // RARISIMO
            return isHTMLDocument((Document)node);
        else  if (node instanceof Attr) // RARISIMO
            return isHTMLAttribute(node.getNamespaceURI(),((Attr)node).getOwnerElement());

        // No consideramos DocumentFragment, EntityReference, Entity, DocumentType, Notation (views obviamente es false)
        return false;
    }

    public static boolean isHTMLElement(Element elem)
    {
        return elem instanceof HTMLElement;
    }

    public static boolean isHTMLCharacterData(CharacterData node)
    {
        return isHTMLNode(node.getParentNode());
    }

    public static boolean isHTMLDocument(Document doc)
    {
        return doc instanceof HTMLDocument;
    }

    public static boolean isHTMLAttribute(String attrNamespaceURI,Element elem)
    {
        // attrNamespaceURI es el del atributo, es normal que sea nulo.
        // elem es el elemento container

        // El namespace es habitual que sea nulo en atributos, pues en el caso de atributos el namespace
        // no se hereda ni del elemento contenedor ni del documento, lo normal para que no sea nulo
        // es que sea una declaración xmlns o bien sea un atributo con prefijo, es decir si no hay explícitamente
        // un prefijo no tiene namespace propio.

        // Ejemplo (suponemos el prefijo "html" definido más arriba como
        //      xmlns:html="http://www.w3.org/1999/xhtml")
        // <html:div prueba="valor" />
        //   En este caso el namespace del atributo prueba es null
        // <html:div html:prueba="valor" />
        //   En este caso namespace es el del prefijo "html" (http://...xhtml)
        //   y el nombre del atributo es "prueba" y se podrá obtener vía:
        //   elem.getAttributeNode("html:prueba") o bien vía
        //   elem.getAttributeNodeNS("http://www.w3.org/1999/xhtml","prueba");

        // El que un atributo no tenga namespace no significa que no se interprete
        // como atributo dentro de un namespace, por ejemplo, en XUL:
        // <html:div onclick="..." >CLICK</html:div>
        // El "onclick" es ejecutado como si fuera un atributo HTML a pesar de que su namespace es null.
        // ES MAS puesto como html:onclick (ahora con namespace XHTML) NO SE EJECUTA.
        // Siguiendo esta regla si hacemos (seguimos en XUL):
        // elem.setAttributeNS("http://www.w3.org/1999/xhtml","onclick","alert('MOUSEUP')");
        // AÑADE UN onclick QUE NO FUNCIONA
        // mejor como: elem.setAttribute("onclick","alert('MOUSEUP')");
        // (o setAttributeNS con null en el namespace)
        // Por lo tanto NO forzar el namespace usando el del elemento contenedor.

        // Nota: En FireFox 3.5 (supongo que anteriores también) el "View Selection Source"
        // no muestra el prefijo de los atributos creados vía JavaScript, verlo con
        // FireBug ("Inspect Element")

        // Sólo consideraremos atributos HTML o XHTML los que están
        // dentro de un elemento HTMLElement (sea o no un documento X/HTML) y namespace
        // nulo o XHTML, pues es extremadamente raro que un atributo con namespace XHTML
        // esté fuera de un HTMLElement (en general es raro que un atributo tenga namespace XHTML)

        return isHTMLElement(elem) &&
               ((attrNamespaceURI == null) || NamespaceUtil.isXHTMLNamespace(attrNamespaceURI));
    }

    private static boolean isHTMLInputType(Node node,String typeLowerCase)
    {
        if (node instanceof HTMLInputElement)
        {
            Element elem = (Element)node;
            String type = elem.getAttribute("type").toLowerCase();
            return type.equals(typeLowerCase);
        }
        else return false;
    }

    public static boolean isHTMLInputTextBased(Node node)
    {
        if (node instanceof HTMLInputElement)
        {
            Element elem = (Element)node;
            String type = elem.getAttribute("type").toLowerCase();
            return type.equals("text") || type.equals("password") || type.equals("file");
        }
        else return false;
    }

    public static boolean isHTMLInputCheckBox(Node node)
    {
        return isHTMLInputType(node,"checkbox");
    }

    public static boolean isHTMLInputImage(Node node)
    {
        return isHTMLInputType(node,"image");
    }

    public static boolean isHTMLInputFile(Node node)
    {
        return isHTMLInputType(node,"file");
    }

    public static boolean isHTMLInputFileWithValueAttr(Node node)
    {
        if (!isHTMLInputFile(node)) return false;

        Element elem = (Element)node;
        return elem.hasAttribute("value");
    }

    public static boolean isHTMLInputFileValueAttr(Node node,String attrName)
    {
        if (!isHTMLInputFile(node)) return false;

        return attrName.toLowerCase().equals("value");
    }

    public static boolean isHTMLInputCheckBoxOrRadio(Node node)
    {
        if (!(node instanceof HTMLInputElement)) return false;

        Element elem = (Element)node;
        String type = elem.getAttribute("type").toLowerCase();
        return type.equals("checkbox") || type.equals("radio");
    }

    public static boolean isHTMLTextAreaOrInputTextBox(Node node)
    {
        if (node instanceof HTMLTextAreaElement) return true;
        else if (node instanceof HTMLInputElement)
        {
            Element elem = (Element)node;
            String type = elem.getAttribute("type").toLowerCase();
            return type.equals("text") || type.equals("password") || type.equals("file");
        }
        else return false;
    }

    public static boolean isHTMLOptionOfSelectMultipleOrWithSize(Node option)
    {
        if (!(option instanceof HTMLOptionElement)) return false; // Por si fuera un nodo de texto

        Node parent = option.getParentNode();
        return isHTMLSelectMultipleOrWithSize(parent);
    }

    public static boolean isHTMLOptionOfSelectMultiple(Node option)
    {
        if (!(option instanceof HTMLOptionElement)) return false; // Por si fuera un nodo de texto

        Node parent = option.getParentNode();
        return isHTMLSelectMultiple(parent);
    }

    public static boolean isHTMLSelectMultipleOrWithSize(Node node)
    {
        if (!(node instanceof HTMLSelectElement)) return false;

        Element elem = (Element)node;
        return (elem.hasAttribute("multiple") || elem.hasAttribute("size"));
    }

    public static boolean isHTMLSelectComboBox(Node node)
    {
        if (!(node instanceof HTMLSelectElement)) return false;

        // Ni "multiple" ni "size"
        Element elem = (Element)node;
        return (!elem.hasAttribute("multiple") && !elem.hasAttribute("size"));
    }

    public static boolean isHTMLSelectMultiple(Node node)
    {
        if (!(node instanceof HTMLSelectElement)) return false;

        Element elem = (Element)node;
        return elem.hasAttribute("multiple");
    }

    public static boolean hasHTMLFocusOrBlurMethod(Node node)
    {
        if (!(node instanceof HTMLElement)) return false;

        return (node instanceof HTMLTextAreaElement) ||
               (node instanceof HTMLInputElement) ||
               (node instanceof HTMLSelectElement) ||
               (node instanceof HTMLAnchorElement);
    }
}
