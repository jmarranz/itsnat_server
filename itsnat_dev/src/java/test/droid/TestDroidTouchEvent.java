/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.droid.DroidMotionEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidTouchEvent extends TestDroidBase implements EventListener
{
   
    public TestDroidTouchEvent(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testTouchEventId");        
        ((EventTarget)testLauncher).addEventListener("touchstart", this, false);
        ((EventTarget)testLauncher).addEventListener("touchmove", this, false);        
        ((EventTarget)testLauncher).addEventListener("touchend", this, false);         
    }
    
    public void handleEvent(Event evt)
    {     
        DroidMotionEvent evt2 = (DroidMotionEvent)evt;
        String data = "rawX: " + evt2.getRawX() + " rawY: " + evt2.getRawY() + " x: " + evt2.getX() + " y: " + evt2.getY();        
        
        itsNatDoc.addCodeToSend("itsNatDoc.alert(\"OK " + evt.getType() + " " + data + "\");");  
    }
    
}
