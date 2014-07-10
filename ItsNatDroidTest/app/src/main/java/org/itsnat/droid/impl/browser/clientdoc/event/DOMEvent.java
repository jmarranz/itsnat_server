package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.View;

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

    public DOMEventListener getDOMEventListener()
    {
        return (DOMEventListener)listener;
    }

    public View getView()
    {
        return getDOMEventListener().getView();
    }

    public StringBuilder genParamURL()
    {
        StringBuilder url = super.genParamURL();
        url.append( "&itsnat_evt_timeStamp=" + timeStamp ); // En vez del problematico Event.timeStamp
        return url;
    }
}
