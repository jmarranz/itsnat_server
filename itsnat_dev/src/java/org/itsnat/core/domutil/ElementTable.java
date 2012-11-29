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

import java.util.List;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 * Manages a pattern based DOM Element table.
 *
 * <p>The generic table structure is:</p>
 *
 * <pre>
    &lt;tableParent>
        &lt;row>
            ...
            &lt;optRowContent>
                &lt;cell>
                    &lt;opt1>...&lt;optN>Pattern&lt;/optN>...&lt;/opt1>
                &lt;/cell>
                ...
            &lt;/optRowContent>
            ...
        &lt;/row>
        ...
    &lt;/tableParent>
 * </pre>
 *
 * <p>The starting point usually is a DOM table with a single row containing one or several cells
 * this row is save as the pattern (really a deep clone) and used when new rows are added, the first cell
 * of the pattern is used to create new cells (for instance a new column).
 * The initial DOM table (including the row pattern) may be initially cleared
 * or kept as is when this object is created and attached to the underlying DOM table.</p>
 *
 * <p>This type of table helps to render a table of values into the DOM element table,
 * for instance, this interface support "out the box" the typical DOM element table where
 * every cell element contains some value usually as the data of a text node.
 * Methods to add new cells include optionally a <code>value</code> parameter.
 * The structure and renderer objects are used to customize how and where this value is saved in the table
 * beyond the default cases.</p>
 *
 * <p>Columns can be added or removed, if a column is removed/added the same column
 * is removed/added to the row pattern too (in fact the table may be empty, no rows, but {@link #getColumnCount()}
 * may be non-zero), the cell pattern is used to add new cells to the row pattern
 * if necessary. All rows have the same number of columns.</p>
 *
 * <p>By default a just created table has one row if the row pattern is not removed
 * or zero rows if removed and as many columns as declared in the row pattern.</p>
 *
 * <p>A pattern based DOM Element table ever works in "master" mode {@link ElementListFree#isMaster()}
 *
 * @see ElementGroupManager#createElementTable(Element,boolean)
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementTable extends ElementTableBase
{
    /**
     * Returns the current structure used by this table.
     *
     * @return the current structure.
     * @see #setElementTableStructure(ElementTableStructure)
     */
    public ElementTableStructure getElementTableStructure();

    /**
     * Sets the structure used by this table.
     *
     * @param structure the new structure.
     * @see #getElementTableStructure()
     */
    public void setElementTableStructure(ElementTableStructure structure);

    /**
     * Returns the current renderer used by this table.
     *
     * @return the current renderer.
     * @see #setElementTableRenderer(ElementTableRenderer)
     */
    public ElementTableRenderer getElementTableRenderer();

    /**
     * Sets the renderer used by this table.
     *
     * @param renderer the new renderer.
     * @see #getElementTableRenderer()
     */
    public void setElementTableRenderer(ElementTableRenderer renderer);

    /**
     * Returns the element used as a row pattern. This element may be a clone of the
     * original first row used as a pattern.
     *
     * @return the row pattern element.
     */
    public Element getRowPatternElement();

    /**
     * Returns the element used as a cell pattern. This element may be a clone of the
     * original first cell used as a pattern.
     *
     * @return the cell pattern element.
     */
    public Element getCellPatternElement();

    /**
     * Increases or shrinks the number of rows to fit the new size.
     *
     * <p>If the new size is bigger new rows are added at the end, if the size
     * is lower tail rows are removed.</p>
     *
     * @param rowCount the new number of rows.
     * @see #getRowCount()
     * @see #addRow()
     * @see #removeRowAt(int)
     */
    public void setRowCount(int rowCount);

    /**
     * Adds a new row element at the end of the table using the row pattern (the new row is a clone).
     *
     * @return the new row element.
     * @see #addRow(Object[])
     */
    public Element addRow();

    /**
     * Adds a new row element at the end of the table using the row pattern, and renders the specified values using
     * the current structure and renderer.
     *
     * @param rowData the row values to render.
     * @return the new row element.
     * @see #addRow()
     * @see #getElementTableStructure()
     * @see #getElementTableRenderer()
     * @see ElementTableStructure
     * @see ElementTableRenderer#renderTable(ElementTable,int,int,Object,Element,boolean)
     */
    public Element addRow(Object[] rowData);

    /**
     * Adds a new row element at the end of the table using the row pattern, and renders the specified values using
     * the current structure and renderer.
     *
     * @param rowData the row values to render.
     * @return the new row element.
     * @see #addRow(Object[])
     */
    public Element addRow(List rowData);

    /**
     * Inserts a new row element at the specified position using the row pattern.
     *
     * @param row index of the new row.
     * @return the new row element.
     * @see #insertRowAt(int,Object[])
     */
    public Element insertRowAt(int row);

    /**
     * Inserts a new row element at the specified position using the row pattern, and renders the specified row values using
     * the current structure and renderer.
     *
     * @param row index of the new row.
     * @param rowData the row values to render.
     * @return the new row element.
     * @see #insertRowAt(int)
     * @see #getElementTableStructure()
     * @see #getElementTableRenderer()
     * @see ElementTableStructure
     * @see ElementTableRenderer#renderTable(ElementTable,int,int,Object,Element,boolean)
     */
    public Element insertRowAt(int row,Object[] rowData);

    /**
     * Inserts a new row element at the specified position using the row pattern and renders the specified row values using
     * the current structure and renderer.
     *
     * @param row index of the new row.
     * @param rowData the row values to render.
     * @return the new row element.
     * @see #insertRowAt(int,Object[])
     */
    public Element insertRowAt(int row,List rowData);

    /**
     * Renders the specified values into the row with the given position
     * using the current structure and renderer.
     *
     * @param row index of the row.
     * @param rowData the row values to render.
     * @see #insertRowAt(int,Object[])
     * @see #getElementTableStructure()
     * @see #getElementTableRenderer()
     * @see ElementTableStructure
     * @see ElementTableRenderer#renderTable(ElementTable,int,int,Object,Element,boolean)
     */
    public void setRowValuesAt(int row,Object[] rowData);

    /**
     * Renders the specified values into the row with the given position
     * using the current structure and renderer.
     *
     * @param row index of the row.
     * @param rowData the row values to render.
     * @see #setRowValuesAt(int,Object[])
     */
    public void setRowValuesAt(int row,List rowData);

    /**
     * Returns the "content" element of the row, this element is the parent of the row cells
     * This element is obtained using the current structure.
     *
     * @param row index of the row.
     * @return the content element of the row.
     * @see #getElementTableStructure()
     * @see ElementTableStructure#getRowContentElement(ElementTable,int,Element)
     */
    public Element getRowContentElementAt(int row);

    /**
     * Returns the "content" element of the cell, this element is used to render below
     * the associated value of the cell. This element is obtained
     * using the current structure.
     *
     * @param row index of the row.
     * @return the content element of the row.
     * @see #getElementTableStructure()
     * @see ElementTableStructure#getCellContentElement(ElementTable,int,int,Element)
     */
    public Element getCellContentElementAt(int row, int column);

    /**
     * Adds a new column at the end of columns using the cell pattern.
     *
     * @return the new cell elements.
     * @see #addColumn(Object[])
     */
    public Element[] addColumn();


    /**
     * Adds a new column at the end of columns using the cell pattern, and renders the specified values using
     * the current structure and renderer.
     *
     * @param columnData the column values to render.
     * @return the new cell elements.
     * @see #addColumn()
     * @see #getElementTableStructure()
     * @see #getElementTableRenderer()
     * @see ElementTableStructure
     * @see ElementTableRenderer#renderTable(ElementTable,int,int,Object,Element,boolean)
     */
    public Element[] addColumn(Object[] columnData);

    /**
     * Adds a new column at the end of columns using the cell pattern, and renders the specified values using
     * the current structure and renderer.
     *
     * @param columnData the column values to render.
     * @return the new cell elements.
     * @see #addColumn(Object[])
     */
    public Element[] addColumn(List columnData);

    /**
     * Inserts a new column at the specified position using the cell pattern.
     *
     * @param column index of the new column.
     * @return the new cell elements.
     * @see #insertColumnAt(int,Object[])
     */
    public Element[] insertColumnAt(int column);

    /**
     * Inserts a new column at the specified position using the cell pattern,
     * and renders the specified values using
     * the current structure and renderer.
     *
     * @param column index of the new column.
     * @param columnData the column values to render.
     * @return the new cell elements.
     * @see #insertColumnAt(int)
     * @see #getElementTableStructure()
     * @see #getElementTableRenderer()
     * @see ElementTableStructure
     * @see ElementTableRenderer#renderTable(ElementTable,int,int,Object,Element,boolean)
     */
    public Element[] insertColumnAt(int column,Object[] columnData);

    /**
     * Inserts a new column at the specified position using the cell pattern,
     * and renders the specified values using
     * the current structure and renderer.
     *
     * @param column index of the new column.
     * @param columnData the column values to render.
     * @return the new cell elements.
     * @see #insertColumnAt(int,Object[])
     */
    public Element[] insertColumnAt(int column,List columnData);

    /**
     * Renders the specified values into the column with the given position
     * using the current structure and renderer.
     *
     * @param column index of the column.
     * @param columnData the column values to render.
     * @see #insertColumnAt(int,Object[])
     * @see #getElementTableStructure()
     * @see #getElementTableRenderer()
     * @see ElementTableStructure
     * @see ElementTableRenderer#renderTable(ElementTable,int,int,Object,Element,boolean)
     */
    public void setColumnValuesAt(int column,Object[] columnData);

    /**
     * Renders the specified values into the column with the given position
     * using the current structure and renderer.
     *
     * @param column index of the column.
     * @param columnData the column values to render.
     * @see #setColumnValuesAt(int,Object[])
     */
    public void setColumnValuesAt(int column,List columnData);

    /**
     * Returns the number of columns.
     *
     * @see #setColumnCount(int)
     * @return the number of columns.
     */
    public int getColumnCount();

    /**
     * Increases or shrinks the number of columns to fit the new size.
     *
     * <p>If the new size is bigger new columns are added at the end, if the size
     * is lower tail columns are removed.</p>
     *
     * @param columnCount the new number of columns.
     * @see #getColumnCount()
     * @see #addColumn()
     * @see #removeColumnAt(int)
     */
    public void setColumnCount(int columnCount);

    /**
     * Renders the specified value into the cell element with the given row and column position
     * using the current structure and renderer.
     *
     * @param row row of the cell.
     * @param column column of the cell.
     * @param value the value to render.
     * @see #getElementTableStructure()
     * @see #getElementTableRenderer()
     * @see ElementTableStructure
     * @see ElementTableRenderer#renderTable(ElementTable,int,int,Object,Element,boolean)
     */
    public void setCellValueAt(int row, int column,Object value);

    /**
     * Renders all table cells with new values
     * using the current structure and renderer.
     *
     * @param values the values to render.
     * @see #getElementTableStructure()
     * @see #getElementTableRenderer()
     * @see ElementTableStructure
     * @see ElementTableRenderer#renderTable(ElementTable,int,int,Object,Element,boolean)
     */
    public void setTableValues(Object[][] values);


    /**
     * Renders all table cells with new values
     * using the current structure and renderer.
     *
     * @param values the values to render.
     * @see #setTableValues(Object[][])
     */
    public void setTableValues(List values);

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

    /**
     * Returns the pattern used to render values if {@link #isUsePatternMarkupToRender()}
     * is true.
     *
     * @return the pattern used to render values.
     */
    public DocumentFragment getCellContentPatternFragment();
}
