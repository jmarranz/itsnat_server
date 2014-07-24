/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidEventPreSendCode extends TestDroidBase implements EventListener
{
   
    public TestDroidEventPreSendCode(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testEventPreSendId");
        String preSendCode = "itsNatDoc.alert(\"OK \" + event);";
        itsNatDoc.addEventListener((EventTarget)testLauncher, "click", this, false, preSendCode);
    }
    
    public void handleEvent(Event evt)
    {     
        // Nada que hacer
    }
    
}
