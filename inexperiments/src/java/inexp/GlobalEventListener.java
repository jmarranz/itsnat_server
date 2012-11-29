package inexp;

import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
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
            ItsNatServletResponse response = itsNatEvt.getItsNatServletResponse();
            response.addCodeToSend("if (confirm('Session or page was lost. Reload?'))");
            response.addCodeToSend("  window.location.reload(true);");
            itsNatEvt.getItsNatEventListenerChain().stop();
        }
    }
}
