/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import static test.droid.core.TestDroidBase.ANDROID_NS;

/**
 *
 * @author jmarranz
 */
public class TestDroidAsyncServerTask extends TestDroidBase implements EventListener
{
   
    public TestDroidAsyncServerTask(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testAsyncTaskId");    
        itsNatDoc.addEventListener((EventTarget)testLauncher,"click", this, false, CommMode.XHR_ASYNC);        
    }
    
    public void handleEvent(Event evt)
    {     
        ItsNatEvent evt2 = (ItsNatEvent)evt;
        ClientDocument clientDoc = evt2.getClientDocument();
        Document doc = getDocument();
        //Element testLauncherHidden = doc.getElementById("testAsyncTaskHiddenId");  
        
        final Element outElem = doc.getElementById("testAsyncTask_text_Id");         
        
        
        
        final EventListener listener = new EventListener()
        {
            public void handleEvent(final Event evt)
            {
                //itsNatDoc.addCodeToSend("itsNatDoc.alert(\"OK End Async Task Lock Doc 2/2\");");
                
                String text = outElem.getAttributeNS(ANDROID_NS, "text");
                text += "OK 2/2 ";
                outElem.setAttributeNS(ANDROID_NS, "text",text);                
            }
        };

        Runnable task = new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(3000);
                }
                catch(InterruptedException ex) { }

                synchronized(itsNatDoc) // Obligatorio, lockDoc es false
                {
                    //itsNatDoc.addCodeToSend("itsNatDoc.alert(\"OK End Async Task Lock Doc 1/2\");");
                    
                    String text = outElem.getAttributeNS(ANDROID_NS, "text");
                    text += "OK 1/2 ";
                    outElem.setAttributeNS(ANDROID_NS, "text",text);                     
                }
            }
        };
        boolean lockDoc = false; int maxWait = 0;
        clientDoc.addAsynchronousTask(task,lockDoc,maxWait,null,listener,CommMode.XHR_ASYNC,null,null,-1);  
    }
    
}
