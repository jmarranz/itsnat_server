/*
 * TestAsyncServerResponse.java
 *
 * Created on 3 de enero de 2007, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import java.io.Serializable;
import org.itsnat.core.CometNotifier;
import org.itsnat.core.CommMode;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestCometNotifier implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element startStopElem;
    protected CometNotifier comet;

    /**
     * Creates a new instance of TestAsyncServerResponse
     */
    public TestCometNotifier(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        this.startStopElem = doc.getElementById("cometNotifierTestId");
        itsNatDoc.addEventListener((EventTarget)startStopElem,"click", this, false,CommMode.XHR_SYNC);
    }

    public void handleEvent(final Event evt)
    {
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        final ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();
        outText("OK " + evt.getType() + " "); // Para que se vea

        if ((comet == null)||comet.isStopped())
        {
            this.comet = itsNatEvent.getClientDocument().createCometNotifier();
            //comet.setExpirationDelay(10000);

            EventListener listener = new EventListenerSerial()
            {
                public void handleEvent(Event evt)
                {
                    outText("OK Comet Event " + evt.getType() + " ");
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
                            outText("OK End Comet Task ");
                        }

                        if (comet.isStopped()) // por ejemplo cuando salgamos de la página
                            break;                        

                        comet.notifyClient(); // No es necesario sincronizar con el documento pero no pasaría nada
                        
                        t2 = System.currentTimeMillis();
                    }

                    comet.stop(); // Si ya está parado no hace nada
                    outText("Stop Notifier (thread) ");

                    synchronized(itsNatDoc)
                    {
                        if ((t2 - t1) >= timeout)
                            outText("End Thread (timeout) ");
                        else
                            outText("End Thread (notifier stopped) ");
                    }
                }
            };

            task.start();

            outText("Created Notifier ");
        }
        else
        {
            comet.stop();
            outText("Stop Notifier (manual) ");
        }
    }

    public void outText(String msg)
    {
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element parent = doc.getElementById("cometLogId");
        parent.appendChild(doc.createTextNode(msg)); // Para que se vea
    }
}
