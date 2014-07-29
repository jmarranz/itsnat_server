package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.MotionEvent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.List;

/**
 * Created by jmarranz on 22/07/14.
 */
public class DroidMotionEventImpl extends DroidInputEventImpl
{
    public DroidMotionEventImpl(DroidEventListener listener, MotionEvent evtNative)
    {
        super(listener, evtNative);
    }

    public MotionEvent getMotionEvent()
    {
        return (MotionEvent)evtNative;
    }

    public List<NameValuePair> genParamURL()
    {
        MotionEvent evtNative = getMotionEvent();

        List<NameValuePair> params = super.genParamURL();
        params.add(new BasicNameValuePair("itsnat_evt_rawX","" + evtNative.getRawX()));
        params.add(new BasicNameValuePair("itsnat_evt_rawY","" + evtNative.getRawY()));
        params.add(new BasicNameValuePair("itsnat_evt_x","" + evtNative.getX()));
        params.add(new BasicNameValuePair("itsnat_evt_y","" + evtNative.getY()));
        return params;
    }
}
