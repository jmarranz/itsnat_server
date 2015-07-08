/*
 * OnClickFireEventFromServerTest.java
 *
 * Created on 6 de agosto de 2007, 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;


import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.Document;
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
public class TestGlobalDOMListener implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element link;

    /** Creates a new instance of OnClickFireEventFromServerTest */
    public TestGlobalDOMListener(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        final Document doc = itsNatDoc.getDocument();
        this.link = doc.getElementById("testGlobalDOMListenerId");
        ((EventTarget)link).addEventListener("click",this,false);

        EventListener global = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                if (evt.getCurrentTarget() != link) return;
                ItsNatEvent itsNatEvt = ((ItsNatEvent)evt);
                itsNatEvt.setUserValue("test_global_dom_listener_doc",Boolean.TRUE);
            }
        };
        itsNatDoc.addEventListener(global);


        global = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                if (evt.getCurrentTarget() != link) return;
                ItsNatEvent itsNatEvt = ((ItsNatEvent)evt);
                itsNatEvt.setUserValue("test_global_dom_listener_client",Boolean.TRUE);
            }
        };
        itsNatDoc.getClientDocumentOwner().addEventListener(global);
    }

    public void handleEvent(Event evt)
    {
        ItsNatEvent itsNatEvt = ((ItsNatEvent)evt);
        Boolean objDoc = (Boolean)itsNatEvt.getUserValue("test_global_dom_listener_doc");
        Boolean objClient = (Boolean)itsNatEvt.getUserValue("test_global_dom_listener_client");
        String msg;
        if ((objDoc != null)&&(objClient != null)) msg = "OK";
        else msg = "WROOOOONG";
        Text text = (Text)link.getFirstChild();
        text.setData(text.getData() + " => " + msg);
    }
}
