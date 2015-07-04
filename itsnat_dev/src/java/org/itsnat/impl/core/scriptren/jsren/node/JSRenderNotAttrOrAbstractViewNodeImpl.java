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

import org.itsnat.core.ItsNatDOMException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.scriptren.shared.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.shared.node.JSAndBSRenderNotAttrOrAbstractViewNodeImpl;
import org.itsnat.impl.core.scriptren.shared.node.RenderNotAttrOrAbstractViewNode;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

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

    public static JSRenderNotAttrOrAbstractViewNodeImpl getJSRenderNotAttrOrAbstractViewNode(Node node,ClientDocumentStfulDelegateWebImpl clientDoc)
    {       
        int nodeType = node.getNodeType();
        switch(nodeType)
        {
            // Está primero los más habituales (Element y Text nodes)
            case Node.ELEMENT_NODE:
                return JSRenderElementImpl.getJSRenderElement((Element)node,clientDoc);
            case Node.TEXT_NODE:
                return JSRenderTextImpl.getJSRenderText((Text)node,clientDoc);
            case Node.CDATA_SECTION_NODE:
                return JSRenderCDATASectionImpl.SINGLETON;
            case Node.COMMENT_NODE:
                return JSRenderCommentImpl.getJSRenderComment((Comment)node,clientDoc);
            case Node.DOCUMENT_FRAGMENT_NODE:
                return JSRenderDocumentFragmentImpl.SINGLETON;
            case Node.ENTITY_REFERENCE_NODE:
                return JSRenderEntityReferenceImpl.SINGLETON;
            case Node.PROCESSING_INSTRUCTION_NODE:
                return JSRenderProcessingInstructionImpl.SINGLETON;
            case Node.DOCUMENT_NODE:
                throw new ItsNatDOMException("Unexpected Document node",node);
            case Node.DOCUMENT_TYPE_NODE:
                throw new ItsNatDOMException("Unexpected DocumentType node",node);
            case Node.ENTITY_NODE:
                throw new ItsNatDOMException("Unexpected Entity node",node);
            case Node.NOTATION_NODE:
                throw new ItsNatDOMException("Unexpected Notation node",node);
        }

        throw new ItsNatDOMException("Internal error",node);
    }    
    
    public abstract Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc);

    @Override
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
