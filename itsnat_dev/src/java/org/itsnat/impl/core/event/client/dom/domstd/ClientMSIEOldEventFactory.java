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

package org.itsnat.impl.core.event.client.dom.domstd;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.event.DOMStdEventTypeInfo;
import org.itsnat.impl.core.event.client.dom.domstd.msie.MSIEOldEventDefaultImpl;
import org.itsnat.impl.core.event.client.dom.domstd.msie.MSIEOldEventImpl;
import org.itsnat.impl.core.event.client.dom.domstd.msie.MSIEOldHTMLEventImpl;
import org.itsnat.impl.core.event.client.dom.domstd.msie.MSIEOldKeyEventImpl;
import org.itsnat.impl.core.event.client.dom.domstd.msie.MSIEOldMouseEventImpl;
import org.itsnat.impl.core.event.client.dom.domstd.msie.MSIEOldUIEventDefaultImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class ClientMSIEOldEventFactory extends ClientItsNatDOMStdEventFactory
{

    /**
     * Creates a new instance of ClientMSIEOldEventFactory
     */
    public ClientMSIEOldEventFactory(RequestNormalEventImpl request)
    {
        super(request);
    }

    public static ClientMSIEOldEventFactory createMSIEOldEventFactory(RequestNormalEventImpl request)
    {
        return new ClientMSIEOldEventFactory(request);
    }

    public ClientItsNatDOMStdEventImpl createClientItsNatDOMStdEvent(int typeCode,ItsNatDOMStdEventListenerWrapperImpl evtListener)
    {
        MSIEOldEventImpl event = null;
        switch(typeCode)
        {
            case DOMStdEventTypeInfo.UNKNOWN_EVENT:
                event = new MSIEOldEventDefaultImpl(evtListener,request);
                break;
            case DOMStdEventTypeInfo.UI_EVENT:
                event = new MSIEOldUIEventDefaultImpl(evtListener,request);
                break;
            case DOMStdEventTypeInfo.MOUSE_EVENT:
                event = new MSIEOldMouseEventImpl(evtListener,request);
                break;
            case DOMStdEventTypeInfo.HTML_EVENT:
                event = new MSIEOldHTMLEventImpl(evtListener,request);
                break;
            case DOMStdEventTypeInfo.MUTATION_EVENT:
                throw new ItsNatException("MSIE does not support Mutation Events");
                // break;
            case DOMStdEventTypeInfo.KEY_EVENT:
                event = new MSIEOldKeyEventImpl(evtListener,request);
                break;
        }
        return event;
    }

}
