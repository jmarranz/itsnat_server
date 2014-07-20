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

package org.itsnat.impl.core.registry.droid;

import org.itsnat.impl.core.registry.dom.ItsNatNormalEventListenerRegistryByTargetTooImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.NodeMutationTransport;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.listener.droid.ItsNatDroidEventListenerWrapperImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatDroidEventListenerRegistryImpl extends ItsNatNormalEventListenerRegistryByTargetTooImpl
{

    /**
     * Creates a new instance of ItsNatDOMStdEventListenerRegistryImpl
     */
    public ItsNatDroidEventListenerRegistryImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc)
    {
        super(itsNatDoc,clientDoc);
    }

    @Override
    public boolean isValidEventTarget(EventTarget target,boolean throwErr)
    {
        if (target == null)
            if (throwErr)
                throw new ItsNatException("Null event target is not allowed");
            else
                return false;

        return super.isValidEventTarget(target,throwErr);
    }

    public void addItsNatDroidEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        if (!canAddItsNatNormalEventListener(target,type,listener,useCapture))
            return; // Ya registrado (u otra razón)

        ItsNatDroidEventListenerWrapperImpl listenerWrapper = new ItsNatDroidEventListenerWrapperImpl(itsNatDoc,clientDocTarget,target,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);

        addItsNatNormalEventListener(listenerWrapper);
    }

    public ItsNatDroidEventListenerWrapperImpl removeItsNatDroidEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        if (!isValidEventTarget(target,false)) return null; // No pudo registrarse, nos ahorramos una búsqueda inútil

        return (ItsNatDroidEventListenerWrapperImpl)removeItsNatNormalEventListener(target,type,listener,useCapture,updateClient);
    }

    public int removeAllItsNatDOMStdEventListeners(EventTarget target,boolean updateClient)
    {
        return removeAllItsNatNormalEventListeners(target,updateClient);
    }

    public ItsNatDroidEventListenerWrapperImpl getItsNatDroidEventListenerById(String listenerId)
    {
        return (ItsNatDroidEventListenerWrapperImpl)getItsNatNormalEventListenerById(listenerId);
    }

}
