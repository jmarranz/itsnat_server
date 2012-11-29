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

package org.itsnat.impl.comp.list;

import org.itsnat.impl.comp.inplace.ItsNatCellEditorImpl;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellEditor;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatListCellEditorDefaultImpl extends ItsNatCellEditorImpl implements ItsNatListCellEditor
{

    /**
     * Creates a new instance of ItsNatListCellEditorDefaultImpl
     */
    public ItsNatListCellEditorDefaultImpl(ItsNatComponent compEditor,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(compEditor,componentMgr);
    }

    public ItsNatComponent getListCellEditorComponent(ItsNatList list, int index, Object value, boolean isSelected,Element cellContentElem)
    {
        if (cellContentElem == null) cellContentElem = list.getItsNatListUI().getContentElementAt(index);
        return getCellEditorComponent(value,cellContentElem);
    }
}
