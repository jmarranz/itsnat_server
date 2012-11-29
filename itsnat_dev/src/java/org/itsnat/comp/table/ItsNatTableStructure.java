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

import org.w3c.dom.Element;

/**
 * Used by table components to locate the required elements of the table layout.
 *
 * <p>Default implementation respects the normal layout of HTML &lt;table&gt; structures,
 * if no HTML table is involved (free table) then is expected a similar layout than in an HTML table.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createDefaultItsNatTableStructure()
 * @see ItsNatTable#getItsNatTableStructure()
 * @see org.itsnat.core.domutil.ElementTableStructure
 */
public interface ItsNatTableStructure
{
    /**
     * Returns the table head element.
     *
     * <p>Default implementation returns (if exists) the &lt;thead&gt; element if the
     * table element is a &lt;table&gt;, else (free table) returns the first child
     * element if the table contains more than one child element, else returns null (no head).
     * </p>
     *
     * @param table the table component, may be used to provide contextual information. Default implementation ignores it.
     * @param tableElem the parent element of the table.
     *          Is a hint, if provided should be obtained by calling <code>table.getElement()</code>.
     * @return the head element. May be null.
     */
    public Element getHeadElement(ItsNatTable table,Element tableElem);

    /**
     * Returns the table body element.
     *
     * <p>Default implementation returns (if exists) the first &lt;tbody&gt; element if the
     * table element is a &lt;table&gt;, else (free table) returns the second child
     * element if the table contains more than one child element, else
     * returns the table element (the one child element is considered a row pattern). Never is null.
     * </p>
     *
     * @param table the table component, may be used to provide contextual information. Default implementation ignores it.
     * @param tableElem the parent element of the table.
     *          Is a hint, if provided should be obtained by calling <code>table.getElement()</code>.
     * @return the body element.
     */
    public Element getBodyElement(ItsNatTable table,Element tableElem);


    /**
     * Returns the content element of a table row. This element is used as the parent
     * of table cell elements.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementTableStructure}.</p>
     *
     * @param table the table component, may be used to provide contextual information. Default implementation ignores it.
     * @param row index of the row.
     * @param rowElem the element containing the row (row element).
     *      This element is a hint, if provided, should be the same as returned by <code>table.getItsNatTableUI().getRowElementAt(row)</code>.
     * @return the row content element.
     */
    public Element getRowContentElement(ItsNatTable table,int row,Element rowElem);

    /**
     * Returns the content element of a table cell. This element is used to render
     * the cell value.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementTableStructure}.</p>
     *
     * @param table the table component, may be used to provide contextual information. Default implementation ignores it.
     * @param row index of the row.
     * @param col index of the column.
     * @param cellElem the element containing the cell (cell element).
     *      This element is a hint, if provided, should be the same as returned by <code>table.getItsNatTableUI().getCellElementAt(row,col)</code>.
     * @return the cell content element.
     * @see ItsNatTableCellRenderer
     */
    public Element getCellContentElement(ItsNatTable table,int row,int col,Element cellElem);

    /**
     * Returns the content element of a table header column. This element is used to render
     * header column.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementListStructure}.</p>
     *
     * @param tableHeader the table header component, may be used to provide contextual information. Default implementation ignores it.
     * @param column the column index.
     * @param itemElem the element containing the column markup in this position.
     *          Is a hint, if provided should be obtained by calling <code>tableHeader.getItsNatTableHeaderUI().getColumnElementAt(index)</code>.
     * @return the content element.
     * @see ItsNatTableHeaderCellRenderer
     */
    public Element getHeaderColumnContentElement(ItsNatTableHeader tableHeader,int column,Element itemElem);

}
