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

import java.util.Date;
import org.itsnat.core.event.ItsNatTimerEvent;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class RemoteControlTimerEventListener implements EventListener
{
    protected RemoteControlTimerMgrGlobalEventListener parent;

    public RemoteControlTimerEventListener(RemoteControlTimerMgrGlobalEventListener parent)
    {
        this.parent = parent;
    }

    public void handleEvent(Event evt)
    {
         ItsNatTimerEvent timerEvt = (ItsNatTimerEvent)evt;
         ItsNatTimerHandle handle = timerEvt.getItsNatTimerHandle();
         long lastNonTimerEvent = parent.getLastNonTimerEvent();
         if ((new Date().getTime() - lastNonTimerEvent) > 2*60*1000) // to avoid never ending pages
             handle.cancel();
    }
}
