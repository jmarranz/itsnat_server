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

import org.w3c.dom.Element;

/**
 * Used by {@link ElementTable} objects to obtain row and cell content elements.
 *
 * @see ElementTable#setElementTableStructure(ElementTableStructure)
 * @see ElementGroupManager#createDefaultElementTableStructure()
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementTableStructure
{
    /**
     * Returns the content element of a row.
     *
     * <p>A row content element is the direct parent element of the row cells.
     * This element is used to access the row cells.</p>
     *
     * @param table the target table, may be used to provide contextual information. Default implementation ignores it.
     * @param row index of the row.
     * @param rowElem the element containing the row (row element).
     *      This element is a hint, if provided, should be the same as returned by <code>table.getRowElementAt(index)</code>.
     * @return the row content element. Default implementation returns <code>rowElem</code>.
     * @see ElementTableRenderer#renderTable(ElementTable,int,int,Object,Element,boolean)
     */
    public Element getRowContentElement(ElementTable table,int row,Element rowElem);

    /**
     * Returns the content element of a cell.
     *
     * <p>The cell content element is the parent element containing the markup of the associated
     * value usually a text node. This element is passed to the renderer method
     * {@link ElementTableRenderer#renderTable(ElementTable,int,int,Object,Element,boolean)}.</p>
     *
     * @param table the target table, may be used to provide contextual information. Default implementation ignores it.
     * @param row index of the row.
     * @param col index of the column.
     * @param cellElem the element containing the cell (cell element).
     *      This element is a hint, if provided, should be the same as returned by <code>table.getCellElementAt(row,col)</code>.
     * @return the cell content element. Default implementation returns <code>cellElem</code>.
     */
    public Element getCellContentElement(ElementTable table,int row,int col,Element cellElem);
}
