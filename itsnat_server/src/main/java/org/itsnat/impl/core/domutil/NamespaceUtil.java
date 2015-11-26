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

import org.itsnat.core.ItsNatException;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

/**
 *
 * @author jmarranz
 */
public class NamespaceUtil
{
    // Namespaces
    public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace"; // Sin "/" al final
    public static final String XMLNS_NAMESPACE = "http://www.w3.org/2000/xmlns/";
    public static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml";
    public static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    public static final String XUL_NAMESPACE = "http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul";
    public static final String ITSNAT_NAMESPACE = "http://itsnat.org/itsnat";
    public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
            
    // MIMES
    public static final String MIME_HTML = "text/html";
    public static final String MIME_XHTML = "application/xhtml+xml";
    public static final String MIME_SVG = "image/svg+xml";    
    public static final String MIME_XUL = "application/vnd.mozilla.xul+xml";      
    public static final String MIME_XML = "text/xml";    
    public static final String MIME_ANDROID_LAYOUT = "android/layout";
    public static final String MIME_JAVASCRIPT = "text/javascript";   
    public static final String MIME_BEANSHELL = "text/beanshell";   // Inventado obviamente  
    
    
    public static final int XML   = 0;
    public static final int HTML  = 1;
    public static final int XHTML = 2;
    public static final int SVG   = 3;
    public static final int XUL   = 4;
    public static final int ANDROID_LAYOUT = 5;
    public static final int ANDROID_DRAWABLE = 6;
    
    public static boolean isXMLNamespace(String namespaceURI)
    {
        // Es el namespace implícito de atributos tipo: xml:lang etc
        return XML_NAMESPACE.equals(namespaceURI); // Puede ser null
    }

    public static boolean isXMLNSNamespace(String namespaceURI)
    {
        // Es el namespace implícito de atributos tipo: xmlns="...." o xmlns:prefix="..."
        return XMLNS_NAMESPACE.equals(namespaceURI); // Puede ser null
    }

    public static boolean isXMLNSDecAttribute(Attr attr)
    {
        String namespaceURI = attr.getNamespaceURI();
        return isXMLNSNamespace(namespaceURI) &&
               attr.getName().startsWith("xmlns"); // Yo creo que esta comprobación sobra pues sólo las declaraciones xmlns= o xmlns:prefix= tienen XMLNS namespace, otro tipo de uso yo creo que sería ilegal
    }

    public static boolean isXHTMLNamespace(String namespaceURI)
    {
        // Por ahora sólo está soportado:
        // http://www.w3.org/1999/xhtml

        // http://en.wikipedia.org/wiki/XHTML#XML_namespaces_and_schemas
        // http://www.w3.org/TR/xhtml2/conformance.html#strict
        return XHTML_NAMESPACE.equals(namespaceURI); // Puede ser null
    }

    public static boolean isSVGNode(Node node)
    {
        if (node instanceof Element)
            return isSVGElement((Element)node);
        else if (node instanceof CharacterData) // Sólo los atributos y elementos tienen namespace propio. Los comentarios, nodos de texto y CDATASection dependerán del nodo padre contenedor
            return isSVGNode(node.getParentNode());
        else  if (node instanceof ProcessingInstruction) // RARISIMO
            return isSVGNode(node.getParentNode());
        else  if (node instanceof Document) // No contemplado (podríamos chequear si el documentElement es SVG node)
            throw new ItsNatException("INTERNAL ERROR");
        else  if (node instanceof Attr) // No contemplado
            throw new ItsNatException("INTERNAL ERROR");
        else
            return false; // No consideramos DocumentFragment, EntityReference, Entity, DocumentType, Notation (views obviamente es false)
    }

    public static boolean isSVGElement(Element elem)
    {
        return isSVGNamespace(elem.getNamespaceURI());
    }

    public static boolean isSVGNamespace(String namespace)
    {
        return SVG_NAMESPACE.equals(namespace); // Puede ser null
    }

    public static Element getSVGRootElement(Node node)
    {
        // Suponemos que node está en el documento.
        if (!isSVGNode(node))
            return null; // No es SVG

        Node parentNode = node.getParentNode();
        if (parentNode == null) throw new ItsNatException("INTERNAL ERROR"); // El elemento HA de estar en el documento
        if (parentNode instanceof Element)
        {
            Element rootElem = getSVGRootElement((Element)parentNode);
            if (rootElem != null)
                return rootElem;
            // El padre no es SVG (no es el root por tanto)
        }
        // El padre no es SVG o no es un elemento (puede ser el documento)
        if (node instanceof Element)
            return (Element)node; // Sí mismo es el root
        else
            return null; // Caso raro de nodo SVG no elemento, debajo de un nodo no SVG.
     }

    public static boolean isSVGRootElement(Element elem)
    {
        Element rootElem = getSVGRootElement(elem);
        return (elem == rootElem); // Si es false es que no es SVG o no es raíz (hay otros SVG por encima)
    }

