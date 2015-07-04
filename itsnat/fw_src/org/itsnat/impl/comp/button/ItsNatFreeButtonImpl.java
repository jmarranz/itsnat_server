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

package org.itsnat.impl.comp.button;

import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import org.itsnat.comp.button.ItsNatButtonUI;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.*;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatFreeButtonImpl extends ItsNatFreeElementComponentImpl implements ItsNatButtonInternal
{
    protected ItsNatButtonSharedImpl buttonDelegate = createItsNatButtonShared();

    /**
     * Creates a new instance of ItsNatFreeButtonImpl
     */
    public ItsNatFreeButtonImpl(Element element,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);
    }

    public ItsNatCompNormalEventListenersByDocImpl createItsNatCompNormalEventListenersByDoc()
    {
        return new ItsNatCompNormalEventListenersByDocDefaultImpl(this);
    }

    public ItsNatCompNormalEventListenersByClientImpl createItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc)
    {
        return new ItsNatCompNormalEventListenersByClientDefaultImpl(this,clientDoc);
    }

    public ItsNatButtonSharedImpl getItsNatButtonShared()
    {
        return buttonDelegate;
    }

    public Object createDefaultStructure()
    {
        return null;
    }

    @Override
    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        enableEventListener("click"); // Por defecto se procesa, pues es lo importante
    }

    public ItsNatButtonUI getItsNatButtonUI()
    {
        return (ItsNatButtonUI)compUI;
    }

    public void bindDataModel()
    {
        buttonDelegate.bindDataModel();
    }

    public void unbindDataModel()
    {
        buttonDelegate.unbindDataModel();
    }

    public void initialSyncUIWithDataModel()
    {
        buttonDelegate.initialSyncUIWithDataModel();
    }

    public void syncWithDataModel()
    {
        buttonDelegate.syncWithDataModel();
    }

    public void stateChanged(ChangeEvent e)
    {
        buttonDelegate.stateChanged(e);
    }

    public ButtonModel getButtonModel()
    {
        return (ButtonModel)dataModel;
    }

    public void setButtonModel(ButtonModel dataModel)
    {
        setDataModel(dataModel);
    }

    public boolean isEnabled()
    {
        return getButtonModel().isEnabled();
    }

    public void setEnabled(boolean b)
    {
        getButtonModel().setEnabled(b);
    }

    public void setDOMEnabled(boolean b)
    {
        // Nada que hacer
    }

    @Override
    public void processNormalEvent(Event evt)
    {
        // Evento click al menos
        if (!buttonDelegate.handleEvent(evt))
            return;

        super.processNormalEvent(evt);
    }
}
