/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;


public class GlobalEventListener implements EventListener
{
    public GlobalEventListener()
    {
    }

    public void handleEvent(Event evt)
    {
        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        if (itsNatEvt.getItsNatDocument() == null)
        {
            ItsNatServletRequest request = itsNatEvt.getItsNatServletRequest();
            ItsNatServletResponse response = itsNatEvt.getItsNatServletResponse();
            if (BrowserUtil.isUCWEB(request))
                response.addCodeToSend("document.body.innerHTML = 'Session or page was lost';");
            else
            {
                response.addCodeToSend("if (confirm('Session or page was lost. Reload?'))");
                response.addCodeToSend("  window.location.reload(true);");
            }
            itsNatEvt.getItsNatEventListenerChain().stop();
        }
    }
}
