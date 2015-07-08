/*
 * TestFireEventFromServerWithBrowser.java
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
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatKeyEvent;
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
public class TestFireEventFromServerWithBrowser implements EventListener,Serializable
{
    protected final ItsNatDocument itsNatDoc;
    protected boolean svg;
    protected Element link;
    protected Element nodeToClick;

    /**
     * Creates a new instance of TestFireEventFromServerWithBrowser
     */
    public TestFireEventFromServerWithBrowser(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        final Document doc = itsNatDoc.getDocument();
        this.link = doc.getElementById("testServerEventsWithBrowserId");
        ((EventTarget)link).addEventListener("click",this,false);

        this.nodeToClick = doc.getElementById("testServerEventsToClickPressId");

        ((EventTarget)nodeToClick).addEventListener("click",this,false);
        ((EventTarget)nodeToClick).addEventListener("keypress",this,false);
        ((EventTarget)nodeToClick).addEventListener("blur",this,false);
        ((EventTarget)nodeToClick).addEventListener("itsnat:user:test",this,false);
    }

    public void handleEvent(Event evt)
    {
        if (evt.getCurrentTarget() == link)
            fireEvents(((ItsNatEvent)evt).getClientDocument());
        else
            processReceivedEvent(evt);
    }


    public void fireEvents(ClientDocument client)
    {
        final Document doc = itsNatDoc.getDocument();

        Runnable thread = new Runnable()
        {
            public void run()
            {
                // Al ser un hilo diferente el document no está sincronizado (no debe estarlo)

                MouseEvent eventClick;
                synchronized(itsNatDoc) // no hace falta pero por si acaso
                {
                    eventClick = (MouseEvent)((DocumentEvent)doc).createEvent("MouseEvents");
                }
                eventClick.initMouseEvent("click",true,true,((DocumentView)doc).getDefaultView(),
                        0,100,200,300,400,false,false,false,false,(short)0,null);
                boolean res = ((EventTarget)nodeToClick).dispatchEvent(eventClick);

                ItsNatKeyEvent eventKey;
                synchronized(itsNatDoc) // no hace falta pero por si acaso
                {
                    eventKey = (ItsNatKeyEvent)((DocumentEvent)doc).createEvent("KeyEvents");
                }
                eventKey.initKeyEvent("keypress",true,true,((DocumentView)doc).getDefaultView(),
                        false,false,false,false,0,'a'); // 'a' = 97
                boolean res2 = ((EventTarget)nodeToClick).dispatchEvent(eventKey);

                Event eventChange;
                synchronized(itsNatDoc) // no hace falta pero por si acaso
                {
                    eventChange = (Event)((DocumentEvent)doc).createEvent("HTMLEvents");
                }
                eventChange.initEvent("blur",true,true);
                boolean res3 = ((EventTarget)nodeToClick).dispatchEvent(eventChange);

                ItsNatUserEvent userEvent;
                synchronized(itsNatDoc) // no hace falta pero por si acaso
                {
                    userEvent = (ItsNatUserEvent)((DocumentEvent)doc).createEvent("itsnat:UserEvents");
                }
                userEvent.initEvent("itsnat:user:test",false,false);
                userEvent.setExtraParam("extra","\"Hello\"");
                boolean res4 = ((EventTarget)nodeToClick).dispatchEvent(userEvent);

                // No hace testear/disparar ContinueEvent pues se dispara
                // solo al registrar un continue listener

                synchronized(itsNatDoc)
                {
                    TestUtil.checkError(link.getChildNodes().getLength() > 1); // Los mensajes se añadieron
                }
            }
        };
        client.startEventDispatcherThread(thread);
    }

    public void processReceivedEvent(Event evt)
    {
        Document doc = itsNatDoc.getDocument();

        String type = evt.getType();
        String msg = "";
        if (type.equals("click"))
        {
            msg = " click: " + ((MouseEvent)evt).getScreenX();
            TestUtil.checkError(((MouseEvent)evt).getScreenX() == 100);
        }
        else if (type.equals("keypress"))
        {
            msg = " keypress: " + ((ItsNatKeyEvent)evt).getCharCode();
            TestUtil.checkError(((ItsNatKeyEvent)evt).getCharCode() == 'a');
        }
        else if (type.equals("blur"))
        {
            msg = " blur";
        }
        else if (evt instanceof ItsNatUserEvent)
            msg = " " + evt.getType() + " " + ((ItsNatUserEvent)evt).getExtraParam("extra");

        link.appendChild(doc.createTextNode(msg));
        if (!(evt instanceof ItsNatUserEvent)) // El user event es el último
            link.appendChild(doc.createTextNode(", "));
    }

}
