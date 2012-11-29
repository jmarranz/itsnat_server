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

package org.itsnat.impl.core.event.client.domstd.w3c;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.event.client.domstd.ClientItsNatDOMStdEventImpl;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.event.client.domstd.NodeContainerImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class W3CEventImpl extends ClientItsNatDOMStdEventImpl
{
    // http://cgi.din.or.jp/~hagi3/JavaScript/JSTips/Mozilla/mds.cgi
    // http://en.wikipedia.org/wiki/DOM_Events

    /**
     * Creates a new instance of W3CEventImpl
     */
    public W3CEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);

        checkTampering();
    }

    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
    {
       throw new ItsNatException("Not implemented",this);
    }

    public void resolveNodePaths()
    {
        super.resolveNodePaths();

        getTarget();
    }

    public abstract boolean isCacheIfPossibleTarget();

    public EventTarget getTarget()
    {
        if (target == null)
            target = new NodeContainerImpl(getParameterNode("target",isCacheIfPossibleTarget()));
        return (EventTarget)target.get();
    }

    protected String getTypeFromClient()
    {
        return getParameter("type");
    }

    public short getEventPhase()
    {
        return getParameterShort("eventPhase");
    }

    public boolean getBubbles()
    {
        return getParameterBoolean("bubbles");
    }

    public boolean getCancelable()
    {
        return getParameterBoolean("cancelable");
    }

}
