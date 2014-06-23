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

package org.itsnat.impl.core.dompath;

import java.io.Serializable;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.droid.BrowserDroid;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 Los paths no considerarán los nodos de texto que son "conflictivos", los filtramos
 otros nodos si cuentan tal y como los comentarios, DocumentType etc.

 MSIE filtra los nodos con espacios y finales de línea salvo que
 los introduzcamos explícitamente via DOM con createTextNode/appendChild etc
 por eso sólo consideramos los elementos.
 http://www.w3schools.com/dom/dom_mozilla_vs_ie.asp
 http://www.w3schools.com/dom/dom_mozilla_vs_ie.asp
 *
 Por otra parte así evitamos el problema de que al eliminar un elemento queden los
 nodos anterior y posterior juntos, este es un problema para el control remoto
 pues cuando Firefox lee el DOM serializado a observar normaliza el árbol.
 De todas formas con un par de trucos conseguimos también contruir y resolver paths para
 nodos de texto.
 *
 * @author jmarranz
 */
public abstract class DOMPathResolver implements Serializable
{
    protected ClientDocumentStfulDelegateImpl clientDoc;

    /**
     * Creates a new instance of DOMPathResolver
     */
    public DOMPathResolver(ClientDocumentStfulDelegateImpl clientDoc)
    {
        this.clientDoc = clientDoc;
    }

    public static DOMPathResolver createDOMPathResolver(ClientDocumentStfulDelegateImpl clientDoc)
    {
        Browser browser = clientDoc.getClientDocumentStful().getBrowser();

        if (browser instanceof BrowserWeb) // NO debe ser nulo
            return DOMPathResolverWeb.createDOMPathResolverWeb((ClientDocumentStfulDelegateWebImpl)clientDoc);
        else if (browser instanceof BrowserDroid)
            return new DOMPathResolverDroid((ClientDocumentStfulDelegateDroidImpl)clientDoc);
        return null;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return clientDoc.getItsNatStfulDocument();
    }

    public abstract boolean isFilteredInClient(Node node); // Devuelve true si el navegador no refleja dicho nodo en el árbol DOM

    private static String[] getArrayPathFromString(String pathStr)
    {
        if (pathStr == null) return null;

        String[] path = pathStr.split(",");
        return path;
    }
  
    protected abstract Node getChildNodeFromPos(Node parentNode,int pos,boolean isTextNode);    


    protected abstract Node getChildNodeFromStrPos(Node parentNode,String posStr);    


    private Node getNodeFromArrayPath(String[] arrayPath,Node topParent)
    {
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();

        if (arrayPath.length == 1)
        {
            String firstPos = arrayPath[0];
            if (firstPos.equals("window"))
                return (Node)view;
            else if (firstPos.equals("document"))
                return doc;
            else if (firstPos.equals("doctype"))
                return doc.getDoctype();
        }

        if (topParent == null) topParent = doc;

        Node node = topParent;

        for(int i = 0; i < arrayPath.length; i++)
        {
            String posStr = arrayPath[i];
            node = getChildNodeFromStrPos(node,posStr);
            if (node == null) return null; // No seguimos buscando porque provocaremos un error, puede ser el caso de que el subárbol esté cacheado y los nodos existen en el cliente pero no en el servidor
        }
        return node;
    }

    public Node getNodeFromPath(String pathStr,Node topParent)
    {
        String[] path = getArrayPathFromString(pathStr);
        if (path == null) return null;

        return getNodeFromArrayPath(path,topParent);
    }

    protected String getNodeChildPosition(Node node)
    {
        // Evitamos toda la problemática de los comentarios bajo el documento que son filtrados
        // por unos navegadores y otros no, no usando la posición numérica del nodo root, pues
        // en dicha posición influyen los comentarios. Así podemos soportar (parcialmente, no AJAX) incluso navegadores antiguos
        // Ya no se necesita por tanto document.childNodes salvo que accedamos a comentarios bajo document
        // pero eso es rarísimo y a día de hoy posiblemente no funcionaría en algún navegador.
        Document doc = getItsNatStfulDocument().getDocument();
        if (node == doc.getDocumentElement())
            return "de";

        Node parentNode = node.getParentNode();
        if (parentNode == null)
            throw new ItsNatException("Unexpected error");

        int pos = 0; // la posición del nodo o la del nodo de texto pero ignorando los nodos de texto anteriores
        Node currNode = parentNode.getFirstChild();
        while(currNode != null)
        {
            if (!isFilteredInClient(currNode))
            {
                if (currNode == node)
                    return Integer.toString(pos);  // Así admitimos que currNode pueda ser un Text Node
                if (currNode.getNodeType() != Node.TEXT_NODE)
                    pos++; // Ignoramos los nodos de texto excepto el dado como argumento (si es un Text Node)                
            }

            currNode = currNode.getNextSibling();
        }

        throw new ItsNatException("Node not found in document to calculate paths",node);
        //return "-1";
    }

