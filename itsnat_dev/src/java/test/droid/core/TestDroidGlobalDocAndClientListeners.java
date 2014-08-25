/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidGlobalDocAndClientListeners extends TestDroidBase implements EventListener
{
    protected Element testLauncher;
    protected EventListener global_doc;
    protected EventListener global_client;    
    
    public TestDroidGlobalDocAndClientListeners(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        this.testLauncher = getDocument().getElementById("testGlobalListenersId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }
    
    public void handleEvent(Event evt)
    {     
        if (global_doc == null)
        {
            this.global_doc = new EventListener()
            {
                public void handleEvent(Event evt)
                {
                    if (evt.getCurrentTarget() != testLauncher) return;
                    ItsNatEvent itsNatEvt = ((ItsNatEvent)evt);
                    itsNatEvt.setUserValue("test_global_listener_doc",Boolean.TRUE);
                }
            };
            itsNatDoc.addEventListener(global_doc);


            this.global_client = new EventListener()
            {
                public void handleEvent(Event evt)
                {
                    if (evt.getCurrentTarget() != testLauncher) return;
                    ItsNatEvent itsNatEvt = ((ItsNatEvent)evt);
                    itsNatEvt.setUserValue("test_global_listener_client",Boolean.TRUE);
                }
            };
            itsNatDoc.getClientDocumentOwner().addEventListener(global_client); 
            
            itsNatDoc.addCodeToSend("alert(\"Registered globals, click again!!\");");
        }
        else
        {
            ItsNatEvent itsNatEvt = ((ItsNatEvent)evt);
            Boolean objDoc = (Boolean)itsNatEvt.getUserValue("test_global_listener_doc");
            Boolean objClient = (Boolean)itsNatEvt.getUserValue("test_global_listener_client");
            String msg;
            if ((objDoc != null)&&(objClient != null)) msg = "OK";
            else msg = "WROOOOONG";
            itsNatDoc.addCodeToSend("alert(\"" + msg + " \");");
            
            itsNatDoc.removeEventListener(global_doc);
            itsNatDoc.removeEventListener(global_client);
            this.global_doc = null;
            this.global_client = null;
        }
    }
    
}
