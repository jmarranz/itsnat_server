/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.core.event.droid.DroidFocusEvent;
import org.itsnat.core.event.droid.DroidKeyEvent;
import org.itsnat.core.event.droid.DroidMotionEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
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
        ((EventTarget)testLauncherHidden).addEventListener("touchend",this,false);          
        ((EventTarget)testLauncherHidden).addEventListener("keydown",this,false);        
        ((EventTarget)testLauncherHidden).addEventListener("blur",this,false);
        if (false) ((EventTarget)testLauncherHidden).addEventListener("itsnat:user:test",this,false);                 // Los dos modos valen
        else itsNatDoc.getClientDocumentOwner().addUserEventListener((EventTarget)testLauncherHidden, "test", this);  //    "
            
        this.logElem = doc.getElementById("testFireEventInServerWithBrowser_text_Id");
    }
    

    public void handleEvent(Event evt)
    {
        if (evt.getCurrentTarget() == testLauncher)
            fireEvents(((ItsNatEvent)evt).getClientDocument());
        else if (evt.getCurrentTarget() == testLauncherHidden)
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

                DroidMotionEvent eventClick;
                synchronized(itsNatDoc) // no hace falta pero por si acaso
                {
                    eventClick = (DroidMotionEvent)((DocumentEvent)doc).createEvent("MotionEvent");
                }        
                eventClick.initEvent("click",true,true);              
                eventClick.setX(3);
                eventClick.setY(4);
                boolean res = ((EventTarget)testLauncherHidden).dispatchEvent(eventClick);                
                
                synchronized(itsNatDoc) // no hace falta pero por si acaso
                {
                    eventClick = (DroidMotionEvent)((DocumentEvent)doc).createEvent("MotionEvent");
                }        
                eventClick.initEvent("touchend",true,true);              
                eventClick.setX(5);
                eventClick.setY(6);
                res = ((EventTarget)testLauncherHidden).dispatchEvent(eventClick);                
                
                DroidKeyEvent eventKey;
                synchronized(itsNatDoc) // no hace falta pero por si acaso
                {
                    eventKey = (DroidKeyEvent)((DocumentEvent)doc).createEvent("KeyEvent");
                }        
                eventKey.initEvent("keydown",true,true);              
                eventKey.setKeyCode(32);
                res = ((EventTarget)testLauncherHidden).dispatchEvent(eventKey);                 
                
                
                DroidFocusEvent focusKey;
                synchronized(itsNatDoc) // no hace falta pero por si acaso
                {
                    focusKey = (DroidFocusEvent)((DocumentEvent)doc).createEvent("FocusEvent");
                }        
                focusKey.initEvent("blur",true,true);              
                focusKey.setFocus(true);
                res = ((EventTarget)testLauncherHidden).dispatchEvent(focusKey);                
                
                
                ItsNatUserEvent userEvent;
                synchronized(itsNatDoc) // no hace falta pero por si acaso
                {
                    userEvent = (ItsNatUserEvent)((DocumentEvent)doc).createEvent("itsnat:UserEvents");
                }
                userEvent.initEvent("itsnat:user:test",false,false);
                userEvent.setExtraParam("extra","\"Hello\"");
                res = ((EventTarget)testLauncherHidden).dispatchEvent(userEvent);

                        
                // No hace testear/disparar ContinueEvent pues se dispara
                // solo al registrar un continue listener

                synchronized(itsNatDoc)
                {
                    TestUtil.checkError(getLogTextView(logElem).length() > 1); // Los mensajes se añadieron
                }
            }
        };
        client.startEventDispatcherThread(thread);
    }

    public void processReceivedEvent(Event evt)
    {
        //DroidEvent evt2 = (DroidEvent)evt;
        //Document doc = itsNatDoc.getDocument();

        String type = evt.getType();
        String msg = "";
        if (type.equals("click"))
        {
            DroidMotionEvent evt2 = (DroidMotionEvent)evt;
            msg = " click: x: " + evt2.getX() + " rawX:" + evt2.getRawX();
            TestUtil.checkError(evt2.getX() == 3 && evt2.getRawX() == 3); // RawX e Y se obtienen nativamente de Android y en el caso de click serán los mismos que X e Y
        }
        if (type.equals("touchend"))
        {
            DroidMotionEvent evt2 = (DroidMotionEvent)evt;
            msg = " touchend: x: " + evt2.getX() + " rawX:" + evt2.getRawX();
            TestUtil.checkError(evt2.getX() == 5 && evt2.getRawX() == 5); // RawX e Y se obtienen nativamente de Android y en el caso de click serán los mismos que X e Y
        }        
        else if (type.equals("keydown"))
        {
            DroidKeyEvent evt2 = (DroidKeyEvent)evt;
            msg = " keydown: keyCode: " + evt2.getKeyCode();
            TestUtil.checkError(evt2.getKeyCode() == 32); 
        }
        else if (type.equals("blur"))
        {
            DroidFocusEvent evt2 = (DroidFocusEvent)evt;
            msg = " blur: hasFocus: " + evt2.hasFocus();
            TestUtil.checkError(evt2.hasFocus() == true); 
        }
        else if (evt instanceof ItsNatUserEvent)
            msg = " " + evt.getType() + " " + ((ItsNatUserEvent)evt).getExtraParam("extra");

        logToTextView(logElem,msg);

    }
    
}
