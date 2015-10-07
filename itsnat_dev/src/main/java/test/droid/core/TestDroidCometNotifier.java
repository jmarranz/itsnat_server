/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.CometNotifier;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatCometEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.event.droid.DroidEvent;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestDroidCometNotifier extends TestDroidBase implements EventListener
{
    protected CometNotifier comet;
    protected Element outElem;
    
    public TestDroidCometNotifier(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testCometNotifierId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
        
        this.outElem = getDocument().getElementById("testCometNotifierLogId");        
    }

    @Override
    public void handleEvent(final Event evt)
    {
        DroidEvent itsNatEvent = (DroidEvent)evt;        
        
        logToTextView(outElem,"OK " + evt.getType() + " "); // Para que se vea

        if ((comet == null)||comet.isStopped())
        {
            ParamTransport[] extraParams = new ParamTransport[] { new CustomParamTransport("model","android.os.Build.MODEL") };
            String preSendCode = null;
            long eventTimeout = itsNatDoc.getEventTimeout();
            this.comet = itsNatEvent.getClientDocument().createCometNotifier(CommMode.XHR_ASYNC,extraParams,preSendCode,eventTimeout); 
            
            //comet.setExpirationDelay(10000);

            EventListener listener = new EventListenerSerial()
            {
                @Override
                public void handleEvent(Event evt)
                {
                    String model = (String)((ItsNatCometEvent)evt).getExtraParam("model");
                    if (model == null) throw new RuntimeException("TEST FAIL");
                    logToTextView(outElem, "Comet Event " + evt.getType() + " ");
                }
            };
            comet.addEventListener(listener);

            final CometNotifier comet = this.comet;
            Thread task = new Thread()
            {
                @Override
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
                            logToTextView(outElem,"End Comet Task ");
                        }

                        if (comet.isStopped()) // por ejemplo cuando salgamos de la página
                            break;                        

                        comet.notifyClient(); // No es necesario sincronizar con el documento pero no pasaría nada
                        
                        t2 = System.currentTimeMillis();
                    }

                    comet.stop(); // Si ya está parado no hace nada

                    synchronized(itsNatDoc)
                    {
                        logToTextView(outElem,"Stopped Notifier (thread) ");
                        if ((t2 - t1) >= timeout)
                            logToTextView(outElem,"End Thread (timeout) ");
                        else
                            logToTextView(outElem,"End Thread (notifier stopped) ");
                    }
                }
            };

            task.start();

            logToTextView(outElem,"Created Notifier ");
        }
        else
        {
            comet.stop();
            logToTextView(outElem,"Stop Notifier (manual) ");
        }
    }    
}
