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

import java.util.ArrayList;
import java.util.Iterator;
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ElementTreeNormalNodeListImpl extends ElementTreeNodeListImpl
{
    protected ElementListImpl childElemList;
    protected boolean removePattern;

    /**
     * Creates a new instance of ElementTreeNormalNodeListImpl
     */
    public ElementTreeNormalNodeListImpl(ItsNatDocumentImpl itsNatDoc,ElementTreeNodeNormalImpl parentTreeNode,Element childListParentElem,Element childPatternElement,boolean clonePattern,
            boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        super(itsNatDoc,parentTreeNode,structure,renderer);

        this.removePattern = removePattern;

        // Necesariamente debe funcionar en modo master porque se mantiene la lista childTreeNodes
        // que no se entera si se añaden nodos directamente via DOM
        // No necesitamos renderizar con esta lista
        ElementGroupManagerImpl factory = getItsNatDocumentImpl().getElementGroupManagerImpl();
        this.childElemList = factory.createElementListNoRenderInternal(childListParentElem,childPatternElement,clonePattern,removePattern);

        initialSynch();
    }

    protected void initialSynch()
    {
        // Puede haber elementos en la lista cuando se crea el gestor
        // necesitamos mantener una lista de objetos Pattern... sincronizada con los nodos
        // No se deben añadir o quitar nodos DOM padres de TreeNodes utilizando directamente DOM
        // pues desincronizaremos

        if (!childElemList.isEmpty())
        {
            ArrayList childTreeNodes = getInternalTreeNodeList();
            int i = 0;
            for(Iterator it = childElemList.getInternalElementListFree().iterator(); it.hasNext(); )
            {
                Element elem = (Element)it.next();

                ElementTreeNodeImpl treeNode = createTreeNode(i,elem);
                childTreeNodes.add(treeNode);

                i++;
            }
        }
    }

    public Element getParentElement()
    {
        return childElemList.getParentElement();
    }

    public ElementTreeNodeImpl createTreeNode(int index,Element childElem)
    {
        return new ElementTreeNodeNormalImpl(getItsNatDocumentImpl(),this,index,childElem,getChildPatternElement(),false,removePattern,structure,renderer);
    }

    public Element getChildPatternElement()
    {
        return childElemList.getChildPatternElement();
    }

    protected Element addTreeNodeDOMElementAt()
    {
        return childElemList.addElement();
    }

    protected Element insertTreeNodeDOMElementAt(int index)
    {
        return childElemList.insertElementAt(index);
    }

    protected Element removeTreeNodeDOMElementAt(int index)
    {
        return childElemList.removeElementAt(index);
    }

    public Element getTreeContainerElement()
    {
        if (parentTreeNode != null)
            return parentTreeNode.getTreeContainerElement();
        else
            return childElemList.getParentElement();
    }
}
