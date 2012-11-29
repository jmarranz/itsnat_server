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

import java.io.Serializable;
import org.itsnat.core.domutil.ElementTableBase;
import org.itsnat.core.domutil.ListElementInfo;
import org.itsnat.core.domutil.TableCellElementInfo;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class TableCellElementInfoImpl implements TableCellElementInfo,Serializable
{
    // Si es master el objeto estará asociada a la celda, si cambia la fila
    // de posición o la celda cambia de columna automáticamente se enterarán los
    // objectos ListElementInfo, idem si se cambia el Element.
    protected ListElementInfo rowInfo;
    protected ListElementInfo cellInfo;
    protected ElementTableBaseImpl table;

    /**
     * Creates a new instance of TableCellElementInfoImpl
     */
    public TableCellElementInfoImpl(ListElementInfo rowInfo,ListElementInfo cellInfo,ElementTableBaseImpl table)
    {
        this.rowInfo = rowInfo;
        this.cellInfo = cellInfo;
        this.table = table;
    }

    public Element getRowElement()
    {
        return rowInfo.getElement();
    }

    public int getRowIndex()
    {
        return rowInfo.getIndex();
    }

    public Element getCellElement()
    {
        return cellInfo.getElement();
    }

    public int getColumnIndex()
    {
        return cellInfo.getIndex();
    }

    public ElementTableBase getParentTable()
    {
        return table;
    }
}
