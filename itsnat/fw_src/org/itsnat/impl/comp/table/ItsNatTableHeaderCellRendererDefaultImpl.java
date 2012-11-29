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

package org.itsnat.impl.comp.table;

import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.*;
import org.itsnat.comp.table.ItsNatTableHeader;
import org.itsnat.comp.table.ItsNatTableHeaderCellRenderer;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatTableHeaderCellRendererDefaultImpl extends ItsNatCellRendererDefaultImpl implements ItsNatTableHeaderCellRenderer
{

    /**
     * Creates a new instance of ItsNatTableHeaderCellRendererDefaultImpl
     */
    public ItsNatTableHeaderCellRendererDefaultImpl(ItsNatDocComponentManagerImpl componentMgr)
    {
        super(componentMgr);
    }

    public void renderTableHeaderCell(ItsNatTableHeader tableHeader, int column, Object value, boolean isSelected, boolean cellHasFocus,
            Element cellContentElem,boolean isNew)
    {
        if (cellContentElem == null) cellContentElem = tableHeader.getItsNatTableHeaderUI().getContentElementAt(column);
        renderCell(tableHeader,value,cellContentElem,isNew);
    }

    public void unrenderTableHeaderCell(ItsNatTableHeader tableHeader,int column,
                    Element columnContentElem)
    {
    }
}
