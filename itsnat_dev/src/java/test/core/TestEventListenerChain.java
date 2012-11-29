/*
 * OnClickFireEventFromServerTest.java
 *
 * Created on 6 de agosto de 2007, 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;


import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEventListenerChain;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestEventListenerChain implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element link;

    /** Creates a new instance of OnClickFireEventFromServerTest */
    public TestEventListenerChain(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        final Document doc = itsNatDoc.getDocument();
        this.link = doc.getElementById("testEventListenerChainId");
        ((EventTarget)link).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();

        EventListener global = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                ItsNatEvent itsNatEvt = ((ItsNatEvent)evt);
                ItsNatEventListenerChain chain = itsNatEvt.getItsNatEventListenerChain();
                try
                {
                    chain.continueChain();
                }
                catch(Exception ex)
                {
                    if (!ex.getMessage().equals("Must be catched"))
                        throw new RuntimeException("FAILED TEST");

                    Text text = (Text)link.getFirstChild();
                    text.setData(text.getData() + " => OK 1-2");
                    itsNatDoc.removeEventListener(this);
                }
            }
        };
        itsNatDoc.addEventListener(global);  // Para después, cuando se ejecute el evento continue

        EventListener contListener1 = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                throw new RuntimeException("Must be catched");
            }
        };
        clientDoc.addContinueEventListener(null, contListener1);

        EventListener contListener2 = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                Text text = (Text)link.getFirstChild();
                text.setData(text.getData() + " => OK 2-2");
            }
        };
        clientDoc.addContinueEventListener(null, contListener2);
    }
}
