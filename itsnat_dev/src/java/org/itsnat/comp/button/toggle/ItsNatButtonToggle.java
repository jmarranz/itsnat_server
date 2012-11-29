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

package org.itsnat.comp.button.toggle;

import org.itsnat.comp.button.ItsNatButton;
import javax.swing.JToggleButton.ToggleButtonModel;

/**
 * Is the base interface of toggle button components. Toggle buttons can be selected.
 *
 * <p>By default this component type uses a <code>javax.swing.JToggleButton.ToggleButtonModel</code>
 * button model.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatButtonToggle extends ItsNatButton
{
    /**
     * Returns the current data model of this component.
     *
     * @return the current data model
     * @see #setToggleButtonModel(javax.swing.JToggleButton.ToggleButtonModel)
     */
    public ToggleButtonModel getToggleButtonModel();

    /**
     * Changes the data model of this component.
     *
     * <p>Current data model is disconnected from this component, and the new
     * data model is bound to this component, every change is tracked and
     * updates the user interfaces accordingly.</p>
     *
     * <p>If the specified data model is the same instance as the current data model,
     * then is reset, component listener is removed and added again. Use this technique if
     * you want to add a data model listener to be executed <i>before</i> the default component listener.
     *
     * @param dataModel the new data model.
     * @see #getToggleButtonModel()
     */
    public void setToggleButtonModel(ToggleButtonModel dataModel);


    /**
     * Informs whether the button is selected.
     *
     * <p>This method calls <code>ButtonModel.isSelected()</code>.</p>
     *
     * @return true if this button is selected.
     */
    public boolean isSelected();

    /**
     * Sets the selection button state.
     *
     * <p>This method calls <code>ButtonModel.setSelected(boolean)</code>.</p>
     *
     * @param b true if this button is going to be selected.
     */
    public void setSelected(boolean b);
}
