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

package org.itsnat.feashow.features.core.misc.remctrl;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.event.ItsNatTimerEvent;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class RemoteControlTimerMgrGlobalEventListener implements EventListener
{
    public static final int PERIOD = 10000; // Each 10 seconds
    protected ItsNatDocument itsNatDoc;
    protected long lastNonTimerEvent;
    protected ItsNatTimer timer;
    protected ItsNatTimerHandle timerHandle;

    public RemoteControlTimerMgrGlobalEventListener(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        this.lastNonTimerEvent = System.currentTimeMillis();

        ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner();
        this.timer = clientDoc.createItsNatTimer();
        scheduleTimer();
    }

    public long getLastNonTimerEvent()
    {
        return lastNonTimerEvent;
    }

    public void scheduleTimer()
    {
        RemoteControlTimerEventListener listener = new RemoteControlTimerEventListener(this);
        this.timerHandle = timer.schedule(null,listener,PERIOD,PERIOD); // Each 10 seconds
    }

    public void handleEvent(Event evt)
    {
        if (evt instanceof ItsNatTimerEvent)
        {
            ItsNatTimerEvent timerEvt = (ItsNatTimerEvent)evt;
            if (timerEvt.getItsNatTimerHandle() == timerHandle)
                return;
        }

        this.lastNonTimerEvent = System.currentTimeMillis();

        if (timerHandle.isCancelled())
        {
            // The user seems active again
            scheduleTimer();
        }
    }
}
