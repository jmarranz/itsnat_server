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
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.event.ItsNatUserEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import test.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestUserListener implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element link;

    /**
     * Creates a new instance of TestAsyncServerResponse
     */
    public TestUserListener(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        this.link = doc.getElementById("userTestId");
        ((EventTarget)link).addEventListener("click",this,false);
    }

    public void handleEvent(final Event evt)
    {
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        HTMLDocument doc = itsNatDoc.getHTMLDocument();

        final String userEvtName = "myUserAction";

        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                ItsNatUserEvent userEvt = (ItsNatUserEvent)evt;
                EventTarget currTarget = userEvt.getCurrentTarget();

                String title = (String)userEvt.getExtraParam("title");
                String[] multivalue = (String[])userEvt.getExtraParamMultiple("multivalue");                
                Text text = (Text)link.getFirstChild();
                text.setData(text.getData() + " => title: " + title + ", multivalue: " + multivalue[0] + "-" + multivalue[1]);

                itsNatDoc.removeUserEventListener(currTarget,userEvtName,this);
            }
        };

        ParamTransport[] extraParams = new ParamTransport[] { new CustomParamTransport("title","document.title"),new CustomParamTransport("multivalue","['one',2]")  };

        itsNatDoc.addUserEventListener((EventTarget)doc,userEvtName,listener,itsNatEvent.getCommMode(),extraParams,null,-1);
        itsNatDoc.addCodeToSend("document.getItsNatDoc().fireUserEvent(document,\"" + userEvtName + "\");");

        itsNatDoc.addUserEventListener(null,userEvtName,listener,itsNatEvent.getCommMode(),extraParams,null,-1);
        itsNatDoc.addCodeToSend("document.getItsNatDoc().fireUserEvent(null,\"" + userEvtName + "\");");

        // El title pasado a través de un ItsNatUserEvent
        itsNatDoc.addUserEventListener((EventTarget)doc.getDocumentElement(),userEvtName,listener,itsNatEvent.getCommMode(),extraParams,null,-1);
        String code = "";
        code += "var userEvt = document.getItsNatDoc().createUserEvent('" + userEvtName + "');";
        code += "userEvt.setExtraParam('title',document.title);";
        code += "userEvt.setExtraParam('multivalue',['one',2]);";        
        code += "document.getItsNatDoc().dispatchUserEvent(document.documentElement,userEvt);";
        itsNatDoc.addCodeToSend(code);
    }
}
