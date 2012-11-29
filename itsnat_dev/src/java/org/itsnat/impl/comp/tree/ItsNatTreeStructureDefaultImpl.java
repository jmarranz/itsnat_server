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
import org.itsnat.impl.core.domutil.ElementTreeNodeStructureDefaultImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatTreeStructureDefaultImpl implements ItsNatTreeStructure,Serializable
{
    protected final static ItsNatTreeStructureDefaultImpl SINGLETON = new ItsNatTreeStructureDefaultImpl();

    /** Creates a new instance of ItsNatTreeStructureDefaultImpl */
    private ItsNatTreeStructureDefaultImpl()
    {
    }

    public static ItsNatTreeStructureDefaultImpl newItsNatTreeStructureDefault()
    {
        // No se guarda estado, usamos el SINGLETON en este falso método factoría
        return SINGLETON;
    }

    public Element getContentElement(ItsNatTree tree, int row, Element nodeParent)
    {
        if (nodeParent == null) nodeParent = tree.getItsNatTreeUI().getParentElementFromRow(row);
        return ElementTreeNodeStructureDefaultImpl.getContentElement(tree.isTreeTable(),nodeParent);
    }

    public Element getHandleElement(ItsNatTree tree, int row, Element nodeParent)
    {
        if (nodeParent == null) nodeParent = tree.getItsNatTreeUI().getParentElementFromRow(row);
        return ElementTreeNodeStructureDefaultImpl.getHandleElement(tree.isTreeTable(),nodeParent);
    }

    public Element getIconElement(ItsNatTree tree, int row, Element nodeParent)
    {
        if (nodeParent == null) nodeParent = tree.getItsNatTreeUI().getParentElementFromRow(row);
        return ElementTreeNodeStructureDefaultImpl.getIconElement(tree.isTreeTable(),nodeParent);
    }

    public Element getLabelElement(ItsNatTree tree, int row, Element nodeParent)
    {
        if (nodeParent == null) nodeParent = tree.getItsNatTreeUI().getParentElementFromRow(row);
        return ElementTreeNodeStructureDefaultImpl.getLabelElement(tree.isTreeTable(),nodeParent);
    }

    public Element getChildListElement(ItsNatTree tree, int row, Element nodeParent)
    {
        if (nodeParent == null) nodeParent = tree.getItsNatTreeUI().getParentElementFromRow(row);
        return ElementTreeNodeStructureDefaultImpl.getChildListElement(tree.isTreeTable(),nodeParent);
    }
}
