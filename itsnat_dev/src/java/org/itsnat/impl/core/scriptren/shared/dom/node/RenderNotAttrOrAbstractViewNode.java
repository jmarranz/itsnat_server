/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.dom.node;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public interface RenderNotAttrOrAbstractViewNode extends RenderNode
{
    public String createNodeCode(Node node,ClientDocumentStfulDelegateImpl clientDoc);
    public String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulDelegateImpl clientDoc);
    public String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulDelegateImpl clientDoc);    
}
