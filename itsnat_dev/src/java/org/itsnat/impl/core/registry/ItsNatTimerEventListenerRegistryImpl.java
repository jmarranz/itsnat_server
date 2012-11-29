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

package org.itsnat.impl.core.registry;

import org.itsnat.impl.core.doc.ItsNatTimerImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.listener.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatTimerEventListenerWrapperImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 * NO derivamos de DOMEventListenerRegistryByTargetTooImpl porque
 * nos interesa registrar múltiples veces un mismo listener y
 * DOMEventListenerRegistryByTargetTooImpl está más orientado
 * a eventos DOM normales con el registro-identidad: target,type,listener,useCapture
 *
 * Pero como nos interesa asociar los timers a también a nodos y admitir el auto-desregistro
 * cuando se pierde el target, utilizamos nuestra colección especial WeakMapPuggable,
 * sobre el uso de esta clase ver ItsNatDOMEventListenerRegistryByTargetTooImpl.
 *
 * @author jmarranz
 */
public class ItsNatTimerEventListenerRegistryImpl extends ItsNatDOMEventListenerRegistryImpl
{
    protected WeakMapItsNatDOMEventListenerByTarget eventListenersByTarget = new WeakMapItsNatDOMEventListenerByTarget(this);

    /**
     * Creates a new instance of ContinueEventListenerRegistryImpl
     */
    public ItsNatTimerEventListenerRegistryImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc.getItsNatStfulDocument(),clientDoc);
    }

    protected void addItsNatDOMEventListener(ItsNatDOMEventListenerWrapperImpl listenerWrapper)
    {
        super.addItsNatDOMEventListener(listenerWrapper);

        ItsNatTimerEventListenerWrapperImpl timerListener = (ItsNatTimerEventListenerWrapperImpl)listenerWrapper;
        timerListener.getItsNatTimerImpl().addListenerLocal(timerListener);

        eventListenersByTarget.addItsNatDOMEventListener(listenerWrapper);
    }

    public ItsNatTimerEventListenerWrapperImpl addItsNatTimerEventListener(EventTarget target,EventListener listener,long time,long period,boolean fixedRate,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener,ItsNatTimerImpl timer)
    {
        // El target puede ser nulo
        // Permitimos registrar múltiples veces el mismo listener para el mismo target
        if (!canAddItsNatDOMEventListener(target,listener))
            return null;

        if (period < 0)
            throw new ItsNatException("Negative period");

        ItsNatTimerEventListenerWrapperImpl listenerWrapper = new ItsNatTimerEventListenerWrapperImpl(target,listener,time,period,fixedRate,commMode,extraParams,preSendCode,eventTimeout,bindToListener,timer);
        addItsNatDOMEventListener(listenerWrapper);
        return listenerWrapper;
    }

    protected ItsNatDOMEventListenerWrapperImpl removeItsNatDOMEventListenerById(String id,boolean updateClient)
    {
        ItsNatTimerEventListenerWrapperImpl listenerWrapper = (ItsNatTimerEventListenerWrapperImpl)super.removeItsNatDOMEventListenerById(id,updateClient);
        if (listenerWrapper == null) return null;

        listenerWrapper.getItsNatTimerImpl().removeListenerLocal(listenerWrapper);

        removeItsNatTimerEventListenerPrivate(listenerWrapper);

        return listenerWrapper;
    }

    public void removeItsNatDOMEventListener(ItsNatDOMEventListenerWrapperImpl listenerWrapper,boolean updateClient,boolean expunged)
    {
        // Este método es llamado también por processExpunged

        ItsNatTimerEventListenerWrapperImpl timerListener = (ItsNatTimerEventListenerWrapperImpl)listenerWrapper;

        if (!expunged)
            removeItsNatTimerEventListenerPrivate(timerListener);

        timerListener.getItsNatTimerImpl().removeListenerLocal(timerListener);

        super.removeItsNatDOMEventListener(timerListener, updateClient,expunged);
    }

    private void removeItsNatTimerEventListenerPrivate(ItsNatTimerEventListenerWrapperImpl listenerWrapper)
    {
        eventListenersByTarget.removeItsNatDOMEventListener(listenerWrapper);
    }

    public void removeItsNatTimerEventListener(ItsNatTimerEventListenerWrapperImpl listener,boolean updateClient)
    {
        removeItsNatDOMEventListener(listener,updateClient,false);
    }

    public ItsNatTimerEventListenerWrapperImpl getItsNatTimerEventListenerById(String listenerId)
    {
        return (ItsNatTimerEventListenerWrapperImpl)getItsNatDOMEventListenerById(listenerId);
    }

    public EventListener[] getEventListenersArrayCopy(EventTarget target,String type,boolean useCapture)
    {
        // No se usa nunca, por ejemplo porque los timer events no pueden ser disparados desde el servidor
        // (no está soportado por ahora)
        throw new ItsNatException("INTERNAL ERROR");
    }

    public int removeAllItsNatDOMEventListeners(EventTarget target,boolean updateClient)
    {
        return eventListenersByTarget.removeAllItsNatDOMEventListeners(target, updateClient);
    }

    public int removeAllItsNatTimerEventListeners(EventTarget target,boolean updateClient)
    {
        return removeAllItsNatDOMEventListeners(target,updateClient);
    }

}
