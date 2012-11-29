/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.core.listeners;

import java.util.Date;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.event.ItsNatTimerEvent;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class TimersTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkToStart;
    protected ItsNatTimer timer;

    public TimersTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkToStart = doc.getElementById("linkToStartId");

        ((EventTarget)linkToStart).addEventListener("click",this,false);

        ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner();
        this.timer = clientDoc.createItsNatTimer();
    }

    public void endExamplePanel()
    {
        ((EventTarget)linkToStart).removeEventListener("click",this,false);

        timer.cancel();
        this.timer = null;

        this.linkToStart = null;
    }

    public void handleEvent(Event evt)
    {
        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ItsNatTimerEvent timerEvt = (ItsNatTimerEvent)evt;
                ItsNatTimerHandle handle = timerEvt.getItsNatTimerHandle();
                long firstTime = handle.getFirstTime();
                if ((new Date().getTime() - firstTime) > 10000) // to avoid never ending ticks
                {
                    handle.cancel();
                    log("Scheduled task canceled, id: " + handle.hashCode());
                }
                else log("Tick, id: " + handle.hashCode() + " next execution: " + new Date(handle.scheduledExecutionTime()));
            }
        };
        ItsNatTimerHandle handle = timer.schedule(null,listener,1000,2000);

        log("Scheduled task started, id: " + handle.hashCode() + " first time: " + new Date(handle.getFirstTime()) + " period: " + handle.getPeriod());
    }

}
