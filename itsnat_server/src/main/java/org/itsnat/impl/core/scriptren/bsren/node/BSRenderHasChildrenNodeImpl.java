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

import org.itsnat.impl.core.scriptren.shared.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.CodeListImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.scriptren.shared.node.JSAndBSRenderHasChildrenNodeImpl;
import org.itsnat.impl.core.scriptren.shared.node.RenderHasChildrenNode;
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

    @Override
    public boolean isCreateComplete(Node node)
    {
        return !node.hasAttributes() && !node.hasChildNodes();
    }

    @Override
    public String getAppendCompleteChildNode(String parentVarName,Node newNode,String newNodeCode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderHasChildrenNodeImpl.getAppendCompleteChildNode(parentVarName, newNode, newNodeCode, clientDoc);
    }    
    
    @Override
    public Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderHasChildrenNodeImpl.getAppendNewNodeCode(parent, newNode, parentVarName, insertMarkupInfo,clientDoc,this);        
    }

    public Object getInsertNewNodeCode(Node newNode,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderHasChildrenNodeImpl.getInsertNewNodeCode(newNode,insertMarkupInfo,clientDoc,this);        
    }

    @Override
    public Object appendChildNodes(Node parent, String parentVarName,boolean beforeParent,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // Sólo es llamado si hay algún hijo

        CodeListImpl code = new CodeListImpl();

        if (parent.hasChildNodes())
        {
            Node child = parent.getFirstChild();
            while(child != null)
            {
                BSRenderNotAttrOrAbstractViewNodeImpl childRender = BSRenderNotAttrOrAbstractViewNodeImpl.getBSRenderNotAttrOrAbstractViewNode(child);
                code.add( childRender.getAppendNewNodeCode(parent,child,parentVarName,insertMarkupInfo,clientDoc) );

                child = child.getNextSibling();
            }
        }
        
        return code;
    }
}
