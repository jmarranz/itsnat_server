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


package org.itsnat.impl.core.event.server;

import java.util.ArrayList;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.EventInternal;
import org.itsnat.impl.core.event.server.domext.ServerItsNatContinueEventImpl;
import org.itsnat.impl.core.event.server.domext.ServerItsNatDOMExtEventImpl;
import org.itsnat.impl.core.event.server.domext.ServerItsNatUserEventImpl;
import org.itsnat.impl.core.event.server.domstd.ServerItsNatDOMStdEventImpl;
import org.itsnat.impl.core.listener.EventListenerUtil;
import org.itsnat.impl.core.registry.ItsNatDOMEventListenerRegistryImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ServerItsNatDOMEventImpl extends ServerItsNatNormalEventImpl implements EventInternal
{
    protected EventTarget target;
    protected String type;
    protected boolean canBubble;
    protected boolean cancelable;
    protected long timeStamp = System.currentTimeMillis();
    protected boolean stopPropagation = false;
    protected boolean preventDefault = false;
    protected short phase;
    protected boolean initialized;


    /** Creates a new instance of ServerItsNatDOMEventImpl */
    public ServerItsNatDOMEventImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public static ServerItsNatDOMEventImpl createServerDOMEvent(String eventGroup,ItsNatStfulDocumentImpl itsNatDoc) throws DOMException
    {
        if (eventGroup.startsWith("itsnat:"))
            return ServerItsNatDOMExtEventImpl.createServerDOMExtEvent(eventGroup,itsNatDoc);
        else
            return ServerItsNatDOMStdEventImpl.createServerItsNatDOMStdEvent(eventGroup,itsNatDoc);
    }

    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
    {
       this.type = eventTypeArg;
       this.canBubble = canBubbleArg;
       this.cancelable = cancelableArg;

       this.initialized = true;
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void checkInitializedEvent()
    {
        String type = getType();
        if(!initialized || (type == null) || type.equals(""))
            throw new EventException(EventException.UNSPECIFIED_EVENT_TYPE_ERR, "Unspecified event type");
    }

    public EventTarget getTarget()
    {
        return target;
    }

    public void setTarget(EventTarget target)
    {
        this.target = target;
    }

    public String getType()
    {
        return type;
    }

    public boolean getBubbles()
    {
        return canBubble;
    }

    public boolean getCancelable()
    {
        return cancelable;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public short getEventPhase()
    {
        return phase;
    }

    public void setEventPhase(short phase)
    {
        this.phase = phase;
    }


    public void stopPropagation()
    {
        this.stopPropagation = true;
    }

    public boolean getStopPropagation()
    {
        return stopPropagation;
    }

    public void setStopPropagation(boolean value)
    {
        this.stopPropagation = value;
    }

    public void preventDefault()
    {
        this.preventDefault = true;
    }

    public boolean getPreventDefault()
    {
        return preventDefault;
    }

    public void setPreventDefault(boolean value)
    {
        this.preventDefault = value;
    }


    public static boolean dispatchEventLocally(EventTarget target,Event event)
    {
        if (event == null) return false;

        ServerItsNatDOMEventImpl evt = (ServerItsNatDOMEventImpl)event; // No valen los eventos que no hayan sido creados con el createServerItsNatDOMStdEvent público
        evt.checkInitializedEvent();

        // timers, asynctask y comet no tienen mucho sentido aquí

        ItsNatStfulDocumentImpl itsNatDoc = evt.getItsNatStfulDocument();
        ClientDocumentStfulImpl clientDoc = evt.getClientDocumentStful();
        ItsNatDOMEventListenerRegistryImpl[] registries = new ItsNatDOMEventListenerRegistryImpl[2];
        if (evt instanceof ServerItsNatDOMStdEventImpl)
        {
            registries[0] = itsNatDoc.getDOMStdEventListenerRegistry();
            registries[1] = clientDoc.getDOMStdEventListenerRegistry();
        }
        else if (evt instanceof ServerItsNatUserEventImpl)
        {
            registries[0] = itsNatDoc.getUserEventListenerRegistry();
            registries[1] = clientDoc.getUserEventListenerRegistry();
        }
        else if (evt instanceof ServerItsNatContinueEventImpl)
        {
            registries[0] = clientDoc.getContinueEventListenerRegistry();
            registries[1] = null; // para que quede claro
        }
        else throw new ItsNatException("Event type is not supported:" + event.getType(),event); // Por ahora no hay más casos

        return dispatchEventLocally(target,evt,registries);
    }

    public static boolean dispatchEventLocally(EventTarget target,ServerItsNatDOMEventImpl evt,ItsNatDOMEventListenerRegistryImpl[] registries)
    {
        Node targetNode = (Node)target;

        evt.setTarget(target);
        evt.setStopPropagation(false);
        evt.setPreventDefault(false);

        // Guardamos los padres para evitar la interferencia que supone los cambios
        // en el DOM al ejecutar los handlers.
        boolean someoneCaptures = false;
        for(int i = 0; i < registries.length; i++)
        {
            if (registries[i] == null) continue;
            if (registries[i].getCapturingCount() > 0)
            {
                someoneCaptures = true;
                break;
            }
        }

        /* El incluir o no al propio targetNode en los nodos de "capture" es controvertido,
         * el DOM de Xerces por ejemplo NO lo incluye empieza desde el padre y lo deja claro
         * "Note that capturing listeners on the target node are _not_ invoked, even during the capture phase."
         * El DOM de Batik tampoco.
         * Sin embargo esto es lo que pasa en los navegadores cuando hay un evento
           recibido directamente en un nodo con dos listener, uno capture y otro no capture:
         *
         - Safari 3.1: ejecuta el listener capture antes de un no capture pero **en la fase AT_TARGET**
         - FireFox 2 y 3: ejecuta ambos listener en el orden en el que se registraron ignorando si son capture o no y **en la fase AT_TARGET**
         - Opera 9.27: ejecuta sólo el no capture 

           El comportamiento de Safari y FireFox NO es estándar,
           el estándar habla de "ancestor" (DOM Level 2 Events):
           "A capturing EventListener will not be triggered by events dispatched directly to the EventTarget upon which it is registered."
         */
        ArrayList<Node> parentList = null;
        if (someoneCaptures || evt.getBubbles())
        {
            parentList = new ArrayList<Node>();
            Node parent = targetNode.getParentNode();
            while (parent != null)
            {
                parentList.add(parent);
                parent = parent.getParentNode();
            }
        }

        if (someoneCaptures)
        {
            // Fase CAPTURING_PHASE, desde el más alto hasta el target
            evt.setEventPhase(Event.CAPTURING_PHASE);
            for (int i = parentList.size() - 1; i >= 0; i--)
            {
                EventTarget currentTarget = (EventTarget)parentList.get(i);
                dispatchEventLocallyCurrentTarget(currentTarget,evt,true,registries);

                if (evt.getStopPropagation())
                    return evt.getPreventDefault();  // Fin capturing
            }
        }

        evt.setEventPhase(Event.AT_TARGET);
        dispatchEventLocallyCurrentTarget(target,evt,false,registries);

        if (evt.getStopPropagation())
            return evt.getPreventDefault();

        if (evt.getBubbles())
        {
            evt.setEventPhase(Event.BUBBLING_PHASE);
            for (int i = 0; i < parentList.size(); i++)
            {
                EventTarget currentTarget = (EventTarget)parentList.get(i);
                dispatchEventLocallyCurrentTarget(currentTarget,evt,false,registries);

                if (evt.getStopPropagation())
                    return evt.getPreventDefault();  // fin bubbling
            }
        }

        return evt.getPreventDefault();
    }

    public static void dispatchEventLocallyCurrentTarget(EventTarget currentTarget,ServerItsNatDOMEventImpl evt,boolean useCapture,ItsNatDOMEventListenerRegistryImpl[] registries)
    {
        evt.setCurrentTarget(currentTarget);

        for(int i = 0; i < registries.length; i++)
        {
            if (registries[i] == null) continue;
            dispatchEventLocallyCurrentTarget(currentTarget,evt,useCapture,registries[i]);
        }
    }

    public static void dispatchEventLocallyCurrentTarget(EventTarget currentTarget,ServerItsNatDOMEventImpl evt,boolean useCapture,ItsNatDOMEventListenerRegistryImpl registry)
    {
        EventListener[] listeners = registry.getEventListenersArrayCopy(currentTarget,evt.getType(),useCapture);
        if (listeners == null) return;

        // Ejecutamos los listeners globales *para cada listener* para simular
        // lo más posible lo que ocurre cuando usamos el navegador y es que
        // para cada listener registrado se envía un evento del cliente al servidor
        // y se ejecutan los global listeners antes del listener destino.
        for (int i = 0; i < listeners.length; i++)
        {
            EventListener listener = listeners[i];
            EventListenerUtil.handleEventIncludingGlobalListeners(listener,evt);
        }
    }
}
