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

package org.itsnat.impl.core.event.client.dom.domext;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatDOMExtEvent;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatDOMExtEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ClientItsNatDOMExtEventImpl extends ClientItsNatNormalEventImpl implements ItsNatDOMExtEvent
{
    /**
     * Creates a new instance of ClientItsNatDOMExtEventImpl
     */
    public ClientItsNatDOMExtEventImpl(ItsNatDOMExtEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
    {
        throw new ItsNatException("Not implemented",this);
    }    
    
    public ItsNatDOMExtEventListenerWrapperImpl getDOMExtEventListenerWrapper()
    {
        return (ItsNatDOMExtEventListenerWrapperImpl)listenerWrapper;
    }

    public EventTarget getTarget()
    {
        return getCurrentTarget(); 
    }

    public short getEventPhase()
    {
        return AT_TARGET; // Por poner algo
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
