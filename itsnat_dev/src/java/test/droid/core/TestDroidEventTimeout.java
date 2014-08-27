/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
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
public class TestDroidEventTimeout extends TestDroidBase implements EventListener
{
   
    public TestDroidEventTimeout(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testTimeoutId");
        
        itsNatDoc.addEventListener(((EventTarget)testLauncher),"click", this, false,CommMode.XHR_ASYNC_HOLD,(ParamTransport[])null,(String)null,2000);  
    }
    
    public void handleEvent(Event evt)
    {     
        try { Thread.sleep(5000); }
        catch (InterruptedException ex) { throw new RuntimeException(ex);  }        
        
 
    }
    
}
