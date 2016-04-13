/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidNativeListeners extends TestDroidBase implements EventListener
{
   
    public TestDroidNativeListeners(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testNativeListenersId");       
        ((EventTarget)testLauncher).addEventListener("click", this, false);        
    }
    
    public TestDroidNativeListeners() // Required for RelProxy
    {    
    }
    
    @Override
    public void handleEvent(Event evt)
    {     

    }
    
}
