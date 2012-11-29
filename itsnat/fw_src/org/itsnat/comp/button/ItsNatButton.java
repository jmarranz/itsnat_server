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
import javax.swing.ButtonModel;

/**
 * Is the base interface of button components.
 *
 * <p>A button component manages a <code>javax.swing.ButtonModel</code>,
 * user actions updates this model accordingly. Default button model
 * depends on the concrete button type (normal, toggle etc). Button model listeners
 * may be used to keep track of the button state changes.</p>
 *
 * <p>In spite of ItsNat specifies interfaces bound to the typical form based buttons,
 * any (X)HTML element can be an ItsNat button (free buttons), however ItsNat does not define
 * a default decoration, button model listeners may be used to decorate
 * any button state.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatButton extends ItsNatElementComponent
{
    /**
     * Returns the current data model of this component.
     *
     * @return the current data model
     * @see #setButtonModel(javax.swing.ButtonModel)
     */
    public ButtonModel getButtonModel();

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
     * @see #getButtonModel()
     */
    public void setButtonModel(ButtonModel dataModel);

    /**
     * Creates a data model instance appropriated to this component. This instance
     * is not bound to the component.
     *
     * @return a new data model instance.
     */
    public ButtonModel createDefaultButtonModel();

    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatButtonUI getItsNatButtonUI();

    /**
     * Informs whether this button receives UI events.
     *
     * <p>Current implementation calls <code>ButtonModel.isEnabled()</code>.</p>
     *
     * @return true if button UI is enabled.
     * @see ItsNatComponent#isEnabled()
     */
    public boolean isEnabled();

    /**
     * Sets whether this button can receive UI events.
     *
     * <p>Current implementation calls <code>ButtonModel.setEnabled(boolean)</code>
     * </p>
     *
     * @param b true to enable the UI.
     * @see ItsNatComponent#setEnabled(boolean)
     */
    public void setEnabled(boolean b);
}
