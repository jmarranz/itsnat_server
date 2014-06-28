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

package org.itsnat.impl.core.scriptren.jsren.dom.node;

import org.itsnat.impl.core.scriptren.shared.dom.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.clientdoc.NodeCacheRegistryImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.JSAndBSRenderNotAttrOrAbstractViewNodeImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderNotAttrOrAbstractViewNodeImpl extends JSRenderNodeImpl
{

    /** Creates a new instance of JSNoAttributeRender */
    public JSRenderNotAttrOrAbstractViewNodeImpl()
    {
    }

    protected abstract String createNodeCode(Node node,ClientDocumentStfulDelegateWebImpl clientDoc);

    public abstract Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateWebImpl clientDoc);

    protected String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        String newNodeCode = createNodeCode(newNode,clientDoc);
        return getAppendCompleteChildNode(parentVarName,newNode,newNodeCode,clientDoc);
    }

    protected String getAppendCompleteChildNode(String parentVarName,Node newNode,String newNodeCode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        String idJS = cacheNewNodeIfNeededAndGenId(newNode,clientDoc);      
        return "itsNatDoc.appendChild2(" + parentVarName + "," + newNodeCode + "," + idJS + ");\n";
    }

    protected String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        String newNodeCode = createNodeCode(newNode,clientDoc);
        return getInsertCompleteNodeCode(newNode,newNodeCode,clientDoc);
    }

    public abstract Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulDelegateWebImpl clientDoc);

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return JSAndBSRenderNotAttrOrAbstractViewNodeImpl.getRemoveNodeCode(removedNode, clientDoc);
    }

    protected String getInsertCompleteNodeCode(Node newNode,String newNodeCode,ClientDocumentStfulDelegateWebImpl clientDoc)
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

    public String getRemoveAllChildCode(Node parentNode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parentNode,true);
        return "itsNatDoc.removeAllChild2(" + parentLoc.toScriptNodeLocation(true) + ");\n";
    }
    
    public String cacheNewNodeIfNeededAndGenId(Node newNode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCacheRegistry();
        if (nodeCache == null) return null;        
        
        return nodeCache.cacheNewNodeIfNeededAndGenId(newNode);     
    }
}
