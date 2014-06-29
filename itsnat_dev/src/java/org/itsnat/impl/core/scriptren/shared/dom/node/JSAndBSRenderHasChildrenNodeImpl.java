/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.dom.node;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.CodeListImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderHasChildrenNodeImpl extends JSAndBSRenderNotAttrOrAbstractViewNodeImpl 
{
    public static Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateWebImpl clientDoc,RenderHasChildrenNode render)
    {
        // Es añadido al final no inserción en medio
        CodeListImpl code = new CodeListImpl();

        if (render.isCreateComplete(newNode))
        {
            code.add( render.getAppendCompleteChildNode(parent,newNode,parentVarName,clientDoc) );
        }
        else
        {
            if (parentVarName == null) throw new ItsNatException("INTERNAL ERROR");

            String newNodeVarName = parentVarName + "_c"; // c = child (para ahorrar letras)

            code.add( "var " + newNodeVarName + " = " + render.createNodeCode(newNode,clientDoc) + ";\n" );

            if (newNode.hasAttributes())
                code.add( render.addAttributesBeforeInsertNode(newNode,newNodeVarName,clientDoc) );

            boolean hasChildNodes = newNode.hasChildNodes();
            boolean beforeParent = false; // Este valor es indiferente si hasChildNodes es false
            if (hasChildNodes) beforeParent = render.isAddChildNodesBeforeNode(newNode,clientDoc);

            if (hasChildNodes && beforeParent)
                code.add( render.appendChildNodes(newNode,newNodeVarName,beforeParent,insertMarkupInfo,clientDoc) );

            code.add( getAppendCompleteChildNode(parentVarName,newNode,newNodeVarName,clientDoc) );

            if (hasChildNodes && !beforeParent)
                code.add( render.appendChildNodes(newNode,newNodeVarName,beforeParent,insertMarkupInfo,clientDoc) );
        }

        return code;
    }    
}
