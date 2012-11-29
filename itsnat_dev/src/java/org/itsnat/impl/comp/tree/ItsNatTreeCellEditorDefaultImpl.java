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

import org.itsnat.impl.comp.inplace.ItsNatCellEditorImpl;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.tree.ItsNatTree;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatTreeCellEditorDefaultImpl extends ItsNatCellEditorImpl implements ItsNatTreeCellEditor
{

    /**
     * Creates a new instance of ItsNatTreeCellEditorDefaultImpl
     */
    public ItsNatTreeCellEditorDefaultImpl(ItsNatComponent compEditor,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(compEditor,componentMgr);
    }

    public ItsNatComponent getTreeCellEditorComponent(ItsNatTree tree,int row, Object value, boolean isSelected, boolean expanded, boolean leaf, Element labelElem)
    {
        if (labelElem == null) labelElem = tree.getItsNatTreeUI().getLabelElementFromRow(row);

        return getCellEditorComponent(value,labelElem);
    }
}
