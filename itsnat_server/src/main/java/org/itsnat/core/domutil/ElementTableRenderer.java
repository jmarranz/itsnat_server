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
 * Used by {@link ElementTable} objects to render the values associated
 * to cell elements.
 *
 *
 * @author Jose Maria Arranz Santamaria
 * @see ElementTable#setElementTableRenderer(ElementTableRenderer)
 * @see ElementGroupManager#createDefaultElementTableRenderer()
 */
public interface ElementTableRenderer
{
    /**
     * Renders as markup the specified value into the specified cell element.
     *
     * <p>The cell content element must be used to render below the value, usually
     * as a text node.</p>
     *
     * <p>Default implementation renders the specified value inside the first text node found below the
     * specified cell content element.</p>
     *
     *
     * @param table the target table.
     * @param row the row position.
     * @param col the column position.
     * @param value the value to render.
     * @param cellContentElem the cell content element. This element
     *    is a hint, if provided, should be obtained by calling {@link ElementTableStructure#getCellContentElement(ElementTable,int,int,Element)}.
     * @param isNew true if this is the first time the markup is rendered. Default implementation ignores this parameter.
     */
    public void renderTable(ElementTable table,int row,int col,Object value,Element cellContentElem,boolean isNew);

    /**
     * Unrenders the markup of the specified table cell. This method is called <i>before</i> the markup
     * is removed.
     *
     * <p>Default implementation does nothing.</p>
     *
     * @param table the target table.
     * @param row the row position.
     * @param col the column position.
     * @param cellContentElem the cell content element. This element
     *    is a hint, if provided, should be obtained by calling {@link ElementTableStructure#getCellContentElement(ElementTable,int,int,Element)}.
     */
    public void unrenderTable(ElementTable table,int row,int col,Element cellContentElem);
}
