/*
 * TestFireEventFromServerNoBrowser.java
 *
 * Created on 6 de agosto de 2007, 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.shared;


import test.shared.TestUtil;
import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatContinueEvent;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.core.event.ItsNatNormalEvent;
import org.itsnat.core.event.ItsNatUserEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public class TestFireEventFromServerNoBrowser implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element nodeToClick;
    protected Element link;

    /** Creates a new instance of TestFireEventFromServerNoBrowser */
    public TestFireEventFromServerNoBrowser(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        final Document doc = itsNatDoc.getDocument();
        this.link = doc.getElementById("testServerEventsNoBrowserId");
        ((EventTarget)link).addEventListener("click",this,false);

        this.nodeToClick = doc.getElementById("testServerEventsToClickPressNoBrowserId");

        ((EventTarget)nodeToClick).addEventListener("click",this,false);
        ((EventTarget)nodeToClick).addEventListener("keypress",this,false);
        ((EventTarget)nodeToClick).addEventListener("itsnat:user:test",this,false);

        // Hacemos ejemplo de capturing pues como es en el servidor siempre funcionará
        ((EventTarget)nodeToClick.getParentNode()).addEventListener("click",this,true);

        // Hacemos ejemplo de bubbling a un nodo padre
        ((EventTarget)nodeToClick.getParentNode()).addEventListener("click",this,false);

        // En total se mostrará tres veces el evento click, el primero será capturing y el segundo at target y tercero bubbling
    }

    public void handleEvent(Event evt)
    {
        if (evt.getCurrentTarget() == link)
            fireEvents(((ItsNatEvent)evt).getClientDocument());
        else
            processReceivedEvent(evt);
    }

    public void fireEvents(ClientDocument clientDoc)
    {
        final Document doc = itsNatDoc.getDocument();

        MouseEvent eventClick = (MouseEvent)((DocumentEvent)doc).createEvent("MouseEvents");
        eventClick.initMouseEvent("click",true,true,((DocumentView)doc).getDefaultView(),
                0,100,200,300,400,false,false,false,false,(short)0,null);
        ((ItsNatNormalEvent)eventClick).setExtraParam("extra","Hello");
        boolean res = ((EventTarget)nodeToClick).dispatchEvent(eventClick);

        ItsNatKeyEvent eventKey = (ItsNatKeyEvent)((DocumentEvent)doc).createEvent("KeyEvents");
        eventKey.initKeyEvent("keypress",true,true,((DocumentView)doc).getDefaultView(),
                false,false,false,false,0,'a'); // 'a' = 97
        ((ItsNatNormalEvent)eventKey).setExtraParam("extra","Bye");
        boolean res2 = ((EventTarget)nodeToClick).dispatchEvent(eventKey);

        ItsNatUserEvent userEvent = (ItsNatUserEvent)((DocumentEvent)doc).createEvent("itsnat:UserEvents");
        userEvent.initEvent("itsnat:user:test",false,false);
        userEvent.setExtraParam("extra","UserVal");
        boolean res3 = ((EventTarget)nodeToClick).dispatchEvent(userEvent);

        //clientDoc.addEventListener((EventTarget)nodeToClick,"itsnat:continue",listener,false);
        clientDoc.addContinueEventListener((EventTarget)nodeToClick, this);
        ItsNatContinueEvent continueEvent = (ItsNatContinueEvent)((DocumentEvent)doc).createEvent("itsnat:ContinueEvents");
        continueEvent.initEvent("itsnat:continue",false,false);
        continueEvent.setExtraParam("extra","\"Bye\"");
        // listener.handleEvent(continueEvent); // No usar dispatchEvent, no está soportado
        boolean res4 = ((EventTarget)nodeToClick).dispatchEvent(continueEvent);

        TestUtil.checkError(link.getChildNodes().getLength() > 1); // Los mensajes se añadieron
    }

    public void processReceivedEvent(Event evt)
    {
        final Document doc = itsNatDoc.getDocument();

        String msg;
        if (evt instanceof MouseEvent)
        {
            Element elem = (Element)evt.getCurrentTarget();
            String phase = null;
            if (evt.getEventPhase() == Event.CAPTURING_PHASE)
                phase = "capturing <" + elem.getTagName() + ">";
            else if (evt.getEventPhase() == Event.AT_TARGET)
                phase = "at_target <" + elem.getTagName() + ">";
            else if (evt.getEventPhase() == Event.BUBBLING_PHASE)
                phase = "bubbling <" + elem.getTagName() + ">";

            msg = " click: " + ((MouseEvent)evt).getScreenX() + " phase: " + phase;
            TestUtil.checkError(((MouseEvent)evt).getScreenX() == 100);
        }
        else if (evt instanceof ItsNatKeyEvent)
        {
            msg = " keypress: " + ((ItsNatKeyEvent)evt).getCharCode();
            TestUtil.checkError(((ItsNatKeyEvent)evt).getCharCode() == 'a');
        }
        else
            msg = " " + evt.getType();

        Object extra = ((ItsNatNormalEvent)evt).getExtraParam("extra");
        msg += " " + extra;

        link.appendChild(doc.createTextNode(msg));
        if (!(evt instanceof ItsNatContinueEvent)) // El continue event es el último
            link.appendChild(doc.createTextNode(", "));
    }
}
