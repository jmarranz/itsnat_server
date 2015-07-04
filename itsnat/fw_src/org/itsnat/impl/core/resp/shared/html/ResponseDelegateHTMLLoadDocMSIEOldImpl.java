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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.doc.web.ItsNatHTMLDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLHeadElement;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateHTMLLoadDocMSIEOldImpl extends ResponseDelegateHTMLLoadDocImpl
{
    public ResponseDelegateHTMLLoadDocMSIEOldImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public static ResponseDelegateHTMLLoadDocMSIEOldImpl createResponseDelegateHTMLLoadDocMSIEOld(BrowserMSIEOld browser,ResponseLoadStfulDocumentValid responseParent)
    {
        return new ResponseDelegateHTMLLoadDocMSIEOldImpl(responseParent);        
    }

    protected LinkedList<Element> fixOtherNSElementsInHTMLFindRootElems()
    {
        return null; // Nada que hacer, sólo W3C
    }

    protected LinkedList<Attr> fixOtherNSElementsInHTMLSaveValidNames(LinkedList<Element> otherNSRootElemsInHTML)
    {
        return null; // Nada que hacer, sólo W3C
    }

    protected void fixOtherNSElementsInHTMLCleanAuxAttribs(LinkedList<Attr> attribs)
    {
        // Nada que hacer, sólo W3C
    }

    protected void fixOtherNSElementsInHTMLGenCode(LinkedList<Element> otherNSElemsInHTML)
    {
        // Nada que hacer, sólo W3C
    }
    public String getJavaScriptDocumentName()
    {
        return "MSIEOldHTMLDocument";
    }
    
    @Override
    public String serializeDocument()
    {
        ItsNatHTMLDocumentImpl itsNatDoc = getItsNatHTMLDocument();
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Map<String,Attr> attrNamespaces = processTreeNamespaces(doc);

        String docMarkup = super.serializeDocument();

        restoreHTMLElemAttrNamespaces(doc,attrNamespaces);

        // Al serializar siempre se genera un finalizador </embed> para los <embed>
        // En MSIE 6,7,8 </embed> es convertido en un elemento desconocido.
        // http://msdn.microsoft.com/en-us/library/ms535245%28VS.85%29.aspx#

        // docMarkup = docMarkup.replaceAll("></embed>","/>");

        return docMarkup;
    }

    protected Map<String,Attr> processTreeNamespaces(HTMLDocument doc)
    {
        // MSIE 6 y 7 tienen un soporte muy pobre de namespaces pero algo hay
        // En tiempo de carga para que el namespace de un elemento como <svg:svg>...</svg:svg>
        // sea reconocido, el prefijo "svg" debe estar declarado como namespace en <html> y sólo en este
        // elemento.
        // Lo que hacemos es recorrer todos los elementos del documento
        // que tengan declaraciones tipo xmlns:prefijo="..." y compiar dicha
        // declaración a <html>, de esta manera conseguimos que plugins como el Adobe SVG Viewer en su modo "SVG inline"
        // o en general "Behaviors" asociados namespaces puedan funcionar con elementos tipo <svg:svg> ....</svg:svg>
        // devolviendo en la propiedad tagUrn el namespace y en scopeName el prefijo.
        // Esto no funcionará obviamente en casos peculiares de namespaces anidados
        // o de uso del mismo prefijo para diferentes URIs en diferentes lugares, pero los casos habituales funcionarán.
        // No nos preocupan los namespaces de los nodos cacheados pues estos
        // no son activos en el servidor.

        Map<String,Attr> attrNamespaces = null;

        // Copiamos en la colección attrNamespaces las declaraciones que ya existan
        // de namespaces tipo xmlns:prefijo para restaurar como estaba al final.
        Element html = doc.getDocumentElement();
        if (html.hasAttributes())
        {
            NamedNodeMap attribs = html.getAttributes();
            for(int i = 0; i < attribs.getLength(); i++)
            {
                Attr attr = (Attr)attribs.item(i);
                String prefix = attr.getPrefix();
                if ((prefix != null) && prefix.equals("xmlns"))
                {
                    if (attrNamespaces == null) attrNamespaces = new HashMap<String,Attr>();
                    attrNamespaces.put(attr.getName(),attr);
                }
            }
        }
        HTMLHeadElement head = DOMUtilHTML.getHTMLHead(doc);
        processTreeNamespaces(head); // head puede ser null
        HTMLBodyElement body = (HTMLBodyElement)doc.getBody();
        processTreeNamespaces(body);
        return attrNamespaces;
    }

    protected void processTreeNamespaces(Node node)
    {
        if (node == null) return;
        if (node.getNodeType() != Node.ELEMENT_NODE) return;

        Element elem = (Element)node;
        processNamespacesOfElement(elem);

        if (elem.hasChildNodes())
        {
            Node child = elem.getFirstChild();
            while(child != null)
            {
                processTreeNamespaces(child);

                child = child.getNextSibling();
            }
        }
    }

    protected void processNamespacesOfElement(Element elem)
    {
        if (elem.hasAttributes())
        {
            NamedNodeMap attribs = elem.getAttributes();
            for(int i = 0; i < attribs.getLength(); i++)
            {
                Attr attr = (Attr)attribs.item(i);
                String prefix = attr.getPrefix();
                if ((prefix != null) && prefix.equals("xmlns"))
                {
                    // No consideramos el caso de xmlns:itsnat="itsnat namespace"
                    // porque nunca enviaremos elementos al cliente tal y como
                    // <itsnat:nombre>... pues estos se procesan y se substituyen en el servidor.
                    // Evitando este caso, evitamos poner en <html> la declaración xmlns:itsnat que no
                    // ha hecho el programador y que puede confundirle.
                    if (NamespaceUtil.isItsNatNamespace(attr.getValue()))
                        continue;
                    Element html = elem.getOwnerDocument().getDocumentElement();
                    html.setAttributeNS(NamespaceUtil.XMLNS_NAMESPACE,attr.getName(),attr.getValue());
                }
            }
        }
    }

    protected static void restoreHTMLElemAttrNamespaces(Document doc,Map<String,Attr> attrNamespaces)
    {
        // Restauramos el estado original del DOM
        // attrNamespaces puede ser null
        Element html = doc.getDocumentElement();

        if (attrNamespaces != null)
        {
            for(Map.Entry<String,Attr> entry : attrNamespaces.entrySet())
            {
                String name = entry.getKey(); // xmlns:algo
                Attr value = entry.getValue();
                html.setAttributeNS(NamespaceUtil.XMLNS_NAMESPACE,name,value.getValue());
            }
        }

        // Ahora eliminamos aquellos atributos que no están en attrNamespaces
        if (html.hasAttributes())
        {
            NamedNodeMap attribs = html.getAttributes();
            // ¡¡¡Cuidado la longitud de la colección cambia !!!
            for(int i = 0; i < attribs.getLength(); )
            {
                Attr attr = (Attr)attribs.item(i);
                String prefix = attr.getPrefix();
                if ((prefix != null) && prefix.equals("xmlns"))
                {
                    // Si attrNamespaces es nulo es que obviamente este atributo no estaba en el elemento original
                    if ((attrNamespaces == null) || !attrNamespaces.containsKey(attr.getName()))
                        html.removeAttributeNode(attr);
                    else i++;
                }
                else i++;
            }
        }

    }
}
