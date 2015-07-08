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
import org.itsnat.impl.core.browser.web.BrowserAdobeSVG;
import org.itsnat.impl.core.browser.web.BrowserGecko;
import org.itsnat.impl.core.browser.web.opera.BrowserOperaOld;
import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.event.DOMStdEventGroupInfo;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.GeckoKeyEventImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.OperaOldKeyEventImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.W3CEventDefaultImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.W3CEventImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.W3CHTMLEventImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.W3CMouseEventImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.W3CMutationEventAdobeSVGImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.W3CMutationEventDefaultImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.W3CUIEventDefaultImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.WebKitKeyEventImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class ClientW3CEventFactory extends ClientItsNatDOMStdEventFactory
{

    /**
     * Creates a new instance of ClientW3CEventFactory
     */
    public ClientW3CEventFactory(RequestNormalEventImpl request)
    {
        super(request);
    }

    public static ClientW3CEventFactory createW3CEventFactory(RequestNormalEventImpl request)
    {
        return new ClientW3CEventFactory(request);
    }

    public ClientItsNatDOMStdEventImpl createClientItsNatDOMStdEvent(int eventGroupCode,ItsNatDOMStdEventListenerWrapperImpl evtListener)
    {
        ClientDocumentImpl clientDoc = request.getClientDocument();

        Browser browser = clientDoc.getBrowser();
        W3CEventImpl event = null;
        switch(eventGroupCode)
        {
            case DOMStdEventGroupInfo.UNKNOWN_EVENT:
                event = new W3CEventDefaultImpl(evtListener,request);
                break;
            case DOMStdEventGroupInfo.UI_EVENT:
                event = new W3CUIEventDefaultImpl(evtListener,request);
                break;
            case DOMStdEventGroupInfo.MOUSE_EVENT:
                event = new W3CMouseEventImpl(evtListener,request);
                break;
            case DOMStdEventGroupInfo.HTML_EVENT:
                event = new W3CHTMLEventImpl(evtListener,request);
                break;
            case DOMStdEventGroupInfo.MUTATION_EVENT:
                if (browser instanceof BrowserAdobeSVG) // ASV v6 (v3 no tiene mutation events)
                    event = new W3CMutationEventAdobeSVGImpl(evtListener,request);
                else
                    event = new W3CMutationEventDefaultImpl(evtListener,request);
                break;
            case DOMStdEventGroupInfo.KEY_EVENT:
                if (browser instanceof BrowserGecko)
                    event = new GeckoKeyEventImpl(evtListener,request);
                else if (browser instanceof BrowserWebKit)
                    event = new WebKitKeyEventImpl(evtListener,request);
                else if (browser instanceof BrowserOperaOld)
                    event = new OperaOldKeyEventImpl(evtListener,request);
                else // Desconocido
                    event = new GeckoKeyEventImpl(evtListener,request);
                break;
        }

        return event;
    }

}
