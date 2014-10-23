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

import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.scriptren.shared.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.domimpl.AbstractViewImpl;
import org.itsnat.impl.core.scriptren.shared.node.JSAndBSRenderNotAttrOrAbstractViewNodeImpl;
import org.itsnat.impl.core.scriptren.shared.node.RenderNotAttrOrAbstractViewNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderNotAttrOrAbstractViewNodeImpl extends BSRenderNodeImpl implements RenderNotAttrOrAbstractViewNode
{

    /** Creates a new instance of JSNoAttributeRender */
    public BSRenderNotAttrOrAbstractViewNodeImpl()
    {
    }

    public static BSRenderNotAttrOrAbstractViewNodeImpl getBSRenderNotAttrOrAbstractViewNode(Node node)
    {
        int nodeType = node.getNodeType();
        switch(nodeType)
        {
            // Está primero los más habituales (Element y Text nodes)
            case Node.ELEMENT_NODE:
                return BSRenderElementImpl.getBSRenderElement((Element)node);
            case Node.TEXT_NODE:
                return BSRenderTextImpl.getBSRenderText();            
            case Node.CDATA_SECTION_NODE:
                throw new ItsNatDOMException("Unexpected CDATA section node",node);
            case Node.COMMENT_NODE:
                throw new ItsNatDOMException("Unexpected comment node",node);                
            case Node.DOCUMENT_FRAGMENT_NODE:
                throw new ItsNatDOMException("Unexpected document fragment node",node);
            case Node.ENTITY_REFERENCE_NODE:
                throw new ItsNatDOMException("Unexpected entity reference node",node);
            case Node.PROCESSING_INSTRUCTION_NODE:
                throw new ItsNatDOMException("Unexpected processing instruction node",node);
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

    public abstract Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulDelegateDroidImpl clientDoc);

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderNotAttrOrAbstractViewNodeImpl.getRemoveNodeCode(removedNode, clientDoc);        
    }

    public String getInsertCompleteNodeCode(Node newNode,String newNodeCode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderNotAttrOrAbstractViewNodeImpl.getInsertCompleteNodeCode(newNode,newNodeCode,clientDoc);
    }
   
    public String getRemoveAllChildCode(Node parentNode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderNotAttrOrAbstractViewNodeImpl.getRemoveAllChildCode(parentNode, clientDoc);
    }

}
