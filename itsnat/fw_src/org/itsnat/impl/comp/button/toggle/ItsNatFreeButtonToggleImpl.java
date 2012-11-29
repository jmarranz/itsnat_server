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
import org.itsnat.impl.comp.button.ItsNatFreeButtonImpl;
import org.itsnat.comp.button.toggle.ItsNatFreeButtonToggle;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.itsnat.core.NameValue;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatFreeButtonToggleImpl extends ItsNatFreeButtonImpl implements ItsNatFreeButtonToggle,ItsNatButtonToggleInternal
{
    /**
     * Creates a new instance of ItsNatFreeButtonToggleImpl
     */
    public ItsNatFreeButtonToggleImpl(Element element,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);
    }

    public boolean isSelected()
    {
        return getButtonModel().isSelected();
    }

    public void setSelected(boolean b)
    {
        getButtonModel().setSelected(b);
    }

    public Object createDefaultModelInternal()
    {
        return createDefaultButtonModel();
    }

    public ButtonModel createDefaultButtonModel()
    {
        return new ToggleButtonModel();
    }

    public ToggleButtonModel getToggleButtonModel()
    {
        return (ToggleButtonModel)dataModel;
    }

    public void setToggleButtonModel(ToggleButtonModel dataModel)
    {
        setDataModel(dataModel);
    }

    public ItsNatButtonToggleUIInternal getItsNatButtonToggleUIInternal()
    {
        return (ItsNatButtonToggleUIInternal)compUI;
    }

    public void setUISelected(boolean selected)
    {
        ItsNatButtonToggleUIInternal compUI = getItsNatButtonToggleUIInternal();
        compUI.setSelected(selected);
    }
}
