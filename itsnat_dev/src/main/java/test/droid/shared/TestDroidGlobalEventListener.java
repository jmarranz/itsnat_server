/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.shared;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatEventStateless;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import test.droid.stateless.core.TestDroidStatelessCoreGlobalEventListenerAction;

/**
 *
 * @author jmarranz
 */
public class TestDroidGlobalEventListener implements EventListener,Serializable
{
    protected Object parent;

    public TestDroidGlobalEventListener(Object parent)
    {
        this.parent = parent;
    }

    @Override
    public void handleEvent(Event evt)
    {
        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        if (itsNatEvt instanceof ItsNatEventStateless)
        {
            // REVISAR para Droid
            TestDroidStatelessCoreGlobalEventListenerAction.handleEvent((ItsNatEventStateless)itsNatEvt);
        }
        else
        {
            if (itsNatEvt.getItsNatDocument() == null)
            {
                // Hemos perdido la sesión o la página
                // El ClientDocument es especial para el caso de documento no encontrado
                // aún así algunas cosas funcionan.
                ClientDocument clientDoc = itsNatEvt.getClientDocument();
                clientDoc.addCodeToSend("alert(\"Session or page is lost\");");

                itsNatEvt.getItsNatEventListenerChain().stop();
            }
        }
    }

}
