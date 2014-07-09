package org.itsnat.droid.impl.browser.clientdoc.event;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventStfulListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class DOMEvent extends NormalEvent
{
    protected long timeStamp;

    public DOMEvent(DOMEventListener listener)
    {
        super(listener);
        this.timeStamp = System.currentTimeMillis();
    }

    public String genParamURL()
    {
        String url = super.genParamURL();
        url += "&itsnat_evt_timeStamp=" + timeStamp; // En vez del problematico Event.timeStamp
        return url;
    }
}
