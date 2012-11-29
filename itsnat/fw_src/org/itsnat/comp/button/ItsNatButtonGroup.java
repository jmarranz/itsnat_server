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

package org.itsnat.comp.button;

import org.itsnat.comp.*;
import javax.swing.ButtonGroup;

/**
 * Represents a button group.
 *
 * <p>Button groups are currently used only by radio buttons ({@link org.itsnat.comp.button.toggle.ItsNatButtonRadio}).</p>
 *
 * <p>A button group has a name, this name is unique per document no other button group has this name.
 * The same is applied for the <code>javax.swing.ButtonGroup</code> associated to this group.</p>
 *
 * <p><code>ButtonGroup</code> objects are used to provide some support to the Swing
 * standard way to create groups (see <code>javax.swing.ButtonModel.setGroup(ButtonGroup)</code>),
 * but some methods like <code>ButtonGroup.add(AbstractButton)</code> have no sense in ItsNat,
 * use {@link #addButton(ItsNatComponent)} instead.
 * </p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatComponentManager#createItsNatButtonGroup()
 */
public interface ItsNatButtonGroup
{
    /**
     * Returns the name of this button group. This name is not shared (unique) per document.
     *
     * @return the button group name.
     * @see ItsNatComponentManager#getItsNatButtonGroup(String)
     */
    public String getName();

    /**
     * Returns the Swing <code>ButtonGroup</code> object of this button group. This object is not shared (unique) per document.
     *
     * @return the <code>ButtonGroup</code> object of this button group.
     * @see ItsNatComponentManager#getItsNatButtonGroup(javax.swing.ButtonGroup)
     */
    public ButtonGroup getButtonGroup();

    /**
     * Adds the specified button to this group.
     *
     * @param button the button to add.
     * @see #removeButton(ItsNatComponent)
     * @see org.itsnat.comp.button.toggle.ItsNatButtonRadio#setItsNatButtonGroup(ItsNatButtonGroup)
     */
    public void addButton(ItsNatComponent button);

    /**
     * Removes the specified button from this group.
     *
     * @param button the button to remove.
     * @see #addButton(ItsNatComponent)
     */
    public void removeButton(ItsNatComponent button);

    /**
     * Returns the number of buttons of this group.
     *
     * @return the number of buttons.
     */
    public int getButtonCount();

    /**
     * Returns the button at the specified position.
     *
     * @param index 0 based index of the button.
     * @return the button at the specified position.
     */
    public ItsNatComponent getButton(int index);
}