    public static boolean isXULNode(Node node)
    {
        if (node instanceof Element)
            return isXULElement((Element)node);
        else if (node instanceof CharacterData) // Sólo los atributos y elementos tienen namespace propio. Los comentarios, nodos de texto y CDATASection dependerán del nodo padre contenedor
            return isXULNode(node.getParentNode());
        else
            throw new ItsNatException("INTERNAL ERROR"); // No consideramos más casos
    }

    public static boolean isXULElement(Element elem)
    {
        return isXULNamespace(elem.getNamespaceURI());
    }

    public static boolean isXULNamespace(String namespace)
    {
        return XUL_NAMESPACE.equals(namespace); // Puede ser null
    }

    public static boolean isItsNatNamespace(String namespace)
    {
        return ITSNAT_NAMESPACE.equals(namespace); // Puede ser null
    }

    public static boolean isAndroidNamespace(String namespace)
    {
        return ANDROID_NAMESPACE.equals(namespace); // Puede ser null
    }
        
    public static boolean isStatefulMime(String mime)
    {
        return isHTMLorXHTMLMime(mime) || isOtherNSMime(mime) || isAndroidLayoutMime(mime);
    }

    public static boolean isHTMLorXHTMLMime(String mime)
    {
        // http://www.w3.org/TR/xhtml-media-types/
        // http://www.xml.com/pub/a/2003/03/19/dive-into-xml.html
        return (isHTMLMime(mime) ||
                isXHTMLMime(mime));
    }

    public static boolean isOtherNSMime(String mime)
    {
        return isSVGMime(mime) || isXULMime(mime);
    }

    public static boolean isXHTMLMime(String mime)
    {
        return mime.equals(MIME_XHTML);
    }

    public static boolean isHTMLMime(String mime)
    {
        return mime.equals(MIME_HTML);
    }

    public static boolean isSVGMime(String mime)
    {
        return mime.equals(MIME_SVG);
    }

    public static boolean isXULMime(String mime)
    {
        // "text/xul" y "text/x-xul" son ignorados por FireFox como XUL remoto
        return mime.equals(MIME_XUL);
    }

    public static boolean isAndroidLayoutMime(String mime)
    {
        return mime.equals(MIME_ANDROID_LAYOUT); 
    }    
    
    public static boolean isMIME_XHTML(int namespaceOfMIME)
    {
         return namespaceOfMIME == NamespaceUtil.XHTML;
    }

    public static boolean isMIME_HTML(int namespaceOfMIME)
    {
         return namespaceOfMIME == NamespaceUtil.HTML;
    }

    public static boolean isMIME_HTML_or_XHTML(int namespaceOfMIME)
    {
         return isMIME_HTML(namespaceOfMIME) || isMIME_XHTML(namespaceOfMIME);
    }

    public static boolean isMIME_OTHERNS(int namespaceOfMIME)
    {
         return isMIME_SVG(namespaceOfMIME) || isMIME_XUL(namespaceOfMIME);
    }

    public static boolean isMIME_SVG(int namespaceOfMIME)
    {
         return namespaceOfMIME == NamespaceUtil.SVG;
    }

    public static boolean isMIME_XUL(int namespaceOfMIME)
    {
         return namespaceOfMIME == NamespaceUtil.XUL;
    }

    public static boolean isMIME_XML(int namespaceOfMIME)
    {
         return namespaceOfMIME == NamespaceUtil.XML;
    }
    
    public static boolean isMIME_ANDROID(int namespaceOfMIME)
    {
        switch(namespaceOfMIME)
        {
            case NamespaceUtil.ANDROID_LAYOUT: return true;
            case NamespaceUtil.ANDROID_DRAWABLE: return true;
            default: return false;
        }
    }        
    
    public static int getNamespaceCode(String mime)
    {
        if (isHTMLorXHTMLMime(mime))
        {
            // El que decide si es HTML o XHTML no es el template sino el MIME suministrado
            // Como el mime no cambia no hay problema
            if (isHTMLMime(mime)) return HTML;
            else return XHTML;
        }
        else
        {
            if (isSVGMime(mime)) return SVG;
            else if (isXULMime(mime)) return XUL;
            else if (isAndroidLayoutMime(mime)) return ANDROID_LAYOUT; 
            else return XML;
        }
    }

    public static String getNamespace(int namespaceCode)
    {
        switch(namespaceCode)
        {
            case HTML:  return XHTML_NAMESPACE;  // No es muy exacto pero es conveniente pues lo habitual es usar XHTML en documentos cuyo MIME es HTML (es el mime el que decide el tipo del documento, es decir el valor del código entero namespace)
            case XHTML: return XHTML_NAMESPACE;
            case SVG:   return SVG_NAMESPACE;
            case XUL:   return XUL_NAMESPACE;
            case ANDROID_LAYOUT: 
            case ANDROID_DRAWABLE: 
                return ANDROID_NAMESPACE;                
        }
        return null;
    }
}
