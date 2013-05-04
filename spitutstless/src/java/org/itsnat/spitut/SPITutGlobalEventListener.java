
package org.itsnat.spitut;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class SPITutGlobalEventListener implements EventListener
{
    public SPITutGlobalEventListener()
    {
    }

    public void handleEvent(Event evt)
    {
        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        if (itsNatEvt.getItsNatDocument() == null)
        {
            StringBuffer code = new StringBuffer();
            code.append("if (confirm('Expired session. Reload?'))");
            code.append("  window.location.reload(true);");
            ClientDocument clientDoc = itsNatEvt.getClientDocument();
            clientDoc.addCodeToSend(code.toString());
            itsNatEvt.getItsNatEventListenerChain().stop();
        }
    }
}
