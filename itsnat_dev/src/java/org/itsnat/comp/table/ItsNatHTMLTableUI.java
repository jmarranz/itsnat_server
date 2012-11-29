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

package org.itsnat.comp.table;

import org.itsnat.comp.ItsNatHTMLElementComponentUI;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 * Is the base interface of the User Interface of an HTML table body component.
 *
 * <p>This interface basically provides casts of the base methods.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHTMLTableUI extends ItsNatHTMLElementComponentUI,ItsNatTableUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatHTMLTable getItsNatHTMLTable();

    /**
     * Returns the header user interface manager of this component.
     *
     * @return the header user interface manager. Null if this table has not a header.
     */
    public ItsNatHTMLTableHeaderUI getItsNatHTMLTableHeaderUI();

    /**
     * Returns the &lt;tbody&gt; element.
     *
     * @return the &lt;tbody&gt; element.
     * @see ItsNatTableUI#getBodyElement()
     */
    public HTMLTableSectionElement getHTMLTableSectionElement();

    /**
     * Returns the &lt;row&gt; element at the specified index.
     *
     * @param row index of the row element to search.
     * @return the row element in this position or null if index is out of range.
     * @see ItsNatTableUI#getRowElementAt(int)
     */
    public HTMLTableRowElement getHTMLTableRowElementAt(int row);

    /**
     * Returns the &lt;td&gt; elements of the specified row as an array.
     *
     * @param row the row index.
     * @return the cell element array or null if index is out of range.
     * @see ItsNatTableUI#getCellElementsOfRow(int)
     */
    public HTMLTableCellElement[] getHTMLTableCellElementsOfRow(int row);

    /**
     * Returns the &lt;td&gt; elements of the specified column as an array.
     *
     * @param column the column index.
     * @return the cell element array or null if index is out of range.
     * @see ItsNatTableUI#getCellElementsOfColumn(int)
     */
    public HTMLTableCellElement[] getHTMLTableCellElementsOfColumn(int column);

    /**
     * Returns the &lt;td&gt; element at the specified row and column.
     *
     * @param row the row index of the cell element to search.
     * @param column the column index of the cell element to search.
     * @return the element in this position or null if some index is out of range.
     * @see ItsNatTableUI#getCellElementAt(int,int)
     */
    public HTMLTableCellElement getHTMLTableCellElementAt(int row,int column);
}
