/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import java.util.Date;
import java.util.LinkedList;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.event.ItsNatTimerEvent;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.event.droid.DroidEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestDroidTimerListener extends TestDroidBase implements EventListener
{
    protected Element addTimerElem;
    protected Element checkBoxElem;     
    protected Element removeTimerElem;    
    protected boolean fixedRate;
    protected ItsNatTimer timerMgr;
    protected LinkedList<ItsNatTimerHandle> timerHandleList = new LinkedList<ItsNatTimerHandle>();    
    protected Element outElem;
    
    public TestDroidTimerListener(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);
       
        Document doc = itsNatDoc.getDocument();
        this.addTimerElem = doc.getElementById("testTimerAddId");
        ((EventTarget)addTimerElem).addEventListener("click",this,false);

        this.checkBoxElem = doc.getElementById("testTimerFixedRateId");
        EventListener fixedRateListener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                fixedRate = Boolean.parseBoolean((String)((DroidEvent)evt).getExtraParam("isChecked()"));
            }
        };
        itsNatDoc.addEventListener((EventTarget)checkBoxElem,"click",fixedRateListener,false,new NodePropertyTransport("isChecked()",false));

        ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner();
        this.timerMgr = clientDoc.createItsNatTimer();

        this.removeTimerElem = doc.getElementById("testTimerRemoveId");
        ((EventTarget)removeTimerElem).addEventListener("click",this,false);
        
        this.outElem = getDocument().getElementById("testTimer_text_Id");        
    }
    
    public void handleEvent(Event evt)
    {     
        
        EventTarget currentTarget = (EventTarget)evt.getCurrentTarget();
        if (currentTarget == addTimerElem)
        {
            DroidEvent itsNatEvent = (DroidEvent)evt;
            ItsNatDocument itsNatDoc = itsNatEvent.getItsNatDocument();

            logToTextView(outElem,"OK timer started "); // Para que se vea

            //boolean fixedRate = false; //checkBoxElem.getChecked();

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
                    logToTextView(outElem,"OK timer canceled "); // Para que se vea
                removeFirstTimerHandle();
            }
        }
        else
        {
            ItsNatTimerEvent timerEvt = (ItsNatTimerEvent)evt;
            ItsNatTimerHandle handle = timerEvt.getItsNatTimerHandle();
            long firstTime = handle.getFirstTime();
            if ((new Date().getTime() - firstTime) > 15000) // to avoid never ending ticks
            {
                handle.cancel();
                logToTextView(outElem,"Timer canceled (timeout)");
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
                logToTextView(outElem,msg); // Para que se vea
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

}
