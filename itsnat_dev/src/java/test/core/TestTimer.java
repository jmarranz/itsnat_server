/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import java.io.Serializable;
import java.util.Date;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.itsnat.core.html.ItsNatHTMLDocument;
import java.util.LinkedList;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ItsNatTimerEvent;
import org.itsnat.core.event.NodePropertyTransport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class TestTimer implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected LinkedList<ItsNatTimerHandle> timerHandleList = new LinkedList<ItsNatTimerHandle>();
    protected ItsNatTimer timerMgr;
    protected HTMLButtonElement addTimerElem;
    protected HTMLInputElement checkBoxElem;
    protected HTMLButtonElement removeTimerElem;
    
    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestTimer(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        this.addTimerElem = (HTMLButtonElement)doc.getElementById("addTimerId");
        ((EventTarget)addTimerElem).addEventListener("click",this,false);

        this.checkBoxElem = (HTMLInputElement)doc.getElementById("fixedRateTimerId");
        itsNatDoc.addEventListener((EventTarget)checkBoxElem,"click",null,false,new NodePropertyTransport("checked",boolean.class));

        ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner();
        this.timerMgr = clientDoc.createItsNatTimer();

        this.removeTimerElem = (HTMLButtonElement)doc.getElementById("removeTimerId");
        ((EventTarget)removeTimerElem).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        EventTarget currentTarget = (EventTarget)evt.getCurrentTarget();
        if (currentTarget == addTimerElem)
        {
            ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
            ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();

            outText("OK timer started "); // Para que se vea

            boolean fixedRate = checkBoxElem.getChecked();

            ItsNatTimerHandle timerHandle;
            if (!fixedRate)
                timerHandle = timerMgr.schedule(null,this,3000,2000);
            else
                timerHandle = timerMgr.scheduleAtFixedRate(null,this,3000,200);
            timerHandleList.add(timerHandle);
        }
        else if (currentTarget == removeTimerElem)
        {
            ItsNatTimerHandle timerHandle = getFirstTimerHandle();
            if (timerHandle != null)
            {
                boolean res = timerHandle.cancel();
                if (res)
                    outText("OK timer canceled "); // Para que se vea
                removeFirstTimerHandle();
            }
        }
        else
        {
            ItsNatTimerEvent timerEvt = (ItsNatTimerEvent)evt;
            ItsNatTimerHandle handle = timerEvt.getItsNatTimerHandle();
            long firstTime = handle.getFirstTime();
            if ((new Date().getTime() - firstTime) > 10000) // to avoid never ending ticks
            {
                handle.cancel();
                outText("Timer canceled (timeout)");
            }
            else 
            {
                long delay = System.currentTimeMillis() - timerEvt.getItsNatTimerHandle().scheduledExecutionTime();
                long period = handle.getPeriod();
                // El delay es para ver lo que ha tardado en procesarse el evento respecto al instante previsto
                // En el caso de fixedRate el periodo de test es pequeño pero la técnica permite "acelerar"
                // el delay podrá superar el periodo alguna vez pero se recuperará (si no es demasiado pequeño)
                String msg = "OK timer, delay: " + delay + " ";
                if (delay >= period) // el retraso choca el instante siguiente evento
                    msg += "(delayed) ";
                outText(msg); // Para que se vea
            }
        }
    }

    public ItsNatTimerHandle getFirstTimerHandle()
    {
        if (timerHandleList.isEmpty()) return null;
        return timerHandleList.getFirst();
    }

    public void removeFirstTimerHandle()
    {
        if (timerHandleList.isEmpty()) return;
        timerHandleList.removeFirst();
    }
    
    public void outText(String msg)
    {
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element parent = doc.getElementById("timerLogId");
        parent.appendChild(doc.createTextNode(msg)); // Para que se vea
    }
}
