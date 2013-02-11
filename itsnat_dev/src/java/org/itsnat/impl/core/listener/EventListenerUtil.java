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

package org.itsnat.impl.core.listener;

import java.util.LinkedList;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.ItsNatEventImpl;
import org.itsnat.impl.core.event.ItsNatEventListenerChainImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class EventListenerUtil
{
    public static void handleEventListeners(Event evt,ItsNatEventListenerChainImpl<EventListener> chain)
    {
        if (chain.isStopped()) return; // Por si acaso, hay que tener en cuenta que puede haber reentrada a este método
        LinkedList<EventListener> listeners = chain.getListeners();
        while(!listeners.isEmpty())
        {
            EventListener listener = listeners.removeFirst();
            listener.handleEvent(evt);
            if (chain.isStopped()) break;
        }
    }

    public static void handleEventListeners(final Event evt,final LinkedList<EventListener> listeners)
    {
        ItsNatEventListenerChainImpl<EventListener> chain = new ItsNatEventListenerChainImpl<EventListener>(listeners)
        {
            @Override
           public void continueChain()
           {
               super.continueChain();
               handleEventListeners(evt,this);
           }
        };

        ItsNatEventImpl itsNatEvt = (ItsNatEventImpl)evt;
        itsNatEvt.setItsNatEventListenerChain(chain);

        try
        {
            handleEventListeners(evt,chain);
        }
        finally
        {
            itsNatEvt.unsetEventListenerChain();  // El chain sólo tiene validez en este contexto
        }
    }

    private static void addGlobalEventListeners(Event evt,LinkedList<EventListener> listeners)
    {
        ItsNatEventImpl itsNatEvt = (ItsNatEventImpl)evt;
        ItsNatServletImpl servlet = itsNatEvt.getItsNatServletRequestImpl().getItsNatServletImpl();
        ItsNatStfulDocumentImpl itsNatDoc = itsNatEvt.getItsNatStfulDocument();
        ItsNatDocumentTemplateImpl template = itsNatDoc.getItsNatDocumentTemplateImpl();
        ClientDocumentImpl clientDoc = itsNatEvt.getClientDocumentImpl();
        
        servlet.getGlobalEventListenerList(listeners);
        template.getGlobalEventListenerList(listeners);
        itsNatDoc.getGlobalEventListenerList(listeners);
        clientDoc.getGlobalEventListenerList(listeners);
    }

    public static void handleEventIncludingGlobalListeners(EventListener listener,Event evt)
    {
        LinkedList<EventListener> listeners = new LinkedList<EventListener>();
        addGlobalEventListeners(evt,listeners);

        if (listener != null) listeners.add(listener);

        handleEventListeners(evt,listeners);
    }

}
