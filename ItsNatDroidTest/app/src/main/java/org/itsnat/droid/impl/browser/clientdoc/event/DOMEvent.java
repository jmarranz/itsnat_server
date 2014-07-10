package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventStfulListener;

import java.util.List;

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

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = super.genParamURL();
        params.add(new BasicNameValuePair("itsnat_evt_timeStamp","" + timeStamp)); // En vez del problematico Event.timeStamp
        return params;
    }
}
