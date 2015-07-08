/*
 * OnClickAddRowListenerExample.java
 *
 * Created on 2 de octubre de 2006, 21:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestEventCapture implements Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element element;

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestEventCapture(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        this.element = itsNatDoc.getDocument().getElementById("eventCaptureId"); // <a>
        Element parent = (Element)element.getParentNode(); // parent

        EventListener listener;

        listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                addToChild(" At Target");
            }
        };

        ((EventTarget)element).addEventListener("click",listener,false);

        listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                addToChild(" Captured");
            }
        };
        ((EventTarget)parent).addEventListener("click",listener,true); // Captura

        listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                addToChild(" Bubble");
            }
        };
        ((EventTarget)parent).addEventListener("click",listener,false); // Bubbling
    }

    public void addToChild(String msg)
    {
        Text text = itsNatDoc.getDocument().createTextNode(msg);
        element.appendChild(text);
    }
}
