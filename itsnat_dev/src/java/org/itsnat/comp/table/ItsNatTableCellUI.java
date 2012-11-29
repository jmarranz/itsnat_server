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

import org.itsnat.core.ItsNatUserData;
import org.w3c.dom.Element;

/**
 * Contains visual information of a table cell element.
 *
 * <p>This interface is the "componentized" version of a master
 * {@link org.itsnat.core.domutil.TableCellElementInfo} and
 * follows a similar philosophy.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatTableUI#getItsNatTableCellUIAt(int,int)
 * @see ItsNatTableUI#getItsNatTableCellUIFromNode(org.w3c.dom.Node)
 */
public interface ItsNatTableCellUI extends ItsNatUserData
{
    /**
     * Returns the parent table UI this object belongs to.
     *
     * @return the parent table UI.
     */
    public ItsNatTableUI getItsNatTableUI();

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

    /**
     * Returns the content element of this table cell.
     *
     * <p>Current implementation delegates to {@link ItsNatTableUI#getCellContentElementAt(int,int)}.</p>
     *
     * @return the content element of this table cell.
     */
    public Element getCellContentElement();
}
