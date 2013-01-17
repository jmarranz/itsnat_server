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

import javax.swing.ButtonModel;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputButtonToggle;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.button.ItsNatHTMLInputButtonBaseImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLInputButtonToggleImpl extends ItsNatHTMLInputButtonBaseImpl implements ItsNatHTMLInputButtonToggle,ItsNatButtonToggleInternal
{
    protected ItsNatHTMLInputButtonToggleMarkupDrivenUtil markupDrivenUtil;

    /**
     * Creates a new instance of ItsNatHTMLInputButtonToggleImpl
     */
    public ItsNatHTMLInputButtonToggleImpl(HTMLInputElement element,NameValue[] artifacts,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        this.markupDrivenUtil = ItsNatHTMLInputButtonToggleMarkupDrivenUtil.initMarkupDriven(this);
    }

    public ToggleButtonModel getToggleButtonModel()
    {
        return (ToggleButtonModel)dataModel;
    }

    public void setToggleButtonModel(ToggleButtonModel dataModel)
    {
        setDataModel(dataModel);
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

    public ItsNatButtonToggleUIInternal getItsNatButtonToggleUIInternal()
    {
        return (ItsNatButtonToggleUIInternal)compUI;
    }

    public void setUISelected(boolean selected)
    {
        if (!isUIEnabled()) return;

        ItsNatButtonToggleUIInternal compUI = getItsNatButtonToggleUIInternal();

        // Hay un caso de envío "redundante" al servidor y es cuando activamos
        // otro radio button, el Group llamará al actualmente activado para desactivar
        // el componente no tiene constancia de pulsación (pues se ha pulsado otro)
        // por lo que no se inhibe el envío de modificación al cliente,
        // pero no importa el código es mínimo y es una operación redundante (funciona bien)

        boolean wasDisabled = disableSendCodeToRequesterIfServerUpdating(); // Evitamos así que llegue al requester si no debe llegar (pero sí a los observadores)

        try
        {
            compUI.setSelected(selected);
        }
        finally
        {
            if (wasDisabled) enableSendCodeToRequester();
        }
    }

    @Override
    public void initialSyncUIWithDataModel()
    {
        super.initialSyncUIWithDataModel();

        if (markupDrivenUtil != null)
            markupDrivenUtil.initialSyncUIWithDataModel();
    }

    @Override
    public void setDefaultDataModel(Object dataModel)
    {
        if (markupDrivenUtil != null)
            markupDrivenUtil.preSetDefaultDataModel(dataModel);

        super.setDefaultDataModel(dataModel);
    }

    @Override
    public void disposeEffective(boolean updateClient)
    {
        super.disposeEffective(updateClient);

        if (markupDrivenUtil != null)
        {
            markupDrivenUtil.dispose();
            this.markupDrivenUtil = null;
        }
    }

    public boolean isMarkupDriven()
    {
        return markupDrivenUtil != null;
    }

    public void setMarkupDriven(boolean value)
    {
        this.markupDrivenUtil = ItsNatHTMLInputButtonToggleMarkupDrivenUtil.setMarkupDriven(this, markupDrivenUtil, value);
    }

}
