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

package org.itsnat.impl.comp;

import org.itsnat.comp.ItsNatHTMLForm;
import org.itsnat.comp.ItsNatHTMLFormComponent;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByClientDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByDocDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByDocImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLFormComponentImpl extends ItsNatHTMLElementComponentImpl implements ItsNatHTMLFormComponent,EventListener
{
    protected ItsNatHTMLForm formComp;
    protected boolean uiEnabled = true; // Lo usan los componentes que pueden ser "markup driven"
    protected boolean serverUpdatingFromClient = false;

    /**
     * Creates a new instance of ItsNatHTMLFormComponentImpl
     */
    public ItsNatHTMLFormComponentImpl(HTMLElement element,NameValue[] artifacts,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);
    }

    public ItsNatCompDOMListenersByDocImpl createItsNatCompDOMListenersByDoc()
    {
        return new ItsNatCompDOMListenersByDocDefaultImpl(this);
    }

    public ItsNatCompDOMListenersByClientImpl createItsNatCompDOMListenersByClient(ClientDocumentImpl clientDoc)
    {
        return new ItsNatCompDOMListenersByClientDefaultImpl(this,clientDoc);
    }

    public boolean isServerUpdatingFromClient()
    {
        return serverUpdatingFromClient;
    }

    public void setServerUpdatingFromClient(boolean serverUpdatingFromClient)
    {
        this.serverUpdatingFromClient = serverUpdatingFromClient;
    }

    public boolean isUIEnabled()
    {
        return uiEnabled;
    }

    public void setUIEnabled(boolean uiEnabled)
    {
        this.uiEnabled = uiEnabled;
    }

    public abstract HTMLFormElement getHTMLFormElement();

    public ItsNatHTMLForm getItsNatHTMLForm()
    {
        HTMLFormElement formElem = getHTMLFormElement(); // puede ser null (el elemento puede no estar dentro de un <form>
        if (formElem == null)
            return null;
        if ((formComp == null)||(formComp.getHTMLFormElement() != formElem))
        {
            ItsNatStfulDocComponentManagerImpl componentMgr = getItsNatStfulDocComponentManager();
            this.formComp = componentMgr.getItsNatHTMLForm(formElem);
        }
        return formComp;
    }

    public boolean disableSendCodeToRequesterIfServerUpdating()
    {
        if (isServerUpdatingFromClient())
        {
            ItsNatEvent evt = (ItsNatEvent)getCurrentEventProcessing();
            ItsNatServletRequestImpl itsNatRequest = (ItsNatServletRequestImpl)evt.getItsNatServletRequest();
            ClientDocument clientDoc = itsNatRequest.getClientDocument();
            if (clientDoc.isSendCodeEnabled())
            {
                // Desactiva el cliente actual, pero los demás no, normalmente los de control remoto
                // Normalmente usamos esto para evitar volver a enviar datos al cliente principal (por cambio del DOM)
                // cuando dichos datos seguramente vienen del cliente, pero esto no impide
                // que a los demás clientes les llegue.
                clientDoc.disableSendCode(); // Desactivamos, será temporal
                return true; // Hemos desactivado, es el recordatorio de que debemos activar de nuevo porque estaba activo (para restaurar el estado)
            }
        }

        return false;
    }

    public void enableSendCodeToRequester()
    {
        ItsNatEvent evt = (ItsNatEvent)getCurrentEventProcessing();
        ItsNatServletRequestImpl itsNatRequest = (ItsNatServletRequestImpl)evt.getItsNatServletRequest();
        ClientDocument requester = itsNatRequest.getClientDocument();
        requester.enableSendCode(); // Restauramos como estaba
    }

}
