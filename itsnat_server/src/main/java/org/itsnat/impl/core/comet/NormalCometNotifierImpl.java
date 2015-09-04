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

package org.itsnat.impl.core.comet;

import java.util.LinkedList;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.CommModeImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.listener.EventListenerSerializableInternal;
import org.itsnat.impl.core.event.ItsNatEventListenerChainImpl;
import org.itsnat.impl.core.event.client.dom.domext.ClientItsNatNormalCometEventImpl;
import org.itsnat.impl.core.listener.EventListenerUtil;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class NormalCometNotifierImpl extends CometNotifierImpl
{
    protected long eventTimeout;
    protected LinkedList<EventListener> normalEventListeners;
    protected EventListener listenerDispatcher;
    protected int commMode;
    protected ParamTransport[] extraParams;
    protected String preSendCode;

    /** Creates a new instance of NormalCometNotifierImpl */
    public NormalCometNotifierImpl(int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,ClientDocumentStfulImpl clientDoc)
    {
        super(true,clientDoc); // userDataSync es true porque el CometNotifier típicamente será usado por hilos "background"

        // NO USAR otro modo diferente al ASYNC o SCRIPT pues el evento se retiene en el servidor
        // y en modo ASYNC_HOLD o SCRIPT_HOLD impediría a los demás llegar.
        if (!CommModeImpl.isPureAsyncMode(commMode))
            throw new ItsNatException("Communication transport mode must be pure asynchronous");

        this.commMode = commMode;
        this.extraParams = extraParams;
        this.preSendCode = preSendCode;
        this.eventTimeout = eventTimeout;

        this.listenerDispatcher = new EventListenerSerializableInternal()
        {
            public void handleEvent(Event evt)
            {
                if (hasEventListenerListeners())
                {
                    ClientItsNatNormalCometEventImpl cometEvt = (ClientItsNatNormalCometEventImpl)evt;

                    @SuppressWarnings("unchecked")
                    ItsNatEventListenerChainImpl<EventListener> chain = cometEvt.getItsNatEventListenerChainImpl();
                    if (getEventListenerList(chain))
                        EventListenerUtil.handleEventListeners(cometEvt, chain);
                }
            }
        };

        clientDoc.addCometNotifier(this);
        clientDoc.addCometTask(this,extraParams,preSendCode);
    }

    public int getCommMode()
    {
        return commMode;
    }

    public void addCometTask()
    {
        getClientDocumentStful().addCometTask(this,extraParams,preSendCode);
    }

    public long getEventTimeout()
    {
        return eventTimeout;
    }

    @Override
    public void stopInternal()
    {
        super.stopInternal();

        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        ItsNatDocumentImpl itsNatDoc = getItsNatStfulDocument();
        synchronized(itsNatDoc) // Por si el stop es llamado por el hilo generador de cambios. No es necesario sincronizar los padres pues esta acción sólo afecta a este documento
        {
            clientDoc.removeCometNotifier(this);
        }
    }

    public EventListener getEventListenerDispatcher()
    {
        return listenerDispatcher;
    }

    public boolean hasEventListenerListeners()
    {
        if (normalEventListeners == null)
            return false;
        return !normalEventListeners.isEmpty();
    }

    public LinkedList<EventListener> getEventListenerList()
    {
        // No sincronizamos porque sólo debe usarse con el documento sincronizado
        // es decir en requests web.
        if (normalEventListeners == null)
            this.normalEventListeners = new LinkedList<EventListener>();
        return normalEventListeners;
    }

    public boolean getEventListenerList(ItsNatEventListenerChainImpl<EventListener> chain)
    {
        return chain.addFirstListenerList(normalEventListeners); // Puede ser null
    }

    public void addEventListener(EventListener listener)
    {
        // Si se añadieran parámetros tal y como ParamTransport[], preSendCode etc
        // se haría de la misma forma que se hace en ItsNatComponentImpl, es decir
        // compartidos por todos los listeners, pues todos son despachados con el mismo evento-request.

        LinkedList<EventListener> normalEventListeners = getEventListenerList();
        normalEventListeners.add(listener);
    }

    public void removeEventListener(EventListener listener)
    {
        LinkedList<EventListener> normalEventListeners = getEventListenerList();
        normalEventListeners.remove(listener);
    }
}
