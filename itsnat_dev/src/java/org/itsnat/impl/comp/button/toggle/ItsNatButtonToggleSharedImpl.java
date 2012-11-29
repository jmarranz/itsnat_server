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

import org.itsnat.impl.comp.button.ItsNatButtonSharedImpl;
import javax.swing.JToggleButton.ToggleButtonModel;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatButtonToggleSharedImpl extends ItsNatButtonSharedImpl
{

    /**
     * Creates a new instance of ItsNatButtonToggleSharedImpl
     */
    public ItsNatButtonToggleSharedImpl(ItsNatButtonToggleInternal comp)
    {
        super(comp);
    }

    public ItsNatButtonToggleInternal getItsNatButtonToggle()
    {
        return (ItsNatButtonToggleInternal)comp;
    }

    public void syncUIWithDataModel()
    {
        super.syncUIWithDataModel();

        ItsNatButtonToggleInternal comp = getItsNatButtonToggle();
        ToggleButtonModel dataModel = comp.getToggleButtonModel();
        comp.setUISelected(dataModel.isSelected()); // Sincronización necesaria
    }
}
