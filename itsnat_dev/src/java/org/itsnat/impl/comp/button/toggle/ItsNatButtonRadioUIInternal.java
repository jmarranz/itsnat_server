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

package org.itsnat.impl.comp.button.toggle;

import org.itsnat.comp.button.ItsNatButtonGroup;
import org.itsnat.comp.button.toggle.ItsNatButtonRadio;

/**
 * ESTE INTERFACE FUE PUBLICO ANTES
 * 
 * Is the base interface of the User Interface of a radio button component.
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatButtonRadio#getItsNatButtonRadioUI()
 */
public interface ItsNatButtonRadioUIInternal extends ItsNatButtonToggleUIInternal
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatButtonRadio getItsNatButtonRadio();

    /**
     * Sets the group of this radio button on markup.
     *
     * <p>If this component is a &lt;input&gt; radio button
     * the method <code>HTMLInputElement.setName(String)</code> is called
     * with the name of the button group ({@link ItsNatButtonGroup#getName()}, otherwise
     * (free radio buttons) it does nothing, use a button model listener to decorate
     * the button when selection state changes.</p>
     *
     * @param buttonGroup the group this radio button belongs to.
     */
    public void setItsNatButtonGroup(ItsNatButtonGroup buttonGroup);
}
