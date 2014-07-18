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

package org.itsnat.impl.core.registry.dom.domext;

import org.itsnat.impl.core.listener.dom.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatContinueEventListenerWrapperImpl;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatContinueEventListenerRegistryImpl extends ItsNatDOMExtEventListenerRegistryImpl
{
    /**
     * Creates a new instance of ItsNatContinueEventListenerRegistryImpl
     */
    public ItsNatContinueEventListenerRegistryImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc.getItsNatStfulDocument(),clientDoc);
    }

    public void addContinueEventListener(EventTarget target,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        String type = ItsNatContinueEventListenerWrapperImpl.getTypeStatic();
        if (!canAddItsNatDOMEventListener(target,type,listener,false))
            return; // Ya registrado (u otra razón)

        ItsNatContinueEventListenerWrapperImpl listenerWrapper = new ItsNatContinueEventListenerWrapperImpl(itsNatDoc,clientDocTarget,target,listener,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);

        addItsNatDOMEventListener(listenerWrapper);
    }

    public ItsNatContinueEventListenerWrapperImpl removeContinueEventListenerById(String id)
    {
        return (ItsNatContinueEventListenerWrapperImpl)removeItsNatDOMEventListenerById(id,false); // Hacemos updateClient = false porque no hay nada que "desregistrar" en el cliente
    }

    public ItsNatDOMEventListenerWrapperImpl getItsNatDOMEventListenerById(String listenerId)
    {
        // A la vez que obtenemos el listener lo eliminamos pues este es el "contrado"
        // de los eventos/listeners "continue"
        // Aunque no se use este método con listeners continue es necesario dejarlo claro
        return removeContinueEventListenerById(listenerId);
    }

    public ItsNatContinueEventListenerWrapperImpl getContinueEventListenerById(String listenerId)
    {
        return (ItsNatContinueEventListenerWrapperImpl)getItsNatDOMEventListenerById(listenerId);
    }

    public EventListener[] getEventListenersArrayCopy(EventTarget target,String type,boolean useCapture)
    {
        // Los devueltos los desregistramos al mismo tiempo, pues suponemos que van a ser procesados
        // este es el uso normal de los continue listeners (sólo despachados una vez)
        EventListener[] listeners = super.getEventListenersArrayCopy(target,type,useCapture);
        if (listeners == null) return null;

        for(int i = 0; i < listeners.length; i++)
            removeItsNatDOMEventListener(target,type,listeners[i],useCapture,false); // No hay nada que enviar al cliente, por eso updateClient es false

        return listeners;
    }

    public int removeAllItsNatContinueEventListeners(EventTarget target,boolean updateClient)
    {
        return removeAllItsNatDOMEventListeners(target,updateClient);
    }
}
