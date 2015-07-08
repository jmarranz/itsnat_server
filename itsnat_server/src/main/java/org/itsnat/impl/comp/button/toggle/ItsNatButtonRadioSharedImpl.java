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

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.ItsNatButtonGroup;

/**
 *
 * @author jmarranz
 */
public class ItsNatButtonRadioSharedImpl extends ItsNatButtonToggleSharedImpl
{

    /**
     * Creates a new instance of ItsNatButtonRadioSharedImpl
     */
    public ItsNatButtonRadioSharedImpl(ItsNatButtonRadioInternal comp)
    {
        super(comp);
    }

    public ItsNatButtonRadioInternal getItsNatButtonRadio()
    {
        return (ItsNatButtonRadioInternal)comp;
    }

    public void initialSyncWithDataModel()
    {
        ItsNatButtonRadioInternal comp = getItsNatButtonRadio();
        ToggleButtonModel dataModel = comp.getToggleButtonModel();
        comp.setButtonGroup(dataModel.getGroup()); // Antes de nada y por si no se hubiera definido o hubiera cambiado (pues el cambio de grupo no se notifica con eventos)
    }

    @Override
    public void syncWithDataModel()
    {
        ItsNatButtonRadioInternal comp = getItsNatButtonRadio();
        ToggleButtonModel dataModel = comp.getToggleButtonModel();
        comp.setButtonGroup(dataModel.getGroup()); // Antes de nada y por si no se hubiera definido o hubiera cambiado (pues el cambio de grupo no se notifica con eventos)

        super.syncWithDataModel();
    }

    @Override
    public void syncUIWithDataModel()
    {
        ItsNatButtonRadioInternal comp = getItsNatButtonRadio();
        ItsNatButtonRadioUIInternal compUI = comp.getItsNatButtonRadioUIInternal();
        compUI.setItsNatButtonGroup(comp.getItsNatButtonGroup());

        super.syncUIWithDataModel();
    }

    public ItsNatButtonGroup setButtonGroup(ItsNatButtonGroup currButtonGroup,ButtonGroup group)
    {
        // Tenemos la obligación de hacer que el radio button pertenezca al
        // nuevo grupo si hay cambio
        ItsNatButtonRadioInternal comp = getItsNatButtonRadio();
        if (group == null)
        {
            comp.setItsNatButtonGroup(null);
            return null;
        }
        else if ((currButtonGroup == null) || currButtonGroup.getButtonGroup() != group) // O no hay actual o ha cambiado
        {
            ItsNatComponentManager componentMgr = comp.getItsNatComponentManager();
            ItsNatButtonGroup newButtonGroup = componentMgr.getItsNatButtonGroup(group);
            comp.setItsNatButtonGroup(newButtonGroup);
            return newButtonGroup;
        }
        else
            return currButtonGroup; // No ha cambiado
    }

    public void setItsNatButtonGroup(ItsNatButtonGroup buttonGroup)
    {
        setItsNatButtonGroup(buttonGroup,true);
    }

    public void setItsNatButtonGroup(ItsNatButtonGroup buttonGroup,boolean addToGroup)
    {
        ItsNatButtonRadioInternal comp = getItsNatButtonRadio();
        comp.setItsNatButtonGroup(buttonGroup,addToGroup,true);
    }

    public void setItsNatButtonGroup(ItsNatButtonGroupImpl itsNatButtonGroup,boolean addToGroup,boolean updateUI)
    {
        ItsNatButtonRadioInternal comp = getItsNatButtonRadio();
        ToggleButtonModel dataModel = comp.getToggleButtonModelNoUpdateButtonGroup();
        if (itsNatButtonGroup != null)
            dataModel.setGroup(itsNatButtonGroup.getButtonGroup());
        else
            dataModel.setGroup(null);

        if (addToGroup && (itsNatButtonGroup != null))
            itsNatButtonGroup.addButton(comp,false);

        ItsNatButtonRadioUIInternal compUI = comp.getItsNatButtonRadioUIInternal();
        if (updateUI && (compUI != null))
        {
            compUI.setItsNatButtonGroup(itsNatButtonGroup);
        }
    }
}
