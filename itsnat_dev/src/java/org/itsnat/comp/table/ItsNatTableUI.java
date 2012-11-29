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

import org.itsnat.comp.ItsNatElementComponentUI;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Is the base interface of the User Interface of a table body component.
 *
 * <p>Table cells are managed as a DOM element table, using the current table structure
 * and renderer.</p>
 *
 * <p>Current implementation does not use the data model and relays heavily on
 * {@link org.itsnat.core.domutil.ElementTable}.</p>
 *
 * <p>Rows and column indexes are 0 based.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatTable#getItsNatTableUI()
 * @see ItsNatTable#getItsNatTableCellRenderer()
 * @see ItsNatTable#getItsNatTableStructure()
 * @see ItsNatTableHeaderUI
 */
public interface ItsNatTableUI extends ItsNatElementComponentUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatTable getItsNatTable();

    /**
     * Returns the header user interface manager of this component.
     *
     * @return the header user interface manager. Null if this table has not a header.
     */
    public ItsNatTableHeaderUI getItsNatTableHeaderUI();

    /**
     * Returns the number of rows.
     *
     * @return the number of rows.
     * @see org.itsnat.core.domutil.ElementTableBase#getRowCount()
     */
    public int getRowCount();

    /**
     * Returns the number of columns.
     *
     * @return the number of columns.
     * @see org.itsnat.core.domutil.ElementTable#getColumnCount()
     */
    public int getColumnCount();


    /**
     * Returns an object info of the cell element at the specified row and column.
     *
     * @param row row index of the cell element.
     * @param column column index of the cell element.
     * @return the object info of the matched table cell element. Null if row or column is out of range.
     *          This object is ever the same per cell and may be used to save any context data.
     * @see #getItsNatTableCellUIFromNode(Node)
     * @see org.itsnat.core.domutil.ElementTableBase#getTableCellElementInfoAt(int,int)
     */
    public ItsNatTableCellUI getItsNatTableCellUIAt(int row,int column);

    /**
     * Returns an object info of the cell element containing the specified node (or the node
     * is itself an element of the table body).
     *
     * @param node the node to search for.
     * @return the object info of the matched child element. Null if this node is not contained by the table.
     *          This object is ever the same per cell and may be used to save any context data.
     * @see #getItsNatTableCellUIAt(int,int)
     * @see org.itsnat.core.domutil.ElementTableBase#getTableCellElementInfoFromNode(Node)
     */
    public ItsNatTableCellUI getItsNatTableCellUIFromNode(Node node);

    /**
     * Returns the element used as parent of the table body.
     * In an HTML &lt;table&gt; returns the &lt;tbody&gt;
     *
     * @return the body parent element.
     */
    public Element getBodyElement();

    /**
     * Returns the row element at the specified index.
     *
     * @param row index of the row element to search.
     * @return the row element in this position or null if index is out of range.
     * @see org.itsnat.core.domutil.ElementTableBase#getRowElementAt(int)
     */
    public Element getRowElementAt(int row);

    /**
     * Returns the content row element. This element is obtained
     * using the current structure.
     *
     * @param row index of the row element.
     * @return the content element.
     * @see ItsNatTableStructure#getRowContentElement(ItsNatTable,int,Element)
     * @see org.itsnat.core.domutil.ElementTable#getRowContentElementAt(int)
     */
    public Element getRowContentElementAt(int row);

    /**
     * Returns the cell elements of the specified row as an array.
     *
     * @param row the row index.
     * @return the cell element array or null if index is out of range.
     * @see org.itsnat.core.domutil.ElementTableBase#getCellElementsOfRow(int)
     */
    public Element[] getCellElementsOfRow(int row);

    /**
     * Returns the cell elements of the specified column as an array.
     *
     * @param column the column index.
     * @return the cell element array or null if index is out of range.
     * @see org.itsnat.core.domutil.ElementTableBase#getCellElementsOfColumn(int)
     */
    public Element[] getCellElementsOfColumn(int column);

    /**
     * Returns the cell element at the specified row and column.
     *
     * @param row the row index of the cell element to search.
     * @param column the column index of the cell element to search.
     * @return the element in this position or null if some index is out of range.
     * @see org.itsnat.core.domutil.ElementTableBase#getCellElementAt(int,int)
     */
    public Element getCellElementAt(int row,int column);

    /**
     * Returns the content element of the cell. This element is obtained
     * using the current structure.
     *
     * @param row index of the row.
     * @return the content element of the row.
     * @see org.itsnat.core.domutil.ElementTable#getCellContentElementAt(int,int)
     */
    public Element getCellContentElementAt(int row,int column);

    /**
     * Informs whether the original (saved as pattern) markup is used to render.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatDocument#isUsePatternMarkupToRender()}</p>
     *
     * @return true if the original markup is used.
     * @see #setUsePatternMarkupToRender(boolean)
     */
    public boolean isUsePatternMarkupToRender();

    /**
     * Sets whether the original (saved as pattern) markup is used to render.
     *
     * @param value true to enable the use of original markup to render.
     * @see #isUsePatternMarkupToRender()
     */
    public void setUsePatternMarkupToRender(boolean value);
}