    private static String getStringPathFromArray(String[] path)
    {
        StringBuilder code = new StringBuilder();
        for(int i = 0; i < path.length; i++)
        {
            if (i != 0)
                code.append( "," );
            code.append( path[i] );
        }
        return code.toString();
    }

    private static int getNodeDeep(Node node,Node topParent)
    {
        // Cuenta cuantos padres tiene hasta el padre dado incluido éste
        int i = 0;
        while(node != topParent)
        {
            i++;
            node = node.getParentNode();
            if (node == null) return -1; // El nodo no está bajo topParent
        }
        return i;
    }

    private String[] getNodePathArray(Node nodeLeaf,Node topParent)
    {
        // Si topParent es null devuelve un path absoluto, es decir hasta el documento como padre
        if (nodeLeaf == null) return null;

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        Document doc = itsNatDoc.getDocument();
        if (topParent == null) topParent = doc;

        if (nodeLeaf.equals(((DocumentView)doc).getDefaultView()))
            return new String[]{"window"};
        else if (nodeLeaf.equals(doc))
            return new String[]{"document"};
        else if (nodeLeaf.equals(doc.getDoctype()))
            return new String[]{"doctype"};

        if (nodeLeaf.getNodeType() == Node.ELEMENT_NODE)
        {
            Element elem = (Element)nodeLeaf;
            String locbyid = elem.getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,"locById"); // Especialmente útil en stateless para evitar resolver los nodos padres via tree paths
            if ("true".equals(locbyid))
            {
                String id = elem.getAttribute("id"); // Si no existe devuelve cadena vacía
                if (!id.equals(""))
                    return new String[]{"eid:" + id};  // eid = element id
            }
        }

        Node node = nodeLeaf;
        if (node.getNodeType() == Node.ATTRIBUTE_NODE)
            node = ((Attr)node).getOwnerElement();

        int len = getNodeDeep(node,topParent);
        if (len < 0) return null;
        String[] path = new String[len];
        for(int i = len - 1; i >= 0; i--)
        {
            String pos = getNodeChildPosition(node);
            path[i] = pos;
            node = node.getParentNode();
        }

        path[len - 1] += getSuffix(nodeLeaf);

        return path;
    }

    private static String getSuffix(Node nodeLeaf)
    {
        int type = nodeLeaf.getNodeType();
        if (type == Node.TEXT_NODE)
            return getTextNodeSuffix();
        else if (type == Node.ATTRIBUTE_NODE)
            return "[@" + ((Attr)nodeLeaf).getName() + "]"; // La @ sobra pero es para seguir la sintaxis de XPath
        else
            return ""; // No tiene sufijo
    }

    public static String getTextNodeSuffix()
    {
        return "[t]";
    }

    public String getStringPathFromNode(Node node)
    {
        return getStringPathFromNode(node,null);
    }

    public String getStringPathFromNode(Node node,Node topParent)
    {
        if (node == null) return null;

        String[] path = getNodePathArray(node,topParent);
        if (path == null) return null;
        return getStringPathFromArray(path);
    }

    public String getRelativeStringPathFromNodeParent(Node child)
    {
        // Posición relativa respecto al padre
        if (child == null) return null;

        return getStringPathFromNode(child,child.getParentNode());
    }

    public static String removeTextNodeSuffix(String path)
    {
        int len = path.length();
        if (path.charAt(len - 1) != ']')
            return path; // No tiene sufijo
        path = path.substring(0,len - getTextNodeSuffix().length());
        return path;
    }

    public Node getPreviousSiblingInClientDOM(Node node)
    {
        // Devuelve nodos que existirán en el DOM del cliente
        Node prevSibling = node;
        do
        {
            prevSibling = prevSibling.getPreviousSibling();
        }
        while((prevSibling != null) && isFilteredInClient(prevSibling));
        return prevSibling;
    }

    public Node getNextSiblingInClientDOM(Node node)
    {
        Node nextSibling = node;
        do
        {
            nextSibling = nextSibling.getNextSibling();
        }
        while((nextSibling != null) && isFilteredInClient(nextSibling));
        return nextSibling;
    }
}
