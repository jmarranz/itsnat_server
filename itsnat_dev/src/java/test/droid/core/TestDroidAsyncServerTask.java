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
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidAsyncServerTask extends TestDroidBase implements EventListener
{
    protected Element outElem;
    
    public TestDroidAsyncServerTask(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testAsyncTaskId");    
        itsNatDoc.addEventListener((EventTarget)testLauncher,"click", this, false, CommMode.XHR_ASYNC);        
        
        this.outElem = getDocument().getElementById("testAsyncTask_text_Id");         
    }
    
    public void handleEvent(Event evt)
    {     
        ItsNatEvent evt2 = (ItsNatEvent)evt;
        ClientDocument clientDoc = evt2.getClientDocument(); 
        
        final EventListener listener = new EventListener()
        {
            public void handleEvent(final Event evt)
            {
                //itsNatDoc.addCodeToSend("itsNatDoc.alert(\"OK End Async Task Lock Doc 2/2\");");
                logToTextView(outElem,"OK 2/2 ");             
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
                    logToTextView(outElem,"OK 1/2 ");                   
                }
            }
        };
        boolean lockDoc = false; int maxWait = 0;
        clientDoc.addAsynchronousTask(task,lockDoc,maxWait,null,listener,CommMode.XHR_ASYNC,null,null,-1);  
    }
    
}
