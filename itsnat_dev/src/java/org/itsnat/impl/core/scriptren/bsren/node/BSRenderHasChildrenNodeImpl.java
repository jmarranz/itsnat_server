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

package org.itsnat.impl.core.scriptren.bsren.node;

import org.itsnat.impl.core.scriptren.shared.dom.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.CodeListImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.JSAndBSRenderHasChildrenNodeImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.RenderHasChildrenNode;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderHasChildrenNodeImpl extends BSRenderNotAttrOrAbstractViewNodeImpl implements RenderHasChildrenNode
{

    /** Creates a new instance of BSRenderHasChildrenNodeImpl */
    public BSRenderHasChildrenNodeImpl()
    {
    }

    public boolean isCreateComplete(Node node)
    {
        return !node.hasAttributes() && !node.hasChildNodes();
    }

    @Override
    public String getAppendCompleteChildNode(String parentVarName,Node newNode,String newNodeCode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderHasChildrenNodeImpl.getAppendCompleteChildNode(parentVarName, newNode, newNodeCode, clientDoc);
    }    
    
    public Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderHasChildrenNodeImpl.getAppendNewNodeCode(parent, newNode, parentVarName, insertMarkupInfo,clientDoc,this);        
    }

    public Object getInsertNewNodeCode(Node newNode,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderHasChildrenNodeImpl.getInsertNewNodeCode(newNode,insertMarkupInfo,clientDoc,this);        
    }

    public Object appendChildNodes(Node parent, String parentVarName,boolean beforeParent,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // Sólo es llamado si hay algún hijo

        // Ojo, si el nodo a serializar de esta forma tiene
        // nodos procedientes de fragmentos cacheados no se pueden
        // tratar aquí, la solución es inhabilitar el cacheado
        // en el fragmento a insertar o bien declarar itsnat:nocache="true"
        // en el nodo problemático que se cachea automáticamente
        // En el caso de inserción de fragmentos XML en documentos XML
        // no es problema pues a día de hoy no admite eventos y este código
        // es llamado ante mutation events los cuales no están activados
        // en la manipulación de un documento todavía no cargado

        CodeListImpl code = new CodeListImpl();

        if (parent.hasChildNodes())
        {
            Node child = parent.getFirstChild();
            while(child != null)
            {
                BSRenderNotAttrOrAbstractViewNodeImpl childRender = (BSRenderNotAttrOrAbstractViewNodeImpl)BSRenderNodeImpl.getBSRenderNode(child);
                code.add( childRender.getAppendNewNodeCode(parent,child,parentVarName,insertMarkupInfo,clientDoc) );

                child = child.getNextSibling();
            }
        }
        
        return code;
    }
}
