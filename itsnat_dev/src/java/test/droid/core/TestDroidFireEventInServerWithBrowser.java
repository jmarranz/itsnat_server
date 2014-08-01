/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.core.event.droid.DroidMotionEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.DocumentView;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestDroidFireEventInServerWithBrowser extends TestDroidBase implements EventListener
{
    protected Element testLauncher;
    protected Element testLauncherHidden;
    protected Element logElem;
    
    public TestDroidFireEventInServerWithBrowser(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Document doc = getDocument();
        
        this.testLauncher = doc.getElementById("testFireEventInServerWithBrowserId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
        
        this.testLauncherHidden = doc.getElementById("testFireEventInServerWithBrowserHiddenId");               
        ((EventTarget)testLauncherHidden).addEventListener("click",this,false);
        ((EventTarget)testLauncherHidden).addEventListener("keypress",this,false);
        ((EventTarget)testLauncherHidden).addEventListener("blur",this,false);
        ((EventTarget)testLauncherHidden).addEventListener("itsnat:user:test",this,false);
        
        this.logElem = doc.getElementById("testFireEventInServerWithBrowser_text_Id");
    }
    

    public void handleEvent(Event evt)
    {
        if (evt.getCurrentTarget() == testLauncher)
            fireEvents(((ItsNatEvent)evt).getClientDocument());
        else
            processReceivedEvent(evt);
    }


    public void fireEvents(ClientDocument client)
    {
        final Document doc = itsNatDoc.getDocument();

        final Element testLauncher = this.testLauncher;
        Runnable thread = new Runnable()
        {
            public void run()
            {
                // Al ser un hilo diferente el document no está sincronizado (no debe estarlo)

                DroidMotionEvent eventClick;
                synchronized(itsNatDoc) // no hace falta pero por si acaso
                {
                    eventClick = (DroidMotionEvent)((DocumentEvent)doc).createEvent("MotionEvent");
                }        
                eventClick.initEvent("click",false,false);
                eventClick.setRawX(1);
                eventClick.setRawY(2);                
                eventClick.setX(3);
                eventClick.setY(4);                

                boolean res = ((EventTarget)testLauncherHidden).dispatchEvent(eventClick);                
                
                /*
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

                */        
                        
                // No hace testear/disparar ContinueEvent pues se dispara
                // solo al registrar un continue listener

                synchronized(itsNatDoc)
                {
                    //TestUtil.checkError(testLauncher.getChildNodes().getLength() > 1); // Los mensajes se añadieron
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
            msg = " click: " + ((DroidMotionEvent)evt).getX();
            TestUtil.checkError(((DroidMotionEvent)evt).getX() == 3);
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

        logToTextView(logElem,msg);
        
        /*
        link.appendChild(doc.createTextNode(msg));
        if (!(evt instanceof ItsNatUserEvent)) // El user event es el último
            link.appendChild(doc.createTextNode(", "));
                */
    }
    
}
