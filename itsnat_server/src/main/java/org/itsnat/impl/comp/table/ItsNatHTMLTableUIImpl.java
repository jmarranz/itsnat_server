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

import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.table.ItsNatHTMLTableHeaderUI;
import org.itsnat.comp.table.ItsNatHTMLTableUI;
import org.itsnat.impl.comp.table.ItsNatHTMLTableImpl;
import org.itsnat.impl.comp.table.ItsNatTableUIImpl;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLTableUIImpl extends ItsNatTableUIImpl implements ItsNatHTMLTableUI
{

    /**
     * Creates a new instance of ItsNatHTMLTableUIImpl
     */
    public ItsNatHTMLTableUIImpl(ItsNatHTMLTableImpl parentComp)
    {
        super(parentComp);
    }

    public ItsNatHTMLTableHeaderUI getItsNatHTMLTableHeaderUI()
    {
        return (ItsNatHTMLTableHeaderUI)getItsNatTableHeaderUI();
    }

    public ItsNatHTMLTable getItsNatHTMLTable()
    {
        return (ItsNatHTMLTable)parentComp;
    }

    public HTMLTableElement getHTMLTableElement()
    {
        return getItsNatHTMLTable().getHTMLTableElement();
    }

    public HTMLTableSectionElement getHTMLTableSectionElement()
    {
        return (HTMLTableSectionElement)getBodyElement(); // El <tbody>
    }

    public HTMLTableRowElement getHTMLTableRowElementAt(int row)
    {
        return (HTMLTableRowElement)getRowElementAt(row);
    }

    public HTMLTableCellElement[] getHTMLTableCellElementsOfRow(int row)
    {
        return (HTMLTableCellElement[])getCellElementsOfRow(row);
    }

    public HTMLTableCellElement[] getHTMLTableCellElementsOfColumn(int column)
    {
        return (HTMLTableCellElement[])getCellElementsOfColumn(column);
    }

    public HTMLTableCellElement getHTMLTableCellElementAt(int row,int column)
    {
        return (HTMLTableCellElement)getCellElementAt(row,column);
    }

    public ItsNatHTMLElementComponent getItsNatHTMLElementComponent()
    {
        return (ItsNatHTMLElementComponent)parentComp;
    }
}
