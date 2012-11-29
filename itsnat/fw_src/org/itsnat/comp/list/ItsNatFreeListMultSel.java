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

import org.itsnat.comp.list.ItsNatListMultSel;
import org.itsnat.comp.*;

/**
 * Is the interface of the free multiple selection list components.
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * <p>There is no default decoration of item selection,
 * selection model listeners may be used to decorate the item markup when its
 * selection state changes.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createItsNatFreeListMultSel(org.w3c.dom.Element element,ItsNatListStructure,org.itsnat.core.NameValue[] artifacts)
 */
public interface ItsNatFreeListMultSel extends ItsNatFreeList,ItsNatListMultSel
{
    /**
     * Returns if the keyboard is used to select items.
     *
     * <p>If false ItsNat simulates the CTRL key is ever pressed
     * when an item is selected.
     * </p>
     * <p>This feature is useful for mobile devices without keyboard and tactile
     * screens to select multiple items (if set to false).
     * </p>
     * <p>Default value is defined by {@link org.itsnat.comp.ItsNatComponentManager#isSelectionOnComponentsUsesKeyboard()}
     * </p>
     *
     * @return the current state.
     * @see #setSelectionUsesKeyboard(boolean)
     */
    public boolean isSelectionUsesKeyboard();

    /**
     * Sets if the keyboard is used to select items.
     *
     * @param value the new state.
     * @see #isSelectionUsesKeyboard()
     */
    public void setSelectionUsesKeyboard(boolean value);
}
