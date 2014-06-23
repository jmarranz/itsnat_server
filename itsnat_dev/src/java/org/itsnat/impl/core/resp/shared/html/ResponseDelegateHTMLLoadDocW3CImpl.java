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

package org.itsnat.impl.core.resp.shared.html;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.itsnat.impl.core.browser.web.BrowserBlackBerryOld;
import org.itsnat.impl.core.browser.web.BrowserGecko;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.browser.web.opera.BrowserOpera;
import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.otherns.JSRenderOtherNSAttributeW3CImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.otherns.JSRenderOtherNSElementW3CImpl;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.itsnat.impl.res.core.js.LoadScriptImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegateHTMLLoadDocW3CImpl extends ResponseDelegateHTMLLoadDocImpl
{
    private final static String NSROOTATTRNAME = "itsnatnsroot";
    private final static String NSATTRPREFIX = "itsnatns_";


    public ResponseDelegateHTMLLoadDocW3CImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public static ResponseDelegateHTMLLoadDocW3CImpl createResponseDelegateHTMLLoadDocW3C(BrowserW3C browser,ResponseLoadStfulDocumentValid responseParent)
    {
        if (browser instanceof BrowserGecko)
            return ResponseDelegateHTMLLoadDocGeckoImpl.createResponseDelegateHTMLLoadDocGecko((BrowserGecko)browser,responseParent);
        else if (browser instanceof BrowserWebKit)
            return ResponseDelegateHTMLLoadDocWebKitImpl.createResponseDelegateLoadHTMLDocWebKit(responseParent);
        else if (browser instanceof BrowserOpera)
            return ResponseDelegateHTMLLoadDocOperaImpl.createResponseDelegateHTMLLoadDocOpera((BrowserOpera)browser, responseParent);
        else if (browser instanceof BrowserBlackBerryOld)
            return new ResponseDelegateHTMLLoadDocBlackBerryOld2Impl(responseParent);
        else
            return new ResponseDelegateHTMLLoadDocW3CDefaultImpl(responseParent);
    }

    protected LinkedList<Element> fixOtherNSElementsInHTMLFindRootElems()
    {
        // En tiempo de carga con MIME "text/html" los elementos inline no-X/HTML presentes
        // en el markup de carga son incorrectamente cargados en el DOM cliente en navegadores
        // con soporte de SVG nativo tal y como FireFox, Opera y algunos navegadores WebKit (Safari, Chrome y iPhone 2.1+ este último no probado)
        // Concretamente el namespaceURI no es el esperado por lo que son considerados elementos HTML desconocidos.
        // Esto no ocurre con MIME XHTML ("application/xhtml+xml")
        // La solución es reinsertar los elementos de nuevo via DOM correctamente,
        // esta solución no es obvia porque los elementos en el DOM no tienen el namespaceURI
        // definido por lo que hay que obtenerlo de los atributos de declaración de namespaces,
        // también existe el problema de los tagNames están erróneamente en mayúsculas
        // (creo que excepto Opera) y los atributos en minúsculas.
        // Por ello, como en el servidor sabemos exactamente como deben ser, creamos
        // atributos auxiliares para poder recrear el DOM correctamente
        // Existe la alternativa de tener un listado de todos los nombres de elementos y atributos de SVG
        // y MathML por ejemplo, tal y como se hace en:
        // http://intertwingly.net/blog/2006/12/05/HOWTO-Embed-MathML-and-SVG-into-HTML4
        // http://raphaeljs.com/
        // pero mi solución es mucho mejor pues no requiere ésto y admite namespaces
        // a priori desconocidos, es la ventaja de trabajar desde el servidor.

        // Obtenemos los elementos antes de serializar y de ejecutar los listeners de usuario
        // Antes de serializar porque en el caso de fastLoad = false, es lo primero que se hace
        // en el caso de fastLoad = true se ejecutan primero los listeners del usuario, dichos
        // listeners

        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (!clientDoc.isSendCodeEnabled())
            return null; // Hay que generar código, aunque no ahora, por lo que no vale la pena esto si no se puede enviar código

        BrowserWeb browser = (BrowserWeb)clientDoc.getBrowser();
        if (!browser.canNativelyRenderOtherNSInXHTMLDoc())
            return null; // Si no puede renderizar por ejemplo SVG no vale la pena esto

        if (!getItsNatHTMLDocument().isMIME_HTML())
            return null; // En MIME XHTML no hace falta esto

        Element rootElem = getItsNatHTMLDocument().getDocument().getDocumentElement();
        return getOtherNSRootElementsInline(rootElem);
        // Si es null es que no hay.
    }

    protected static boolean hasSomeCharUpcase(String value)
    {
        if (value == null) return false;
        for(int i = 0; i < value.length(); i++)
            if (Character.isUpperCase(value.charAt(i))) return true;
        return false;
    }

    protected LinkedList<Attr> fixOtherNSElementsInHTMLSaveValidNames(LinkedList<Element> otherNSRootElemsInHTML)
    {
        LinkedList<Attr> auxAttribs = new LinkedList<Attr>();
        for(Element elem : otherNSRootElemsInHTML)
        {
            // Marcamos el elemento root como elemento que ha de procesarse con el script
            // que enviaremos al cliente, para distinguir de los casos en los que por ejemplo
            // embebamos MathML dentro de SVG etc (raro pero posible).
            Attr attr = elem.getOwnerDocument().createAttribute(NSROOTATTRNAME);
            attr.setValue("true");
            elem.setAttributeNode(attr);
            auxAttribs.add(attr);

            fixOtherNSElementsInHTMLSaveValidNames(elem,auxAttribs);
        }
        return auxAttribs;
    }

    protected void fixOtherNSElementsInHTMLSaveValidNames(Element elem,LinkedList<Attr> auxAttribs)
    {
        // Primero tomamos una foto de los atributos originales pues sobre la marcha vamos a añadir
        // atributos auxiliares seguramente.

        if (JSRenderOtherNSAttributeW3CImpl.hasIgnoreNSAttrInMIMEHTML(elem)) return;

        Attr[] originalAttribs = null;
        if (elem.hasAttributes())
        {
            NamedNodeMap attribs = elem.getAttributes();
            int len = attribs.getLength();
            originalAttribs = new Attr[len];
            for(int i = 0; i < len; i++)
            {
                Attr attr = (Attr)attribs.item(i);
                originalAttribs[i] = attr;
            }
        }

        boolean elemWithOtherNS = JSRenderOtherNSElementW3CImpl.isElementWithOtherNSTagNameInMIMEHTML(elem);
        if (elemWithOtherNS)
            fixOtherNSElementsInHTMLSaveValidName(elem,elem.getPrefix(),elem.getLocalName(),auxAttribs);

        if (originalAttribs != null)
        {
            for(int i = 0; i < originalAttribs.length; i++)
            {
                Attr attr = originalAttribs[i];
                if (elemWithOtherNS || JSRenderOtherNSAttributeW3CImpl.isAttrWithOtherNSInMIMEHTML(attr))
                {
                    // Aunque el atributo no tenga namespace propio, si está dentro de un elemento con namespace (no XHTML) la distinción entre mayúsculas y minúsculas es importante (por ejemplo en SVG)
                    // hay que generar el atributo backup
                    String prefix = attr.getPrefix();
                    String localName;
                    if (prefix != null) localName = attr.getName().substring(prefix.length() + 1);
                    else localName = attr.getName();
                    fixOtherNSElementsInHTMLSaveValidName(elem,prefix,localName,auxAttribs);
                }
            }
        }


        if (elem.hasChildNodes())
        {
            Node node = elem.getFirstChild();
            while(node != null)
            {
                if (node instanceof Element) // Los demás casos: nodos de texto, comentarios etc
                    fixOtherNSElementsInHTMLSaveValidNames((Element)node,auxAttribs);

                node = node.getNextSibling();
            }
        }
    }

    protected void fixOtherNSElementsInHTMLSaveValidName(Element elem,String prefix,String localName,LinkedList<Attr> auxAttribs)
    {
        if (!hasSomeCharUpcase(prefix) && !hasSomeCharUpcase(localName)) return;

        // Necesita un atributo auxiliar "backup" con el nombre original
        String attrName,attrValue;
        if (prefix != null)
        {
            attrName = NSATTRPREFIX + prefix.toLowerCase() + "_" + localName.toLowerCase();
            attrValue = prefix + ":" + localName;
        }
        else
        {
            attrName = NSATTRPREFIX + localName.toLowerCase();
            attrValue = localName;
        }
        Attr attr = elem.getOwnerDocument().createAttribute(attrName);
        attr.setValue(attrValue);
        elem.setAttributeNode(attr);
        auxAttribs.add(attr);
    }

    protected void fixOtherNSElementsInHTMLCleanAuxAttribs(LinkedList<Attr> attribs)
    {
        if (attribs == null) return;
        for(Attr attr : attribs)
        {
            attr.getOwnerElement().removeAttributeNode(attr);
        }
    }

    protected void fixOtherNSElementsInHTMLGenCode(LinkedList<Element> otherNSElemsInHTML)
    {
        addScriptFileToLoad(LoadScriptImpl.ITSNAT_FIX_OTHERNS_IN_HTML);

        StringBuilder code = new StringBuilder();

        Set<String> tagNames = new HashSet<String>();
        for(Element elem : otherNSElemsInHTML)
        {
            tagNames.add(elem.getTagName().toUpperCase());
        }

        code.append("new ItsNatFixOtherNSInHTML().fixTreeOtherNSInHTML([");
        for(Iterator<String> it = tagNames.iterator(); it.hasNext(); )
        {
            String tagName = (String)it.next();
            code.append("\"" + tagName + "\"");
            if (it.hasNext()) code.append(",");
        }
        code.append("]);\n");

        code.append("obj = null;\n"); // Para liberar el objeto utilidad pues ya no lo necesitamos

        addFixDOMCodeToSend(code.toString());
    }


    protected LinkedList<Element> getOtherNSRootElementsInline(Node node)
    {
        return getOtherNSRootElementsInline(node,null);
    }

    protected LinkedList<Element> getOtherNSRootElementsInline(Node node,LinkedList<Element> list)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return list;

        Element elem = (Element)node;

        if (isSVGRootElementProcessedBySVGWeb(elem))
        {
            // En el caso de que sea un elemento a procesar por SVGWeb se envolverá en un <script type="image/svg+xml"> dentro del mismo el SVG será un nodo de texto por lo que no será "visible" por el JavaScript que corrige el DOM por lo que no tiene sentido este proceso
            // No procesamos ni el elemento ni sus hijos
            return list;
        }
        else if (JSRenderOtherNSElementW3CImpl.isElementWithSomethingOtherNSInMIMEHTML(elem))
        {
            if (list == null) list = new LinkedList<Element>();

            // Añadimos sólo el elemento padre pues el tratamiento
            // también se hará a los hijos de este padre pero via JavaScript.
            list.add(elem);
        }
        else
        {
            Node child = elem.getFirstChild();
            while (child != null)
            {
                list = getOtherNSRootElementsInline(child,list);
                child = child.getNextSibling();
            }
        }

        return list;
    }

    public String getJavaScriptDocumentName()
    {
        return "W3CHTMLDocument";
    }
}
