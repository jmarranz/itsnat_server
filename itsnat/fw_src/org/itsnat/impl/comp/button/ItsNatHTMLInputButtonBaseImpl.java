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
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.ItsNatHTMLInputImpl;
import org.itsnat.impl.comp.mgr.web.ItsNatStfulWebDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLInputButtonBaseImpl extends ItsNatHTMLInputImpl implements ItsNatButtonInternal
{
    protected ItsNatButtonSharedImpl buttonDelegate = createItsNatButtonShared();
    protected ItsNatHTMLFormCompButtonSharedImpl htmlButtonDeleg = new ItsNatHTMLFormCompButtonSharedImpl(this);

    /**
     * Creates a new instance of ItsNatHTMLInputButtonBaseImpl
     */
    public ItsNatHTMLInputButtonBaseImpl(HTMLInputElement element, NameValue[] artifacts, ItsNatStfulWebDocComponentManagerImpl componentMgr)
    {
        super(element, artifacts, componentMgr);
    }

    public ItsNatButtonSharedImpl getItsNatButtonShared()
    {
        return buttonDelegate;
    }

    @Override
    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        // Es un botón
        enableEventListener("click"); // Por defecto se procesa, pues es lo importante
    }

    public ItsNatButtonUI getItsNatButtonUI()
    {
        return (ItsNatButtonUI) compUI;
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
        return (ButtonModel) dataModel;
    }

    public void setButtonModel(ButtonModel dataModel)
    {
        setDataModel(dataModel);
    }

    @Override
    public boolean isEnabled()
    {
        // Está propiedad está en el modelo no sólo en el DOM
        // el modelo modifica el DOM via listeners pues es el modelo el que manda
        return getButtonModel().isEnabled();
    }

    @Override
    public void setEnabled(boolean b)
    {
        // Está propiedad está en el modelo no sólo en el DOM
        // el modelo modificará el DOM via listeners pues es el modelo el que manda
        getButtonModel().setEnabled(b);
    }

    public void setDOMEnabled(boolean b)
    {
        // Llamada via button model listeners
        super.setEnabled(b);
    }

    @Override
    public void processNormalEvent(Event evt)
    {
        // Evento click al menos
        // Redefinir para cada tipo de botón si es necesario

        if (!htmlButtonDeleg.handleEvent(evt))
            return;

        super.processNormalEvent(evt);
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        return null;
    }
}
