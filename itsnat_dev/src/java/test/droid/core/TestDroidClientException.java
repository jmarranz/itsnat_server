/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidClientException extends TestDroidBase implements EventListener
{
   
    public TestDroidClientException(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testClientExceptionId");       
        ((EventTarget)testLauncher).addEventListener("touchstart", this, false);        
    }
    
    public void handleEvent(Event evt)
    {     
        itsNatDoc.addCodeToSend("BAD_CODE();");       
    }
    
}
