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

package org.itsnat.impl.comp.tree;

import java.io.Serializable;
import org.itsnat.comp.tree.ItsNatTree;
import org.itsnat.comp.tree.ItsNatTreeStructure;
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.impl.core.domutil.ElementTreeNodeImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatTreeStructureCoreAdapterImpl implements ElementTreeNodeStructure,Serializable
{
    protected ItsNatTreeStructure structure;
    protected ItsNatTreeUIImpl treeUI;

    /**
     * Creates a new instance of ItsNatTreeStructureCoreAdapterImpl
     */
    public ItsNatTreeStructureCoreAdapterImpl(ItsNatTreeStructure structure,ItsNatTreeUIImpl treeUI)
    {
        this.structure = structure;
        this.treeUI = treeUI;
    }

    public ItsNatTree getItsNatTree()
    {
        return treeUI.getItsNatTree();
    }

    public int getLogicRow(ElementTreeNode treeNode)
    {
        return treeUI.getRow((ElementTreeNodeImpl)treeNode);
    }

    public Element getContentElement(ElementTreeNode treeNode,Element nodeParent)
    {
        if (nodeParent == null) nodeParent = treeNode.getParentElement();
        return structure.getContentElement(getItsNatTree(),getLogicRow(treeNode),nodeParent);
    }

    public Element getHandleElement(ElementTreeNode treeNode,Element nodeParent)
    {
        if (nodeParent == null) nodeParent = treeNode.getParentElement();
        return structure.getHandleElement(getItsNatTree(),getLogicRow(treeNode),nodeParent);
    }

    public Element getIconElement(ElementTreeNode treeNode,Element nodeParent)
    {
        if (nodeParent == null) nodeParent = treeNode.getParentElement();
        return structure.getIconElement(getItsNatTree(),getLogicRow(treeNode),nodeParent);
    }

    public Element getLabelElement(ElementTreeNode treeNode,Element nodeParent)
    {
        if (nodeParent == null) nodeParent = treeNode.getParentElement();
        return structure.getLabelElement(getItsNatTree(),getLogicRow(treeNode),nodeParent);
    }

    public Element getChildListElement(ElementTreeNode treeNode,Element nodeParent)
    {
        if (nodeParent == null) nodeParent = treeNode.getParentElement();
        return structure.getChildListElement(getItsNatTree(),getLogicRow(treeNode),nodeParent);
    }

}
