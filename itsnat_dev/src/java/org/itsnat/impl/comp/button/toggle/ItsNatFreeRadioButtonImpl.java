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

import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.button.ItsNatButtonSharedImpl;
import org.itsnat.comp.button.ItsNatButtonGroup;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButton;
import org.itsnat.comp.ItsNatComponentUI;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.itsnat.core.NameValue;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatFreeRadioButtonImpl extends ItsNatFreeButtonToggleImpl implements ItsNatFreeRadioButton,ItsNatButtonRadioInternal
{
    protected ItsNatButtonGroupImpl itsNatButtonGroup;

    /**
     * Creates a new instance of ItsNatFreeRadioButtonImpl
     */
    public ItsNatFreeRadioButtonImpl(Element element,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);
    }

    public ItsNatButtonRadioSharedImpl getItsNatButtonRadioShared()
    {
        return (ItsNatButtonRadioSharedImpl)buttonDelegate;
    }

    public ItsNatButtonSharedImpl createItsNatButtonShared()
    {
        return new ItsNatButtonRadioSharedImpl(this);
    }

    public ItsNatButtonRadioUIInternal createDefaultItsNatFreeRadioButtonUI()
    {
        return new ItsNatFreeRadioButtonUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatFreeRadioButtonUI();
    }

    public ItsNatButtonRadioUIInternal getItsNatButtonRadioUIInternal()
    {
        return (ItsNatButtonRadioUIInternal)compUI;
    }

    public void initialSyncWithDataModel()
    {
        getItsNatButtonRadioShared().initialSyncWithDataModel();

        super.initialSyncWithDataModel();
    }

    public ToggleButtonModel getToggleButtonModelNoUpdateButtonGroup()
    {
        return super.getToggleButtonModel();
    }

    public ButtonModel getButtonModel()
    {
        return getToggleButtonModel();
    }

    public void setButtonModel(ButtonModel dataModel)
    {
        super.setButtonModel(dataModel);

        getToggleButtonModel(); // Para que se actualice el group del nuevo data model
    }

    public ToggleButtonModel getToggleButtonModel()
    {
        // Como el cambio de ButtonGroup en el data model no genera eventos hemos de sincronizar en cuanto se pueda
        ToggleButtonModel model = super.getToggleButtonModel();
        setButtonGroup(model.getGroup()); // Si no hay cambio no hace nada
        return model;
    }

    public void setToggleButtonModel(ToggleButtonModel dataModel)
    {
        super.setToggleButtonModel(dataModel);

        getToggleButtonModel(); // Para que se actualice el group del nuevo data model
    }

    public ItsNatButtonGroup getItsNatButtonGroup()
    {
        // Como el cambio de ButtonGroup en el data model no genera eventos hemos de sincronizar en cuanto se pueda
        getToggleButtonModel(); // Actualiza el ButtonGroup si ha cambiado

        return itsNatButtonGroup;
    }

    public ItsNatButtonGroup setButtonGroup(ButtonGroup group)
    {
        return getItsNatButtonRadioShared().setButtonGroup(this.itsNatButtonGroup,group);
    }

    public void setItsNatButtonGroup(ItsNatButtonGroup buttonGroup)
    {
        getItsNatButtonRadioShared().setItsNatButtonGroup(buttonGroup);
    }

    public void setItsNatButtonGroup(ItsNatButtonGroup buttonGroup,boolean addToGroup)
    {
        getItsNatButtonRadioShared().setItsNatButtonGroup(buttonGroup,addToGroup);
    }

    public void setItsNatButtonGroup(ItsNatButtonGroup buttonGroup,boolean addToGroup,boolean updateUI)
    {
        this.itsNatButtonGroup = (ItsNatButtonGroupImpl)buttonGroup;

        getItsNatButtonRadioShared().setItsNatButtonGroup(this.itsNatButtonGroup,addToGroup,updateUI);
    }

}
