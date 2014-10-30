package org.itsnat.droid.impl.browser.serveritsnat.event;

import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.event.NormalEvent;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.NormalEventListener;

import java.util.List;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class NormalEventImpl extends EventStfulImpl implements NormalEvent
{
    protected long timeStamp;


    public NormalEventImpl(NormalEventListener listener)
    {
        super(listener);
        this.timeStamp = System.currentTimeMillis();
    }

    public NormalEventListener getNormalEventListener()
    {
        return (NormalEventListener)listener;
    }

    public View getCurrentTarget()
    {
        return getNormalEventListener().getCurrentTarget();
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = super.genParamURL();
        params.add(new BasicNameValuePair("itsnat_evt_timeStamp","" + timeStamp)); // En vez del problematico Event.timeStamp

        return params;
    }
}

