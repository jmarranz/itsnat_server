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

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ClientItsNatDOMStdEventFactory
{
    protected RequestNormalEventImpl request;

    /**
     * Creates a new instance of ClientItsNatDOMStdEventFactory
     */
    public ClientItsNatDOMStdEventFactory(RequestNormalEventImpl request)
    {
        this.request = request;
    }

    public static ClientItsNatDOMStdEventImpl createClientItsNatDOMStdEvent(ItsNatDOMStdEventListenerWrapperImpl evtListener,RequestNormalEventImpl request)
    {
        ClientDocumentImpl clientDoc = request.getClientDocument();
        Browser browser = clientDoc.getBrowser();
        ClientItsNatDOMStdEventFactory factory;
        if (browser instanceof BrowserMSIEOld)
            factory = ClientMSIEOldEventFactory.createMSIEOldEventFactory(request);
        else
            factory = ClientW3CEventFactory.createW3CEventFactory(request);

        int eventGroupCode = evtListener.getEventGroupCode();

        return factory.createClientItsNatDOMStdEvent(eventGroupCode,evtListener);
    }

    public abstract ClientItsNatDOMStdEventImpl createClientItsNatDOMStdEvent(int eventGroupCode,ItsNatDOMStdEventListenerWrapperImpl evtListener);

}
