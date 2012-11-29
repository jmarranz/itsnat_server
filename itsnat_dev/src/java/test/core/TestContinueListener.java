/*
 * TestAsyncServerResponse.java
 *
 * Created on 3 de enero de 2007, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ItsNatContinueEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ParamTransport;
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
public class TestContinueListener implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element link;

    /**
     * Creates a new instance of TestAsyncServerResponse
     */
    public TestContinueListener(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        this.link = doc.getElementById("continueTestId");
        ((EventTarget)link).addEventListener("click",this,false);
    }

    public void handleEvent(final Event evt)
    {
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ClientDocument clientDoc = itsNatEvent.getClientDocument();

        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                ItsNatContinueEvent contEvt = (ItsNatContinueEvent)evt;
                String title = (String)contEvt.getExtraParam("title");
                Text text = (Text)link.getFirstChild();
                text.setData(text.getData() + " => " + title);
            }
        };

        ParamTransport[] extraParams = new ParamTransport[] { new CustomParamTransport("title","document.title") };
        clientDoc.addContinueEventListener(null,listener,itsNatEvent.getCommMode(),extraParams,null,-1);
    }
}
