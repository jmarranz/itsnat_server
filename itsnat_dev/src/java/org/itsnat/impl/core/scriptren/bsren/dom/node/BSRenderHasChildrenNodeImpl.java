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
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.CodeListImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderHasChildrenNodeImpl extends BSRenderNotAttrOrAbstractViewNodeImpl
{

    /** Creates a new instance of BSRenderHasChildrenNodeImpl */
    public BSRenderHasChildrenNodeImpl()
    {
    }

    protected abstract String addAttributesBeforeInsertNode(Node node,String elemVarName,ClientDocumentStfulDelegateDroidImpl clientDoc);

    protected boolean isCreateComplete(Node node)
    {
        return !node.hasAttributes() && !node.hasChildNodes();
    }

    public abstract boolean isAddChildNodesBeforeNode(Node parent,ClientDocumentStfulDelegateDroidImpl clientDoc);

    public Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        // Es añadido al final no inserción en medio
        CodeListImpl code = new CodeListImpl();

        if (isCreateComplete(newNode))
        {
            code.add( getAppendCompleteChildNode(parent,newNode,parentVarName,clientDoc) );
        }
        else
        {
            if (parentVarName == null) throw new ItsNatException("INTERNAL ERROR");

            String newNodeVarName = parentVarName + "_c"; // c = child (para ahorrar letras)

            code.add( "var " + newNodeVarName + " = " + createNodeCode(newNode,clientDoc) + ";\n" );

            if (newNode.hasAttributes())
                code.add( addAttributesBeforeInsertNode(newNode,newNodeVarName,clientDoc) );

            boolean hasChildNodes = newNode.hasChildNodes();
            boolean beforeParent = false; // Este valor es indiferente si hasChildNodes es false
            if (hasChildNodes) beforeParent = isAddChildNodesBeforeNode(newNode,clientDoc);

            if (hasChildNodes && beforeParent)
                code.add( appendChildNodes(newNode,newNodeVarName,beforeParent,insertMarkupInfo,clientDoc) );

            code.add( getAppendCompleteChildNode(parentVarName,newNode,newNodeVarName,clientDoc) );

            if (hasChildNodes && !beforeParent)
                code.add( appendChildNodes(newNode,newNodeVarName,beforeParent,insertMarkupInfo,clientDoc) );
        }

        return code;
    }

    public Object getInsertNewNodeCode(Node newNode,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        CodeListImpl code = new CodeListImpl();

        if (isCreateComplete(newNode))
        {
            code.add( getInsertCompleteNodeCode(newNode,clientDoc) );
        }
        else
        {
            String newNodeVarName = "child";

            code.add( "var " + newNodeVarName + " = " + createNodeCode(newNode,clientDoc) + ";\n" );

            if (newNode.hasAttributes())
                code.add( addAttributesBeforeInsertNode(newNode,newNodeVarName,clientDoc) );

            boolean hasChildNodes = newNode.hasChildNodes();
            boolean beforeParent = false; // Este valor es indiferente si hasChildNodes es false
            if (hasChildNodes) beforeParent = isAddChildNodesBeforeNode(newNode,clientDoc);

            if (hasChildNodes && beforeParent)
                code.add( appendChildNodes(newNode,newNodeVarName,beforeParent,insertMarkupInfo,clientDoc) );

            code.add( getInsertCompleteNodeCode(newNode,newNodeVarName,clientDoc) );

            if (hasChildNodes && !beforeParent)
                code.add( appendChildNodes(newNode,newNodeVarName,beforeParent,insertMarkupInfo,clientDoc) );
        }

        return code;
    }

    protected Object appendChildNodes(Node parent, String parentVarName,boolean beforeParent,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateDroidImpl clientDoc)
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
                BSRenderNotAttrOrAbstractViewNodeImpl childRender = (BSRenderNotAttrOrAbstractViewNodeImpl)BSRenderNodeImpl.getBSRenderNode(child,clientDoc);
                code.add( childRender.getAppendNewNodeCode(parent,child,parentVarName,insertMarkupInfo,clientDoc) );

                child = child.getNextSibling();
            }
        }
        
        return code;
    }
}
