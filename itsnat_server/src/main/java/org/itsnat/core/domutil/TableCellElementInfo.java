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

package org.itsnat.core.domutil;

import org.itsnat.core.ItsNatUserData;
import org.w3c.dom.Element;

/**
 * Contains the DOM Elements, row and cell, and the row and column indexes
 * of a cell of a DOM element table.
 *
 * <p>If the parent table (which this object belongs to) is master, this object is ever the
 * same instance per table cell element, supporting row and column changes (inserting or removing
 * rows and columns) and DOM element changes (calling {@link ElementTableFree#setCellElementAt(int,int,org.w3c.dom.Element)} ).
 * This feature is very interesting to save contextual
 * data associated to the cell element (using {@link ItsNatUserData}).
 * </p>
 *
 * <p>If the parent table is slave every object obtained is a new instance, {@link ItsNatUserData}
 * can not be used.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ElementTableBase#getTableCellElementInfoFromNode(Node)
 * @see ElementTableBase#getTableCellElementInfoAt(int,int)
 */
public interface TableCellElementInfo extends ItsNatUserData
{
    /**
     * Returns the parent table this object belongs to.
     *
     * @return the parent table.
     */
    public ElementTableBase getParentTable();

    /**
     * Returns the row DOM element (top) parent of the cell.
     *
     * @return the row element.
     */
    public Element getRowElement();

    /**
     * Returns the row index of the cell.
     *
     * @return the row index.
     */
    public int getRowIndex();

    /**
     * Returns the cell DOM element.
     *
     * @return the cell element.
     */
    public Element getCellElement();

    /**
     * Returns the column index of the cell.
     *
     * @return the column index.
     */
    public int getColumnIndex();
}
