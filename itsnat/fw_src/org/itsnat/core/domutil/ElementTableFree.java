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
 * Represents an integer indexed DOM Element table, row elements and cell elements can have different
 * tag names (the meaning of "free").
 *
 * <p>The interface inherits from {@link ElementListFree} and <code>java.util.List</code>
 * indirectly and supports both types of iterators. <code>List</code> and <code>Iterator</code> methods accept
 * and return DOM Element objects. They see the table as a list of rows.</p>
 *
 * <p>The table can work in master and slave modes, see
 * {@link ElementListFree#isMaster()}. If in master mode direct DOM
 * structural changes (add/remove/replace a row, column, cell) must be avoided.
 * </p>
 *
 * <p>In a "free" table the number of columns may not be the same between rows.</p>
 *
 * @see ElementGroupManager#createElementTableFree(Element,boolean)
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementTableFree extends ElementTableBase,ElementListFree
{
    /**
     * Adds a new row element at the end of the table.
     *
     * <p>If the new row element is already in the table a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is inserted. This avoids
     * an indirect delete by DOM.</p>
     *
     * @param elem the new row element.
     * @see #insertRowAt(int,org.w3c.dom.Element)
     */
    public void addRow(Element elem);

    /**
     * Inserts a new row element at the specified position.
     *
     * <p>If the new row element is already in the table a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is inserted. This avoids
     * an indirect delete by DOM.</p>
     *
     * @param row row index of the element.
     * @param elem the new element.
     * @see #addRow(Element)
     */
    public void insertRowAt(int row,Element elem);

    /**
     * Replaces the specified row element with a new one.
     *
     * <p>If the new row element is already in the list a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is used to replace the element in that
     * position. This avoids an indirect delete by DOM.</p>
     *
     * @param row row index of the element.
     * @param elem the new element.
     * @return the element replaced.
     * @see #insertRowAt(int,Element)
     * @see #getRowElementAt(int)
     */
    public Element setRowAt(int row,Element elem);

    /**
     * Clears the specified row and fills again with new elements. The number of columns of the row may change.
     *
     * <p>If a new cell element is already in the list a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is used to replace the element in that
     * position. This avoids an indirect delete by DOM.</p>
     *
     * @param cells new cell elements.
     * @return the cell elements replaced.
     * @see #getCellElementsOfRow(int)
     */
    public Element[] setCellElementsOfRow(int row,Element[] cells);


    /**
     * Replaces the specified cell element with a new one.
     *
     * <p>If the new cell element is already in the list a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is used to replace the element in that
     * position. This avoids an indirect delete by DOM.</p>
     *
     * @param row row index of the cell element.
     * @param column column index of the cell element.
     * @param elem the new element.
     * @return the element replaced.
     * @see #getCellElementAt(int,int)
     * @see #setRowAt(int,Element)
     */
    public Element setCellElementAt(int row, int column,Element elem);

    /**
     * Returns the cell elements of the specified row as an element list.
     * Modifications performed in this list affects to the original table.
     *
     * @param row    the row index.
     * @return the cell element list.
     */
    public ElementListFree getCellElementListOfRow(int row);

    /**
     * Adds a new column at the end of columns.
     *
     * <p>If a new cell element is already in the table a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is inserted. This avoids
     * an indirect delete by DOM.</p>
     *
     * @param cells the cells of the new column.
     * @see #insertColumnAt(int,org.w3c.dom.Element[])
     */
    public void addColumn(Element[] cells);

    /**
     * Inserts a new column at the specified position.
     *
     * <p>If a new cell element is already in the table a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is inserted. This avoids
     * an indirect delete by DOM.</p>
     *
     * @param column index of column to insert.
     * @param cells the cells of the new column.
     * @see #addColumn(Element[])
     */
    public void insertColumnAt(int column,Element[] cells);

    /**
     * Replaces the specified column with a new one.
     *
     * <p>If a new cell element is already in the list a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is used to replace the element in that
     * position. This avoids an indirect delete by DOM.</p>
     *
     * @param column index of column to replace.
     * @param cells the new cells of the column.
     * @return the cell elements replaced.
     * @see #insertColumnAt(int,Element[])
     * @see #getCellElementsOfColumn(int)
     */
    public Element[] setCellElementsOfColumn(int column,Element[] cells);

}
