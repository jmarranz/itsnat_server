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
 * Used by list components to obtain the content parent element of
 * a item element of the list.
 *
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createDefaultItsNatListStructure()
 * @see ItsNatList#getItsNatListStructure()
 * @see org.itsnat.core.domutil.ElementListStructure
 */
public interface ItsNatListStructure
{
    /**
     * Returns the content element of an item element. This element is used to render
     * or edit the item value.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementListStructure}.</p>
     *
     * @param list the list component, may be used to provide contextual information. Default implementation ignores it.
     * @param index the list item index.
     * @param itemElem the element containing the list item markup in this position.
     *          Is a hint, if provided should be obtained by calling <code>list.getItsNatListUI().getElementAt(index)</code>.
     * @return the content element.
     * @see ItsNatListCellRenderer
     * @see ItsNatListCellEditor
     */
    public Element getContentElement(ItsNatList list,int index,Element itemElem);
}
