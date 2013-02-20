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

package org.itsnat.impl.core.doc;

import java.io.Serializable;
import java.util.Date;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.listener.domext.ItsNatTimerEventListenerWrapperImpl;
import org.itsnat.impl.core.util.MapUniqueId;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatTimerImpl implements ItsNatTimer,Serializable
{
    protected MapUniqueId<ItsNatTimerEventListenerWrapperImpl> listeners;
    protected ClientDocumentStfulImpl clientDoc;
    protected boolean canceled = false;

    /**
     * Creates a new instance of ItsNatTimerImpl
     */
    public ItsNatTimerImpl(ClientDocumentStfulImpl clientDoc)
    {
        this.clientDoc = clientDoc;
    }

    public ItsNatDocument getItsNatDocument()
    {
        return getItsNatStfulDocument();
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return clientDoc.getItsNatStfulDocument();
    }

    public ClientDocument getClientDocument()
    {
        return clientDoc;
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return clientDoc;
    }

    public MapUniqueId<ItsNatTimerEventListenerWrapperImpl> getListeners()
    {
        if (listeners == null)
            this.listeners = new MapUniqueId<ItsNatTimerEventListenerWrapperImpl>(clientDoc.getUniqueIdGenerator());
        return listeners;
    }

    public void cancel()
    {
        if (canceled) return;

        MapUniqueId<ItsNatTimerEventListenerWrapperImpl> listeners = getListeners();
        if (!listeners.isEmpty())
        {
            // Convertimos en array porque al cancelar cada timer task se modifica el propio Map (un iterador daría error)
            ItsNatTimerEventListenerWrapperImpl[] listenerArray = listeners.values().toArray(new ItsNatTimerEventListenerWrapperImpl[listeners.size()]);
            for(int i = 0; i < listenerArray.length; i++)
            {
                ItsNatTimerEventListenerWrapperImpl listener = listenerArray[i];
                listener.cancel(); // Automáticamente desregistra de este timer y en el documento. Si en este momento está ejecutándose el método TimerEventListener.handleTimerEvent(Event)  asegura que no se genera código JavaScript para enviar más
            }
            listeners.clear(); // No hace falta pero por si acaso
        }

        this.canceled = true;
    }

    public ItsNatTimerEventListenerWrapperImpl getTimerEventListenerWrapper(String id)
    {
        MapUniqueId<ItsNatTimerEventListenerWrapperImpl> listeners = getListeners();
        return listeners.get(id);
    }

    public void removeListener(ItsNatTimerEventListenerWrapperImpl listener)
    {
        clientDoc.getItsNatTimerEventListenerRegistry().removeItsNatTimerEventListener(listener,true);
    }

    private ItsNatTimerHandle scheduleGeneric(EventTarget target,EventListener listener,long time,long period,boolean fixedRate,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        if (canceled) throw new ItsNatException("This timer is cancelled",this);

        return clientDoc.getItsNatTimerEventListenerRegistry().addItsNatTimerEventListener(target,listener,time,period,fixedRate,commMode,extraParams,preSendCode,eventTimeout,bindToListener,this);
    }

    public void addListenerLocal(ItsNatTimerEventListenerWrapperImpl listenerWrapper)
    {
        // Registro local
        MapUniqueId<ItsNatTimerEventListenerWrapperImpl> listeners = getListeners();
        listeners.put(listenerWrapper);
    }

    public void removeListenerLocal(ItsNatTimerEventListenerWrapperImpl listener)
    {
        // Desregistro local
        MapUniqueId<ItsNatTimerEventListenerWrapperImpl> listeners = getListeners();
        listeners.remove(listener);
    }

    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, Date time)
    {
        int commMode = clientDoc.getCommMode();
        long eventTimeout = clientDoc.getEventTimeout();
        return schedule(target,task,time,commMode,null,null,eventTimeout);
    }

    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, Date time,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        return scheduleGeneric(target,task,time.getTime(),0,false,commMode,extraParams,preSendCode,eventTimeout,null);
    }

    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, Date firstTime, long period)
    {
        int commMode = clientDoc.getCommMode();
        long eventTimeout = clientDoc.getEventTimeout();
        return schedule(target,task,firstTime,period,commMode,null,null,eventTimeout);
    }

    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, Date firstTime, long period,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        return scheduleGeneric(target,task,firstTime.getTime(),period,false,commMode,extraParams,preSendCode,eventTimeout,null);
    }

    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, long delay)
    {
        int commMode = clientDoc.getCommMode();
        long eventTimeout = clientDoc.getEventTimeout();
        return schedule(target,task,delay,commMode,null,null,eventTimeout);
    }

    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, long delay,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay");

        return scheduleGeneric(target,task,System.currentTimeMillis() + delay,0,false,commMode,extraParams,preSendCode,eventTimeout,null);
    }

    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, long delay, long period)
    {
        int commMode = clientDoc.getCommMode();
        long eventTimeout = clientDoc.getEventTimeout();
        return schedule(target,task,delay,period,commMode,null,null,eventTimeout);
    }

    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, long delay, long period,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay");

        return scheduleGeneric(target,task,System.currentTimeMillis() + delay,period,false,commMode,extraParams,preSendCode,eventTimeout,null);
    }

    public ItsNatTimerHandle scheduleAtFixedRate(EventTarget target,EventListener task, Date firstTime, long period)
    {
        int commMode = clientDoc.getCommMode();
        long eventTimeout = clientDoc.getEventTimeout();
        return scheduleAtFixedRate(target,task,firstTime,period,commMode,null,null,eventTimeout);
    }

    public ItsNatTimerHandle scheduleAtFixedRate(EventTarget target,EventListener task, Date firstTime, long period,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        return scheduleGeneric(target,task,firstTime.getTime(),period,true,commMode,extraParams,preSendCode,eventTimeout,null);
    }

    public ItsNatTimerHandle scheduleAtFixedRate(EventTarget target,EventListener task, long delay, long period)
    {
        int commMode = clientDoc.getCommMode();
        long eventTimeout = clientDoc.getEventTimeout();
        return scheduleAtFixedRate(target,task,delay,period,commMode,null,null,eventTimeout);
    }

    public ItsNatTimerHandle scheduleAtFixedRate(EventTarget target,EventListener task, long delay, long period,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay");

        return scheduleGeneric(target,task,System.currentTimeMillis() + delay,period,true,commMode,extraParams,preSendCode,eventTimeout,null);
    }
}
