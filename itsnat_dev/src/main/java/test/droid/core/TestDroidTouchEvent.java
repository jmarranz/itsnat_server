/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.droid.DroidMotionEvent;
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
    protected Element testLog;

    public TestDroidTouchEvent(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testTouchEventId");
        ((EventTarget)testLauncher).addEventListener("touchstart", this, false);
        ((EventTarget)testLauncher).addEventListener("touchmove", this, false);
        ((EventTarget)testLauncher).addEventListener("touchend", this, false);

        this.testLog = getDocument().getElementById("testTouchEventLogId");
    }

    @Override
    public void handleEvent(Event evt)
    {
        DroidMotionEvent evt2 = (DroidMotionEvent)evt;
        String data = "rawX: " + evt2.getRawX() + " rawY: " + evt2.getRawY() + " x: " + evt2.getX() + " y: " + evt2.getY();

        logToTextView(testLog,"OK " + evt.getType() + " " + data + "\n");
    }

}
