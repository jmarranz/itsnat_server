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
 * Is used to render column values of table headers as markup into DOM elements.
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createDefaultItsNatTableHeaderCellRenderer()
 * @see ItsNatTableHeader#getItsNatTableHeaderCellRenderer()
 * @see ItsNatTableHeader#setItsNatTableHeaderCellRenderer(ItsNatTableHeaderCellRenderer)
 */
public interface ItsNatTableHeaderCellRenderer
{
    /**
     * Renders as markup the specified column value of the table header into the specified element, usually
     * as a text node.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementRenderer}.</p>
     *
     * <p>Default implementation ignores <code>isSelected</code> and <code>hasFocus</code>
     * (current implementation of ItsNat tables does not handle focus on table header columns, ever is false).</p>
     *
     * @param tableHeader the table header component, may be used to provide contextual information. Default implementation ignores it.
     * @param column the column index.
     * @param value the value to render.
     * @param isSelected true if the column is selected.
     * @param hasFocus true if the column has the focus.
     * @param columnContentElem the table column content element to render the value into.
     *          Is a hint, if provided should be obtained by calling <code>tableHeader.getItsNatTableHeaderUI().getColumnContentElementAt(column)</code>.
     * @param isNew true if this is the first time the markup is rendered. Default implementation ignores this parameter.
     */
    public void renderTableHeaderCell(
                    ItsNatTableHeader tableHeader,
                    int column,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    Element columnContentElem,
                    boolean isNew);

    /**
     * Unrenders the markup of the specified header column. This method is called <i>before</i> the markup
     * is removed.
     *
     * <p>Default implementation does nothing.</p>
     *
     * @param tableHeader the table header component, may be used to provide contextual information. Default implementation ignores it.
     * @param column the column index.
     * @param columnContentElem the table column content element to render the value into.
     *          Is a hint, if provided should be obtained by calling <code>tableHeader.getItsNatTableHeaderUI().getColumnContentElementAt(column)</code>.
     */
    public void unrenderTableHeaderCell(
                    ItsNatTableHeader tableHeader,
                    int column,
                    Element columnContentElem);
}
