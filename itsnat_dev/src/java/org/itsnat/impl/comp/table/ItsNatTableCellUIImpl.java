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

import java.io.Serializable;
import org.itsnat.comp.table.ItsNatTableCellUI;
import org.itsnat.comp.table.ItsNatTableUI;
import org.itsnat.impl.core.domutil.TableCellElementInfoMasterImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatTableCellUIImpl implements ItsNatTableCellUI,Serializable
{
    protected TableCellElementInfoMasterImpl cellInfo;
    protected ItsNatTableUIImpl tableUI;

    /**
     * Creates a new instance of ItsNatListCellUIImpl
     */
    protected ItsNatTableCellUIImpl(TableCellElementInfoMasterImpl cellInfo,ItsNatTableUIImpl tableUI)
    {
        this.cellInfo = cellInfo;
        this.tableUI = tableUI;
    }

    public static ItsNatTableCellUIImpl getItsNatTableCellUI(TableCellElementInfoMasterImpl cellInfo,ItsNatTableUIImpl tableUI)
    {
        if (cellInfo == null) return null;

        ItsNatTableCellUIImpl cellUI = (ItsNatTableCellUIImpl)cellInfo.getAuxObject();
        if (cellUI == null)
        {
            cellUI = new ItsNatTableCellUIImpl(cellInfo,tableUI);
            cellInfo.setAuxObject(cellUI);
        }
        return cellUI;
    }

    public ItsNatTableUI getItsNatTableUI()
    {
        return tableUI;
    }

    public Element getRowElement()
    {
        return cellInfo.getRowElement();
    }

    public int getRowIndex()
    {
        return cellInfo.getRowIndex();
    }

    public Element getCellElement()
    {
        return cellInfo.getCellElement();
    }

    public int getColumnIndex()
    {
        return cellInfo.getColumnIndex();
    }

    public Element getCellContentElement()
    {
        return tableUI.getCellContentElementAt(getRowIndex(),getColumnIndex());
    }

    public boolean containsUserValueName(String name)
    {
        return cellInfo.containsUserValueName(name);
    }

    public Object getUserValue(String name)
    {
        return cellInfo.getUserValue(name);
    }

    public Object setUserValue(String name, Object value)
    {
        return cellInfo.setUserValue(name,value);
    }

    public Object removeUserValue(String name)
    {
        return cellInfo.removeUserValue(name);
    }

    public String[] getUserValueNames()
    {
        return cellInfo.getUserValueNames();
    }


}
