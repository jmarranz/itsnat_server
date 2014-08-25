/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidEventCapture extends TestDroidBase implements EventListener
{
   
    public TestDroidEventCapture(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testEventCaptureId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }
    
    public void handleEvent(Event evt)
    {     
       
        Element child = getDocument().getElementById("eventCaptureChildId");          
        ((EventTarget)child).addEventListener("click", new EventListener() {

            public void handleEvent(Event evt)
            {
                itsNatDoc.addCodeToSend("toast(\"CHILD at target: " + (evt.getEventPhase() == Event.AT_TARGET) + " target: " + ((Element)evt.getTarget()).getTagName() + "\");");
            }
        }, true); // Notar que capture ES TRUE
        
        Element parent = getDocument().getElementById("eventCaptureParentId");          
        ((EventTarget)parent).addEventListener("click", new EventListener() {

            public void handleEvent(Event evt)
            {
                itsNatDoc.addCodeToSend("toast(\"PARENT capturing: " + (evt.getEventPhase() == Event.CAPTURING_PHASE) + " target: " + ((Element)evt.getTarget()).getTagName() + "\");");
            }
        }, false);        
        
        parent.setAttribute("android:visibility","visible");
    }
    
}
