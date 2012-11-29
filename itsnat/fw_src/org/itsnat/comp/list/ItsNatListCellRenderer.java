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

package org.itsnat.comp.list;

import org.w3c.dom.Element;

/**
 * Is used to render list item values as markup into DOM elements.
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createDefaultItsNatListCellRenderer()
 * @see ItsNatList#getItsNatListCellRenderer()
 * @see ItsNatList#setItsNatListCellRenderer(ItsNatListCellRenderer)
 */
public interface ItsNatListCellRenderer
{
    /**
     * Renders as markup the specified list item value into the specified element, usually
     * as a text node.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementRenderer}.</p>
     *
     * <p>Default implementation ignores <code>isSelected</code> and <code>hasFocus</code>
     * (current implementation of ItsNat lists does not handle focus on list items, ever is false).</p>
     *
     * @param list the list component, may be used to provide contextual information. Default implementation ignores it.
     * @param index the list item index.
     * @param value the value to render.
     * @param isSelected true if the list item is selected.
     * @param hasFocus true if the list item has the focus.
     * @param cellContentElem the list item content element to render the value into. Is a hint, if provided should be obtained by calling <code>list.getItsNatListUI().getContentElementAt(index)</code>.
     * @param isNew true if this is the first time the markup is rendered. Default implementation ignores this parameter.
     */
    public void renderListCell(
                ItsNatList list,
                int index,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                Element cellContentElem,
                boolean isNew);

    /**
     * Unrenders the markup of the specified list item. This method is called <i>before</i> the markup
     * is removed.
     *
     * <p>Default implementation does nothing.</p>
     *
     * @param list the list component, may be used to provide contextual information. Default implementation ignores it.
     * @param index the list item index.
     * @param cellContentElem the list item content element to render the value into. Is a hint, if provided should be obtained by calling <code>list.getItsNatListUI().getContentElementAt(index)</code>.
     */
    public void unrenderListCell(ItsNatList list,
                int index,
                Element cellContentElem);
}
