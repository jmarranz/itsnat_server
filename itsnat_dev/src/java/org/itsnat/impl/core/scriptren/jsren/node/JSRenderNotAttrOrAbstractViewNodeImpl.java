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

package org.itsnat.impl.core.scriptren.jsren.node;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.JSAndBSRenderNotAttrOrAbstractViewNodeImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.RenderNotAttrOrAbstractViewNode;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderNotAttrOrAbstractViewNodeImpl extends JSRenderNodeImpl implements RenderNotAttrOrAbstractViewNode
{

    /** Creates a new instance of JSNoAttributeRender */
    public JSRenderNotAttrOrAbstractViewNodeImpl()
    {
    }

    public abstract Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc);

    public String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulDelegateImpl clientDoc)
    {
        String newNodeCode = createNodeCode(newNode,clientDoc);
        return getAppendCompleteChildNode(parentVarName,newNode,newNodeCode,clientDoc);
    }

    protected String getAppendCompleteChildNode(String parentVarName,Node newNode,String newNodeCode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderNotAttrOrAbstractViewNodeImpl.getAppendCompleteChildNode(parentVarName, newNode, newNodeCode, clientDoc);
    }

    public String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        String newNodeCode = createNodeCode(newNode,clientDoc);
        return getInsertCompleteNodeCode(newNode,newNodeCode,clientDoc);
    }

    public abstract Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulDelegateWebImpl clientDoc);

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return JSAndBSRenderNotAttrOrAbstractViewNodeImpl.getRemoveNodeCode(removedNode, clientDoc);
    }

    public String getInsertCompleteNodeCode(Node newNode,String newNodeCode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderNotAttrOrAbstractViewNodeImpl.getInsertCompleteNodeCode(newNode,newNodeCode,clientDoc);
    }

    public String getRemoveAllChildCode(Node parentNode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return JSAndBSRenderNotAttrOrAbstractViewNodeImpl.getRemoveAllChildCode(parentNode, clientDoc);
    }
    

}
