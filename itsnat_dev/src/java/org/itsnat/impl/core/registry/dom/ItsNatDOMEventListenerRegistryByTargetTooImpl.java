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

package org.itsnat.impl.core.registry.dom;

import org.itsnat.impl.core.listener.dom.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.registry.WeakMapItsNatDOMEventListenerByTarget;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public abstract class ItsNatDOMEventListenerRegistryByTargetTooImpl extends ItsNatDOMEventListenerRegistryImpl
{
    protected WeakMapItsNatDOMEventListenerByTarget eventListenersByTarget = new WeakMapItsNatDOMEventListenerByTarget(this);

    /** Creates a new instance of ItsNatDOMEventListenerRegistryByTargetTooImpl */
    public ItsNatDOMEventListenerRegistryByTargetTooImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc)
    {
        super(itsNatDoc,clientDoc);
    }

    public ItsNatDOMEventListenerListSameTarget getItsNatDOMEventListenersByTarget(EventTarget target)
    {
        return eventListenersByTarget.getItsNatDOMEventListenersByTarget(target); 
    }

    public boolean containsItsNatDOMEventListener(EventTarget target,String type,EventListener listener,boolean useCapture)
    {
        return eventListenersByTarget.containsItsNatDOMEventListener(target, type, listener, useCapture);
    }

    public boolean canAddItsNatDOMEventListener(EventTarget target,String type,EventListener listener,boolean useCapture)
    {
        if (!canAddItsNatDOMEventListener(target,listener))
            return false;

        return !containsItsNatDOMEventListener(target,type,listener,useCapture);
    }

    @Override
    protected void addItsNatDOMEventListener(ItsNatDOMEventListenerWrapperImpl listenerWrapper)
    {
        super.addItsNatDOMEventListener(listenerWrapper);

        eventListenersByTarget.addItsNatDOMEventListener(listenerWrapper);
    }

    public int removeAllItsNatDOMEventListeners(EventTarget target,boolean updateClient)
    {
        return eventListenersByTarget.removeAllItsNatDOMEventListeners(target, updateClient);
    }

    @Override
    public void removeItsNatDOMEventListener(ItsNatDOMEventListenerWrapperImpl listener,boolean updateClient,boolean expunged)
    {
        // Este método es llamado también por processExpunged al limpiar los registros
        // de event target perdidos por el GC en WeakMapPluggable. En ese caso no hay ya registro asociado al event
        // target por lo que no tiene sentido llamar a removeItsNatDOMEventListener pasando el target, type etc.
        // pues hemos perdido el target, así evitamos desregistrar erróneamente pasando un target nulo
        // (pues además ciertos tipos de registros admiten target nulo)

        if (!expunged)
        {
            eventListenersByTarget.removeItsNatDOMEventListener(listener);
        }

        super.removeItsNatDOMEventListener(listener, updateClient,expunged);
    }

    public ItsNatDOMEventListenerWrapperImpl removeItsNatDOMEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        if (!isValidEventTarget(target,false)) return null; // No pudo registrarse, nos ahorramos una búsqueda inútil

        ItsNatDOMEventListenerWrapperImpl listenerWrapper = eventListenersByTarget.removeItsNatDOMEventListener(target,type,listener,useCapture);
        if (listenerWrapper == null) return null;

        super.removeItsNatDOMEventListener(listenerWrapper,updateClient,false);

        return listenerWrapper;
    }

    @Override
    public ItsNatDOMEventListenerWrapperImpl removeItsNatDOMEventListenerById(String id,boolean updateClient)
    {
        ItsNatDOMEventListenerWrapperImpl listenerWrapper = super.removeItsNatDOMEventListenerById(id, updateClient);
        if (listenerWrapper == null) return null;

        eventListenersByTarget.removeItsNatDOMEventListener(listenerWrapper);

        return listenerWrapper;
    }

    public EventListener[] getEventListenersArrayCopy(EventTarget target,String type,boolean useCapture)
    {
        return eventListenersByTarget.getEventListenersArrayCopy(target, type, useCapture);
    }
}
