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

package org.itsnat.impl.core.jsren.dom.node.html.msie;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.path.DOMPathResolverHTMLDocMSIEPocket;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;

/**
 * Estos métodos sirven tanto para insertar/remover/modificar textos
 * como comentarios en Pocket IE (aunque se ha dejado de usar para comentarios, ya
 * no se envían al cliente).
 * 
 * @author jmarranz
 */
public class JSRenderHTMLFilteredNodeMSIEPocketImpl
{

    /** Creates a new instance of JSRenderHTMLFilteredNodeMSIEPocketImpl */
    public JSRenderHTMLFilteredNodeMSIEPocketImpl()
    {
    }

    public static String filterTags(String text)
    {
        // Los métodos "filteredNode" en el cliente usan innerHTML aunque
        // sea con la misión de insertar un texto, pero si este texto contiene
        // < o > puede dar problemas pues PocketIE creerá que son nodos
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        return text;
    }
    
    public static String getModifiedFilteredNodeCode(CharacterData node,String jsNewValue,ClientDocumentStfulImpl clientDoc)
    {
        jsNewValue = filterTags(jsNewValue);

        Node parent = node.getParentNode();
        NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parent,true);

        DOMPathResolverHTMLDocMSIEPocket pathResolver = (DOMPathResolverHTMLDocMSIEPocket)clientDoc.getDOMPathResolver();
        Node prevNode = pathResolver.getPreviousSiblingInClientDOM(node); // puede ser null
        Node nextNode = pathResolver.getNextSiblingInClientDOM(node); // puede ser null

        NodeLocationImpl prevNodeLoc = clientDoc.getNodeLocationRelativeToParent(prevNode);
        NodeLocationImpl nextNodeLoc = clientDoc.getNodeLocationRelativeToParent(nextNode);
        // Tener en cuenta que prevNode y nextNode pueden ser nulos.
        return "itsNatDoc.setFilteredNode(" + parentLoc.toJSArray(true) + "," + prevNodeLoc.toJSArray(false) + "," + nextNodeLoc.toJSArray(false) + "," + jsNewValue + ");\n";
    }

    public static String getAppendFilteredNodeCode(String parentVarName,String jsCode)
    {
        jsCode = filterTags(jsCode);

        return parentVarName + " = itsNatDoc.appendFilteredNode(" + parentVarName + "," + jsCode + ");\n";
    }

    public static String getInsertFilteredNodeCode(CharacterData newNode,String jsCode,ClientDocumentStfulImpl clientDoc)
    {
        jsCode = filterTags(jsCode);
        
        Node parent = newNode.getParentNode();
        DOMPathResolverHTMLDocMSIEPocket pathResolver = (DOMPathResolverHTMLDocMSIEPocket)clientDoc.getDOMPathResolver();
        Node nextSibling = pathResolver.getNextSiblingInClientDOM(newNode);
        if (nextSibling != null)
        {
            // No podemos usar ClientDocumentStfulImpl.getRefNodeLocationInsertBefore porque el nodo de texto/comentario es invisible y no sirve para calcular la posición del nextSibling
            // precisamente como el nodo de texto/comentario es invisible en el DOM tanto en el cliente como en el servidor (se filtra)
            // podemos calcular la posición de nextSibling de la forma normal
            // como si no se hubiera insertado el nuevo elemento, pues en teoría
            // el nuevo elemento ha sido insertado en el árbol DOM servidor y no en el cliente
            // pero el nuevo elemento es filtrado en el servidor en el cálculo de paths
            NodeLocationImpl refNodeLoc = clientDoc.getNodeLocationRelativeToParent(nextSibling);
            NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parent,true);
            return "itsNatDoc.insertBeforeFilteredNode(" + parentLoc.toJSArray(true) + "," + refNodeLoc.toJSArray(true) + "," + jsCode + ");\n";
        }
        else
        {
            NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parent,true);
            return "itsNatDoc.appendFilteredNode2(" + parentLoc.toJSArray(true) + "," + jsCode + ");\n";
        }
    }
}
