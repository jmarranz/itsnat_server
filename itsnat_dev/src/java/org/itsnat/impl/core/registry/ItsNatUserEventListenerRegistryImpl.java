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

import org.itsnat.impl.core.listener.domext.*;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatUserEventListenerRegistryImpl extends ItsNatDOMEventListenerRegistryByTargetTooImpl
{

    /**
     * Creates a new instance of ItsNatUserEventListenerRegistryImpl
     */
    public ItsNatUserEventListenerRegistryImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc)
    {
        super(itsNatDoc,clientDoc);
    }

    public void addItsNatUserEventListener(EventTarget target,String name,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        String type = ItsNatUserEventListenerWrapperImpl.getType(name);
        if (!canAddItsNatDOMEventListener(target,type,listener,false))
            return; // Ya registrado (u otra razón)

        ItsNatUserEventListenerWrapperImpl listenerWrapper = new ItsNatUserEventListenerWrapperImpl(itsNatDoc,clientDocTarget,target,name,listener,commMode,extraParams,preSendCode,eventTimeout,bindToListener);

        addItsNatDOMEventListener(listenerWrapper);
    }

    public ItsNatUserEventListenerWrapperImpl removeItsNatUserEventListener(EventTarget target,String name,EventListener listener,boolean updateClient)
    {
        String type = ItsNatUserEventListenerWrapperImpl.getType(name);
        return (ItsNatUserEventListenerWrapperImpl)removeItsNatDOMEventListener(target,type,listener,false,updateClient);
    }

    public int removeAllItsNatUserEventListeners(EventTarget target,boolean updateClient)
    {
        return removeAllItsNatDOMEventListeners(target,updateClient);
    }

    public EventListener[] getEventListenersByNameArrayCopy(EventTarget target,String name)
    {
        String type = ItsNatUserEventListenerWrapperImpl.getType(name);
        return getEventListenersArrayCopy(target,type,false);
    }

    public ItsNatUserEventListenerWrapperImpl getItsNatUserEventListenerById(String listenerId)
    {
        return (ItsNatUserEventListenerWrapperImpl)getItsNatDOMEventListenerById(listenerId);
    }
}
