/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.shared;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ItsNatEventStateless;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class TestCoreStatelessEventListener implements EventListener,Serializable
{
    protected Object parent;

    public TestCoreStatelessEventListener(Object parent)
    {
        this.parent = parent;
    }

    public void handleEvent(Event evt)
    {
        ItsNatEventStateless itsNatEvt = (ItsNatEventStateless)evt;
        ClientDocument clientDoc = itsNatEvt.getClientDocument();
        clientDoc.addCodeToSend("alert('BIEN STATELESS EVENT');");
    }

}
