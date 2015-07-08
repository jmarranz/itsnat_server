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
import org.w3c.dom.Node;

/**
 * This utility interface represents and manages an integer indexed DOM Element table, a row list
 * of consecutive elements with a single parent element, every row element contains again an element list (cells).
 *
 * <p>Objects implementing this interface are attached to a real DOM element table
 * with a single parent element, this "initial" table may be not empty,
 * in fact it is automatically synchronized with the "real" DOM element table
 * to show the current state when the utility object is created.</p>
 *
 * <p>This interface only manages DOM Element objects as row and cell elements, other node types like text nodes are ignored (filtered).</p>
 *
 * <p>Indexes are zero-based.</p>
 *
 * <p>When a DOM element (a new row or a new cell/column) is added or removed using this interface, this element
 * is added/removed to/from the DOM table too.</p>
 *
 * <p>This interface inherits from {@link ElementListBase}, the list interface
 * see the table as a list of rows.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementTableBase extends ElementListBase
{
    /**
     * Removes the specified row element.
     *
     * @param row index of the row element to remove.
     * @return the removed element or null if index is out of bounds.
     */
    public Element removeRowAt(int row);

    /**
     * Removes the row elements between the specified indexes.
     *
     * @param fromIndex low index (inclusive).
     * @param toIndex high index (inclusive).
     */
    public void removeRowRange(int fromIndex, int toIndex);

    /**
     * Removes all row elements. The table is now empty.
     */
    public void removeAllRows();

    /**
     *  Moves one or more row elements from the inclusive range <code>start</code> to
     *  <code>end</code> to the <code>to</code> position in the table.
     *  After the move, the row element that was at index <code>start</code>
     *  will be at index <code>to</code>.
     *
     * <p>The algorithm is explained in {@link ElementListBase#moveElement(int,int,int)}.</p>
     *
     * @param   start       the starting element index to be moved
     * @param   end         the ending element index to be moved
     * @param   to          the destination of the elements to be moved
     */
    public void moveRow(int start,int end,int to);

    /**
     * Returns the number of rows.
     *
     * @return the number of rows.
     */
    public int getRowCount();

    /**
     * Returns the row element list as an array.
     *
     * @return the element array.
     */
    public Element[] getElementRows();

    /**
     * Returns the row element at the specified index.
     *
     * @param row index of the row to search.
     * @return the row element in this position or null if index is out of range.
     */
    public Element getRowElementAt(int row);

    /**
     * Returns the first row element (row at position 0).
     *
     * @return the first row element or null is the table is empty.
     */
    public Element getFirstRowElement();

    /**
     * Returns the last row element (row at position length - 1).
     *
     * @return the last row element or null is the table is empty.
     */
    public Element getLastRowElement();

    /**
     * Returns the position of the specified row element.
     *
     * @param elem the element to search.
     * @return the position or -1 if the specified element is not in the row list.
     */
    public int indexOfRowElement(Element elem);

    /**
     * Returns the position of the specified row element searching backwards.
     *
     * <p>The result must be the same as {@link #indexOfRowElement(Element)} because
     * there is no "duplicated" elements. Use this method if the specified
     * element is near to the end of the list.</p>
     *
     * @param elem the element to search.
     * @return the element position or -1 if the specified element is not in the row list.
     */
    public int lastIndexOfRowElement(Element elem);

    /**
     * Returns the row element of the table containing the specified node. If the node
     * is itself a row element, self is returned.
     *
     *
     * @param node the node to search for.
     * @return the row element containing or equal the specified node. Null if this node is not contained by the table.
     * @see #indexOfRowElement(Element)
     * @see #getRowListElementInfoFromNode(Node)
     */
    public Element getRowElementFromNode(Node node);

    /**
     * Returns an object info of the row element at the specified position.
     *
     * @param index index of the row.
     * @return the object info of the specified row element. Null if index is out of range.
     * @see #getRowListElementInfoFromNode(Node)
     */
    public ListElementInfo getRowListElementInfoAt(int index);

    /**
     * Returns an object info of the row element containing the specified node (or the node
     * is itself a row element).
     *
     *
     * @param node the node to search for.
     * @return the object info of the matched row element. Null if this node is not contained by the table.
     * @see #getRowElementFromNode(Node)
     */
    public ListElementInfo getRowListElementInfoFromNode(Node node);

    /**
     * Returns an object info of the cell element containing the specified node (or the node
     * is itself a cell element of the table).
     *
     *
     * @param node the node to search for.
     * @return the object info of the matched cell element. Null if this node is not contained by the table.
     * @see #getTableCellElementInfoAt(int,int)
     */
    public TableCellElementInfo getTableCellElementInfoFromNode(Node node);

    /**
     * Returns an object info of the cell element specified by the row and column indexes.
     *
     * @param row row index of the cell element.
     * @param column column index of the cell element.
     * @return the object info of the matched cell element. Null if some index is out of range.
     * @see #getTableCellElementInfoFromNode(Node)
     */
    public TableCellElementInfo getTableCellElementInfoAt(int row,int column);

    /**
     * Returns the cell elements of the specified row as an array.
     *
     * @param row    the row index.
     * @return the cell element array or null if index is out of range.
     */
    public Element[] getCellElementsOfRow(int row);

    /**
     * Returns the cell element at the specified row and column.
     *
     * @param row    the row index of the cell element to search.
     * @param column the column index of the cell element to search.
     * @return the element in this position or null if some index is out of range.
     */
    public Element getCellElementAt(int row, int column);

    /**
     * Removes the specified column.
     *
     * @param column index of the column to remove.
     */
    public void removeColumnAt(int column);

    /**
     * Removes all columns. The table remains as a row list with no cells.
     */
    public void removeAllColumns();

    /**
     * Moves the column at <code>columnIndex</code> to
     * <code>newIndex</code>.  The old column at <code>columnIndex</code>
     * will now be found at <code>newIndex</code>.  The column
     * that used to be at <code>newIndex</code> is shifted
     * left or right to make room.  This will not move any columns if
     * <code>columnIndex</code> equals <code>newIndex</code>.
     *
     * @param columnIndex the index of the column to be moved
     * @param newIndex    new index to move the column
     */
    public void moveColumn(int columnIndex,int newIndex);

    /**
     * Returns the cell elements of the specified column as an array.
     *
     * @param column    the column index.
     * @return the cell element array or null if index is out of range..
     */
    public Element[] getCellElementsOfColumn(int column);
}
