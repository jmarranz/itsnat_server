/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.scriptren.shared.dom.node;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.CodeListImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderHasChildrenNodeImpl extends JSAndBSRenderNotAttrOrAbstractViewNodeImpl 
{
    public static Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc,RenderHasChildrenNode render)
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
    
    public static Object getInsertNewNodeCode(Node newNode,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc,RenderHasChildrenNode render)
    {
        CodeListImpl code = new CodeListImpl();

        if (render.isCreateComplete(newNode))
        {
            code.add( render.getInsertCompleteNodeCode(newNode,clientDoc) );
        }
        else
        {
            String newNodeVarName = "child";

            code.add( "var " + newNodeVarName + " = " + render.createNodeCode(newNode,clientDoc) + ";\n" );

            if (newNode.hasAttributes())
                code.add( render.addAttributesBeforeInsertNode(newNode,newNodeVarName,clientDoc) );

            boolean hasChildNodes = newNode.hasChildNodes();
            boolean beforeParent = false; // Este valor es indiferente si hasChildNodes es false
            if (hasChildNodes) beforeParent = render.isAddChildNodesBeforeNode(newNode,clientDoc);

            if (hasChildNodes && beforeParent)
                code.add( render.appendChildNodes(newNode,newNodeVarName,beforeParent,insertMarkupInfo,clientDoc) );

            code.add( getInsertCompleteNodeCode(newNode,newNodeVarName,clientDoc) );

            if (hasChildNodes && !beforeParent)
                code.add( render.appendChildNodes(newNode,newNodeVarName,beforeParent,insertMarkupInfo,clientDoc) );
        }

        return code;
    }    
}
