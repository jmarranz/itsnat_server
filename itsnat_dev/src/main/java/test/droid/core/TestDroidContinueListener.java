/*
 * TestAsyncServerResponse.java
 *
 * Created on 3 de enero de 2007, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatContinueEvent;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidContinueListener extends TestDroidBase implements EventListener
{
   
    public TestDroidContinueListener(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testContinueEvtListenerId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }


    @Override
    public void handleEvent(final Event evt)
    {
        ItsNatEvent itsNatEvent = (ItsNatEvent)evt;
        ClientDocument clientDoc = itsNatEvent.getClientDocument();

        EventListener listener = new EventListener()
        {
            @Override
            public void handleEvent(Event evt)
            {
                ItsNatContinueEvent contEvt = (ItsNatContinueEvent)evt;
                String manufacturer = (String)contEvt.getExtraParam("manufacturer");
                String model = (String)contEvt.getExtraParam("model");
                itsNatDoc.addCodeToSend("alert(\"OK " + manufacturer + " " + model + "\");");                
            }
        };

        ParamTransport manufacturerParam = new CustomParamTransport("manufacturer","android.os.Build.MANUFACTURER");
        ParamTransport modelParam = new CustomParamTransport("model","android.os.Build.MODEL");        
        clientDoc.addContinueEventListener(null,listener,itsNatEvent.getCommMode(),new ParamTransport[]{ manufacturerParam,modelParam },null,-1);
    }
}
