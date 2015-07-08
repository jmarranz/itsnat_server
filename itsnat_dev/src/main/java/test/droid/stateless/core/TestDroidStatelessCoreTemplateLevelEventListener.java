/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.stateless.core;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ItsNatEventDOMStateless;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class TestDroidStatelessCoreTemplateLevelEventListener implements EventListener,Serializable
{
    protected Object parent;

    public TestDroidStatelessCoreTemplateLevelEventListener(Object parent)
    {
        this.parent = parent;
    }

    public void handleEvent(Event evt)
    {
        ItsNatEventDOMStateless itsNatEvt = (ItsNatEventDOMStateless)evt;        
        ClientDocument clientDoc = itsNatEvt.getClientDocument();
        clientDoc.addCodeToSend("alert(\"OK Template Level Listener\");");         
    }

}
