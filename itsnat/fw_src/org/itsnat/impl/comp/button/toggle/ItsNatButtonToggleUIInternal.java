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

import org.itsnat.comp.button.ItsNatButtonUI;
import org.itsnat.comp.button.toggle.ItsNatButtonToggle;


/**
 * ESTA INTERFACE FUE PUBLICA ANTES
 * 
 * Is the base interface of the User Interface of a toggle button component.
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatButtonToggle#getItsNatButtonToggleUI()
 */
public interface ItsNatButtonToggleUIInternal extends ItsNatButtonUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatButtonToggle getItsNatButtonToggle();

    /**
     * Sets the selection state on markup.
     *
     * <p>If this component is a &lt;input&gt; (check box or radio button)
     * the method <code>HTMLInputElement.setChecked(boolean)</code> is called, otherwise
     * (free buttons) it does nothing, use a button model listener to decorate
     * the button when selection state changes.</p>
     *
     * @param selected if true the markup is set as selected.
     */
    public void setSelected(boolean selected);
}
