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

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public abstract class ItsNatNormalEventListenerRegistryByTargetTooImpl extends ItsNatNormalEventListenerRegistryImpl
{
    protected WeakMapItsNatNormalEventListenerByTarget eventListenersByTarget = new WeakMapItsNatNormalEventListenerByTarget(this);

    /** Creates a new instance of ItsNatNormalEventListenerRegistryByTargetTooImpl */
    public ItsNatNormalEventListenerRegistryByTargetTooImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc)
    {
        super(itsNatDoc,clientDoc);
    }

    public ItsNatNormalEventListenerListSameTarget getItsNatNormalEventListenersByTarget(EventTarget target)
    {
        return eventListenersByTarget.getItsNatNormalEventListenersByTarget(target); 
    }

    public boolean containsItsNatNormalEventListener(EventTarget target,String type,EventListener listener,boolean useCapture)
    {
        return eventListenersByTarget.containsItsNatNormalEventListener(target, type, listener, useCapture);
    }

    public boolean canAddItsNatNormalEventListener(EventTarget target,String type,EventListener listener,boolean useCapture)
    {
        if (!canAddItsNatNormalEventListener(target,type,listener))
            return false;

        return !containsItsNatNormalEventListener(target,type,listener,useCapture);
    }

    @Override
    protected void addItsNatNormalEventListener(ItsNatNormalEventListenerWrapperImpl listenerWrapper)
    {
        super.addItsNatNormalEventListener(listenerWrapper);

        eventListenersByTarget.addItsNatNormalEventListener(listenerWrapper);
    }

    public int removeAllItsNatNormalEventListeners(EventTarget target,boolean updateClient)
    {
        return eventListenersByTarget.removeAllItsNatNormalEventListeners(target, updateClient);
    }

    @Override
    public void removeItsNatNormalEventListener(ItsNatNormalEventListenerWrapperImpl listener,boolean updateClient,boolean expunged)
    {
        // Este método es llamado también por processExpunged al limpiar los registros
        // de event target perdidos por el GC en WeakMapPluggable. En ese caso no hay ya registro asociado al event
        // target por lo que no tiene sentido llamar a removeItsNatDOMEventListener pasando el target, type etc.
        // pues hemos perdido el target, así evitamos desregistrar erróneamente pasando un target nulo
        // (pues además ciertos tipos de registros admiten target nulo)

        if (!expunged)
        {
            eventListenersByTarget.removeItsNatNormalEventListener(listener);
        }

        super.removeItsNatNormalEventListener(listener, updateClient,expunged);
    }

    public ItsNatNormalEventListenerWrapperImpl removeItsNatNormalEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        if (!isValidEventTarget(target,type,false)) return null; // No pudo registrarse, nos ahorramos una búsqueda inútil

        ItsNatNormalEventListenerWrapperImpl listenerWrapper = eventListenersByTarget.removeItsNatNormalEventListener(target,type,listener,useCapture);
        if (listenerWrapper == null) return null;

        super.removeItsNatNormalEventListener(listenerWrapper,updateClient,false);

        return listenerWrapper;
    }

    @Override
    public ItsNatNormalEventListenerWrapperImpl removeItsNatNormalEventListenerById(String id,boolean updateClient)
    {
        ItsNatNormalEventListenerWrapperImpl listenerWrapper = super.removeItsNatNormalEventListenerById(id, updateClient);
        if (listenerWrapper == null) return null;

        eventListenersByTarget.removeItsNatNormalEventListener(listenerWrapper);

        return listenerWrapper;
    }

    public EventListener[] getEventListenersArrayCopy(EventTarget target,String type,boolean useCapture)
    {
        return eventListenersByTarget.getEventListenersArrayCopy(target, type, useCapture);
    }
}
