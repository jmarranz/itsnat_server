/*
 * TestAsyncServerTask.java
 *
 * Created on 3 de enero de 2007, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.CommMode;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLDocument;
import test.shared.EventListenerSerial;
import test.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestAsyncServerTask extends TestBaseHTMLDocument implements EventListener
{
    protected HTMLButtonElement buttonLock;
    protected HTMLButtonElement buttonNoLock;

    /**
     * Creates a new instance of TestAsyncServerTask
     */
    public TestAsyncServerTask(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        this.buttonLock = (HTMLButtonElement)doc.getElementById("asyncTaskLockTestId");
        itsNatDoc.addEventListener((EventTarget)buttonLock,"click", this, false, CommMode.XHR_SYNC);

        this.buttonNoLock = (HTMLButtonElement)doc.getElementById("asyncTaskNoLockTestId");
        itsNatDoc.addEventListener((EventTarget)buttonLock,"click", this, false, CommMode.XHR_SYNC);
    }

    public void handleEvent(final Event evt)
    {
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        final ClientDocument clientDoc = itsNatEvent.getClientDocument();
        final ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();
        outText("OK " + evt.getType() + " "); // Para que se vea

        final boolean lockDoc = (evt.getCurrentTarget() == buttonLock);

        final EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(final Event evt)
            {
                outText("OK End Async Task Lock Doc: " + lockDoc + " 2-2 ");
            }
        };

        Runnable task = new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                }
                catch(InterruptedException ex) { }

                synchronized(itsNatDoc) // Si se registró la tarea con lockDoc = true entonces es redundante (no hace nada, ya está bloqueado)
                {
                    outText("OK End Async Task Lock Doc: " + lockDoc + " 1-2 ");
                }
            }
        };
        clientDoc.addAsynchronousTask(task,lockDoc,0,null,listener,CommMode.XHR_ASYNC,null,null,-1);
    }

    public void outText(String msg)
    {
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element parent = doc.getElementById("asyncTaskLogId");
        parent.appendChild(doc.createTextNode(msg)); // Para que se vea
    }
}
