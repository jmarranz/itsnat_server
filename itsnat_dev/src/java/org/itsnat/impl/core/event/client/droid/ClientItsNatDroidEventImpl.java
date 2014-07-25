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

package org.itsnat.impl.core.event.client.droid;

import org.itsnat.impl.core.event.client.*;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.droid.DroidEvent;
import org.itsnat.impl.core.event.EventInternal;
import org.itsnat.impl.core.event.client.dom.domstd.NodeContainerImpl;
import org.itsnat.impl.core.listener.droid.ItsNatDroidEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import static org.w3c.dom.events.Event.AT_TARGET;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ClientItsNatDroidEventImpl extends ClientItsNatNormalEventImpl implements DroidEvent,EventInternal
{
    protected NodeContainerImpl target;
    
    /**
     * Creates a new instance of ClientItsNatDroidEventImpl
     */
    public ClientItsNatDroidEventImpl(ItsNatDroidEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }


    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
    {
        throw new ItsNatException("Not implemented",this);
    }

    public ItsNatDroidEventListenerWrapperImpl getItsNatDroidEventListenerWrapper()
    {
        return (ItsNatDroidEventListenerWrapperImpl)listenerWrapper;
    }


    public EventTarget getTarget()
    {
        if (target == null)
        {
            boolean isCacheIfPossibleTarget = true; // El false es un caso de mutation events de web            
            target = new NodeContainerImpl(getParameterNode("target",isCacheIfPossibleTarget));
        }
        EventTarget evtTarget = (EventTarget)target.get();
        // Si es null es una optimización para evitar enviar el mismo nodo que el currentTarget
        if (evtTarget == null) return getCurrentTarget();
        return evtTarget;
    }
    
    public short getEventPhase()
    {
        return getParameterShort("eventPhase");
    }

    public boolean getCancelable()
    {
        return false; // Por poner algo
    }

    public boolean getBubbles()
    {
        return false; // Por poner algo
    }
}
