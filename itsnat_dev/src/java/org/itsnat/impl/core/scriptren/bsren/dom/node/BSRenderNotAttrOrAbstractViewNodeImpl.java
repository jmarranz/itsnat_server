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

package org.itsnat.impl.core.scriptren.bsren.dom.node;

import org.itsnat.impl.core.scriptren.shared.dom.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.clientdoc.NodeCacheRegistryImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderNotAttrOrAbstractViewNodeImpl extends BSRenderNodeImpl
{

    /** Creates a new instance of JSNoAttributeRender */
    public BSRenderNotAttrOrAbstractViewNodeImpl()
    {
    }

    protected abstract String createNodeCode(Node node,ClientDocumentStfulDelegateDroidImpl clientDoc);

    public abstract Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateDroidImpl clientDoc);

    protected String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        String newNodeCode = createNodeCode(newNode,clientDoc);
        return getAppendCompleteChildNode(parentVarName,newNode,newNodeCode,clientDoc);
    }

    protected String getAppendCompleteChildNode(String parentVarName,Node newNode,String newNodeCode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        String idJS = cacheNewNodeIfNeededAndGenId(newNode,clientDoc);      
        return "itsNatDoc.appendChild2(" + parentVarName + "," + newNodeCode + "," + idJS + ");\n";
    }

    protected String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        String newNodeCode = createNodeCode(newNode,clientDoc);
        return getInsertCompleteNodeCode(newNode,newNodeCode,clientDoc);
    }

    public abstract Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulDelegateDroidImpl clientDoc);

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulDelegateDroidImpl clientDoc)
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
            childRelPath = toLiteralStringBS(childRelPath);
            return "itsNatDoc.removeChild3(" + parentLoc.toScriptNodeLocation(true) + "," + childRelPath + "," + isText + ");\n";
        }
    }

    protected String getInsertCompleteNodeCode(Node newNode,String newNodeCode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        Node parent = newNode.getParentNode();

        // Obtenemos el sibling con representación en el DOM cliente (no filtrado)
        // Sólo hay filtrado de los comentarios en trozos de SVG gestionados por SVGWeb,
        // dichos comentarios no están en el DOM y afortunadamente no son visibles 
        // No consideramos el filtrado en servidor de los nodos de texto con espacios, que a veces son filtrados
        // en algunos navegadores (MSIE por ejemplo) pues ItsNat está preparado para ello si no se encuentra en el cliente.

        NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parent,true);
        String idJS = cacheNewNodeIfNeededAndGenId(newNode,clientDoc);
        
        Node nextSibling = clientDoc.getNextSiblingInClientDOM(newNode);
        if (nextSibling != null)
        {
            NodeLocationImpl refNodeLoc = clientDoc.getRefNodeLocationInsertBefore(newNode,nextSibling);
            return "itsNatDoc.insertBefore3(" + parentLoc.toScriptNodeLocation(true) + "," + newNodeCode + "," + refNodeLoc.toScriptNodeLocation(true) + "," + idJS + ");\n";
        }
        else
        {
            return "itsNatDoc.appendChild3(" + parentLoc.toScriptNodeLocation(true) + "," + newNodeCode + "," + idJS + ");\n";
        }
    }

    public String getRemoveAllChildCode(Node parentNode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parentNode,true);
        return "itsNatDoc.removeAllChild2(" + parentLoc.toScriptNodeLocation(true) + ");\n";
    }
    
    public String cacheNewNodeIfNeededAndGenId(Node newNode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCacheRegistry();
        if (nodeCache == null) return null;        
        
        return nodeCache.cacheNewNodeIfNeededAndGenId(newNode);     
    }
}
