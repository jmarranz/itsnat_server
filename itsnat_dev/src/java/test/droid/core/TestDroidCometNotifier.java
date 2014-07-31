/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.CometNotifier;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatCometEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.event.droid.DroidEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import static test.droid.core.TestDroidBase.logToTextView;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestDroidCometNotifier extends TestDroidBase implements EventListener
{
    protected CometNotifier comet;
    
    public TestDroidCometNotifier(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testCometNotifierId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }

    public void handleEvent(final Event evt)
    {
        DroidEvent itsNatEvent = (DroidEvent)evt;        
        
        Document doc = getDocument();        
        final Element logElem = doc.getElementById("testCometNotifier_text_Id");
        
        logToTextView(logElem,"OK " + evt.getType() + " "); // Para que se vea

        if ((comet == null)||comet.isStopped())
        {
            ParamTransport[] extraParams = new ParamTransport[] { new CustomParamTransport("model","android.os.Build.MODEL") };
            String preSendCode = null;
            long eventTimeout = itsNatDoc.getEventTimeout();
            this.comet = itsNatEvent.getClientDocument().createCometNotifier(CommMode.XHR_ASYNC,extraParams,preSendCode,eventTimeout); 
            
            //comet.setExpirationDelay(10000);

            EventListener listener = new EventListenerSerial()
            {
                public void handleEvent(Event evt)
                {
                    String model = (String)((ItsNatCometEvent)evt).getExtraParam("model");
                    if (model == null) throw new RuntimeException("TEST FAIL");
                    logToTextView(logElem, "Comet Event " + evt.getType() + " ");
                }
            };
            comet.addEventListener(listener);

            final CometNotifier comet = this.comet;
            Thread task = new Thread()
            {
                public void run()
                {
                    long t1 = System.currentTimeMillis();
                    long t2 = t1;
                    long timeout = 10*1000;
                    while((t2 - t1) < timeout)
                    {
                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch(InterruptedException ex) { }

                        synchronized(itsNatDoc) 
                        {
                            logToTextView(logElem,"End Comet Task ");
                        }

                        if (comet.isStopped()) // por ejemplo cuando salgamos de la página
                            break;                        

                        comet.notifyClient(); // No es necesario sincronizar con el documento pero no pasaría nada
                        
                        t2 = System.currentTimeMillis();
                    }

                    comet.stop(); // Si ya está parado no hace nada

                    synchronized(itsNatDoc)
                    {
                        logToTextView(logElem,"Stopped Notifier (thread) ");
                        if ((t2 - t1) >= timeout)
                            logToTextView(logElem,"End Thread (timeout) ");
                        else
                            logToTextView(logElem,"End Thread (notifier stopped) ");
                    }
                }
            };

            task.start();

            logToTextView(logElem,"Created Notifier ");
        }
        else
        {
            comet.stop();
            logToTextView(logElem,"Stop Notifier (manual) ");
        }
    }    
}
