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
import test.web.shared.EventListenerSerial;

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

    @Override
    public void handleEvent(Event evt)
    {
        final Element logElem = getDocument().getElementById("testEventCaptureLogId");

        Element child = getDocument().getElementById("eventCaptureChildId");
        ((EventTarget)child).addEventListener("click", new EventListenerSerial() {
            @Override
            public void handleEvent(Event evt)
            {
                if (evt.getEventPhase() != Event.AT_TARGET)  throw new RuntimeException("Unexpected event phase " + evt.getEventPhase());

                Element target = (Element)evt.getTarget();
                String tagName = target.getTagName();
                if (!"TextView".equals(tagName)) throw new RuntimeException("Unexpected element " + tagName);
                logToTextView(logElem,"OK 2/2 CHILD at target: " + tagName + " " + target.getAttribute("id"));
            }
        }, true); // Notar que capture ES TRUE

        final Element parent = getDocument().getElementById("eventCaptureParentId");
        ((EventTarget)parent).addEventListener("click", new EventListenerSerial() {
            @Override
            public void handleEvent(Event evt)
            {
                if (evt.getEventPhase() != Event.CAPTURING_PHASE)
                {
                    Element currTarget = (Element)evt.getCurrentTarget();
                    if (currTarget == parent)
                    {
                        itsNatDoc.addCodeToSend("alert(\"Unexpected event phase " + evt.getEventPhase() + " or you are clicking in the read zone (parent elem)\");");
                        return;
                    }
                    throw new RuntimeException("Unexpected event phase " + evt.getEventPhase());
                }                    
                Element currTarget = (Element)evt.getCurrentTarget();
                String tagName = currTarget.getTagName();                
                if (!"FrameLayout".equals(tagName)) throw new RuntimeException("Unexpected element " + tagName);                
                logToTextView(logElem,"OK 1/2 PARENT capturing in current target: " + tagName + " " + currTarget.getAttribute("id") + "\n");
            }
        }, false);

        parent.setAttribute("android:visibility","visible");
    }

}
