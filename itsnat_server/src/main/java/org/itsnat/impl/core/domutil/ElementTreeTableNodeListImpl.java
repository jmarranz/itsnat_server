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

package org.itsnat.impl.core.domutil;

import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ElementTreeTableNodeListImpl extends ElementTreeNodeListImpl
{
    /** Creates a new instance of ElementTreeTableNodeListImpl */
    public ElementTreeTableNodeListImpl(ItsNatDocumentImpl itsNatDoc,ElementTreeTableNodeImpl parentTreeNode,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        super(itsNatDoc,parentTreeNode,structure,renderer);
    }

    public abstract ElementListImpl getGlobalChildList();


    public Element getParentElement()
    {
        return getGlobalChildList().getParentElement();
    }

    public ElementTreeNodeImpl createTreeNode(int index,Element childElem)
    {
        return new ElementTreeTableNodeImpl(getItsNatDocumentImpl(),this,index,childElem,structure,renderer);
    }

    public Element getChildPatternElement()
    {
        return getGlobalChildList().getChildPatternElement();
    }

    public int getRow(int index)
    {
        if (index == 0)
        {
            if (parentTreeNode == null) // Caso de lista contenedora del root
                return 0; // Se pide la row del root: 0
            else
                return parentTreeNode.getRow() + 1;
        }
        else
        {
            ElementTreeNodeImpl treeNode = (ElementTreeNodeImpl)getTreeNodeAt(index);
            if (treeNode == null)
            {
                // Caso por ejemplo de obtener la row para añadir al final de la lista
                // No debería ser nulo el resultado ahora
                treeNode = (ElementTreeNodeImpl)getTreeNodeAt(index - 1);
                treeNode = treeNode.getDeepMostLastTreeNode();
                return treeNode.getRow() + 1;
            }
            else
            {
                return treeNode.getRow();
            }
        }
    }

    protected Element addTreeNodeDOMElementAt()
    {
        int index = getLength();  // posición relativa
        return insertTreeNodeDOMElementAt(index);
    }

    protected Element insertTreeNodeDOMElementAt(int index)
    {
        // index es la posición relativa
        int row = getRow(index); // fila global
        return getGlobalChildList().insertElementAt(row);
    }

    protected Element removeTreeNodeDOMElementAt(int index)
    {
        ElementTreeNodeImpl treeNodeStart = (ElementTreeNodeImpl)getTreeNodeAt(index);
        if (treeNodeStart == null) return null; // Fuera de rango
        ElementTreeNodeImpl treeNodeEnd = (ElementTreeNodeImpl)treeNodeStart.getDeepMostLastTreeNode();
        // treeNodeEnd puede ser el mismo que treeNodeStart

        int rowFrom = treeNodeStart.getRow();
        int rowTo = treeNodeEnd.getRow();
        getGlobalChildList().removeElementRange(rowFrom,rowTo);

        return treeNodeStart.getParentElement();
    }

    public Element getTreeContainerElement()
    {
        return getGlobalChildList().getParentElement();
    }
}
