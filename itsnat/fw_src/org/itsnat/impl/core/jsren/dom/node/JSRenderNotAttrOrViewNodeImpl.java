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

package org.itsnat.impl.core.jsren.dom.node;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.NodeCacheRegistryImpl;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderNotAttrOrViewNodeImpl extends JSRenderNodeImpl
{

    /** Creates a new instance of JSNoAttributeRender */
    public JSRenderNotAttrOrViewNodeImpl()
    {
    }

    protected abstract String createNodeCode(Node node,ClientDocumentStfulImpl clientDoc);

    public abstract Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulImpl clientDoc);

    protected String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulImpl clientDoc)
    {
        String newNodeCode = createNodeCode(newNode,clientDoc);
        return getAppendCompleteChildNode(parentVarName,newNode,newNodeCode,clientDoc);
    }

    protected String getAppendCompleteChildNode(String parentVarName,Node newNode,String newNodeCode,ClientDocumentStfulImpl clientDoc)
    {
        String idJS = cacheNewNodeIfNeededAndGenJSId(newNode,clientDoc);      
        return "itsNatDoc.appendChild2(" + parentVarName + "," + newNodeCode + "," + idJS + ");\n";
    }

    protected String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulImpl clientDoc)
    {
        String newNodeCode = createNodeCode(newNode,clientDoc);
        return getInsertCompleteNodeCode(newNode,newNodeCode,clientDoc);
    }

    public abstract Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulImpl clientDoc);

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulImpl clientDoc)
    {
        // Los nodos de texto pueden ser filtrados en el cliente si son nodos
        // con espacios y/o fines de línea únicamente, por lo que tenemos que
        // ser tolerantes no encontrarlo. No encontrarlo no es solamente
        // que devuelva null pues el buscador por path puede devolver el 
        // elemento adyacente.
        boolean isText = (removedNode.getNodeType() == Node.TEXT_NODE);
        String id = clientDoc.getCachedNodeId(removedNode);
        if (id != null)
        {
            // Está cacheado
            return "itsNatDoc.removeChild2(\"" + id + "\"," + isText + ");\n";
        }
        else
        {
            // No está cacheado, no nos interesa cachear el path de un nodo que vamos
            // a eliminar (aunque se añadiera de nuevo tendría un nuevo id),
            // sin embargo el padre sí nos interesa cachear porque habitualmente
            // se eliminan más nodos hijo, usamos el sistema de path relativo
            Node parent = removedNode.getParentNode();
            NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parent,true);
            String childRelPath = clientDoc.getRelativeStringPathFromNodeParent(removedNode);
            childRelPath = toLiteralStringJS(childRelPath);
            return "itsNatDoc.removeChild3(" + parentLoc.toJSNodeLocation(true) + "," + childRelPath + "," + isText + ");\n";
        }
    }

    protected String getInsertCompleteNodeCode(Node newNode,String newNodeCode,ClientDocumentStfulImpl clientDoc)
    {
        Node parent = newNode.getParentNode();

        // Obtenemos el sibling con representación en el DOM cliente (no filtrado)
        // Sólo hay filtrado de los comentarios en trozos de SVG gestionados por SVGWeb,
        // dichos comentarios no están en el DOM y afortunadamente no son visibles 
        // No consideramos el filtrado en servidor de los nodos de texto con espacios, que a veces son filtrados
        // en algunos navegadores (MSIE por ejemplo) pues ItsNat está preparado para ello si no se encuentra en el cliente.

        NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parent,true);
        String idJS = cacheNewNodeIfNeededAndGenJSId(newNode,clientDoc);
        
        Node nextSibling = clientDoc.getNextSiblingInClientDOM(newNode);
        if (nextSibling != null)
        {
            NodeLocationImpl refNodeLoc = clientDoc.getRefNodeLocationInsertBefore(newNode,nextSibling);
            return "itsNatDoc.insertBefore3(" + parentLoc.toJSNodeLocation(true) + "," + newNodeCode + "," + refNodeLoc.toJSNodeLocation(true) + "," + idJS + ");\n";
        }
        else
        {
            return "itsNatDoc.appendChild3(" + parentLoc.toJSNodeLocation(true) + "," + newNodeCode + "," + idJS + ");\n";
        }
    }

    public String getRemoveAllChildCode(Node parentNode,ClientDocumentStfulImpl clientDoc)
    {
        NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parentNode,true);
        return "itsNatDoc.removeAllChild2(" + parentLoc.toJSNodeLocation(true) + ");\n";
    }
    
    public String cacheNewNodeIfNeededAndGenJSId(Node newNode,ClientDocumentStfulImpl clientDoc)
    {
        // Cacheamos el  nuevo nodo (en servidor y en cliente obviamente) cuando es un nodo hijo directo
        // de <head> o <body> en el caso de HTML/XHTML o el elemento root en otros namespaces
        // para evitar problemas al acceder en el futuro al mismo con los elementos intrusivos que habitualmente
        // añaden los add-on de los navegadores o muchas librerías JavaScript al final del <head> o <body>
        // incluso al final de <svg> en el caso de FireBug
        // Si el nodo es cacheable (no es de texto) y no está bloqueada la caché (raro) no tendremos problemas
        // con estos nodos intrusivos.
        // Podríamos extender el cacheado a cualquier nivel de inserción pero aumentaríamos la generación
        // de ids exponencialmente y la lucha contra los nodos intrusos en cualquier parte es casi imposible
        // y por otra parte está la inserción via markup (innerHTML) en donde el cacheado de los nodos insertados
        // sería muy tedioso.

        if (!clientDoc.getItsNatStfulDocument().isNewNodeDirectChildOfContentRoot(newNode))
            return null;
        
        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCacheRegistry();
        if (nodeCache == null) return null;
        String id = nodeCache.addNode(newNode); // Si devuelve null es que no se puede cachear el nodo o caché "bloqueada"
        
        // Este idJS es para métodos especiales en donde opcionalmente podemos pasar el id del nodo cacheado (si se pudo cachear sino pues null), es simplemente el "id", en este caso no es necesario y no sigue el convencionalismo de arrays de NodeLocation
        return id != null ? "\"" + id + "\"" : "null";        
    }
}
