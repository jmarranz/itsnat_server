package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.InputEvent;
import android.view.MotionEvent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;

import java.util.List;

/**
 * Created by jmarranz on 8/07/14.
 */
public class W3CMouseEvent extends W3CEvent
{
    public W3CMouseEvent(DOMStdEventListener listener, MotionEvent evtNative)
    {
        super(listener,evtNative);
    }

    public MotionEvent getMotionEvent()
    {
        return (MotionEvent)evtNative;
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = super.genParamURL();

        MotionEvent evt = getMotionEvent();

        params.add(new BasicNameValuePair("itsnat_evt_screenX","" + evt.getRawX())); // evt.screenX;
        params.add(new BasicNameValuePair("itsnat_evt_screenY","" + evt.getRawY())); // evt.screenY;
        params.add(new BasicNameValuePair("itsnat_evt_clientX","" + evt.getX())); // evt.clientX;
        params.add(new BasicNameValuePair("itsnat_evt_clientY","" + evt.getY())); // evt.clientY;
        params.add(new BasicNameValuePair("itsnat_evt_ctrlKey","" + false)); // evt.ctrlKey;
        params.add(new BasicNameValuePair("itsnat_evt_metaKey","" + false)); // evt.metaKey;
        params.add(new BasicNameValuePair("itsnat_evt_shiftKey","" + false)); // evt.shiftKey;
        params.add(new BasicNameValuePair("itsnat_evt_altKey","" + false)); // evt.altKey;
        params.add(new BasicNameValuePair("itsnat_evt_button","" + 0)); // evt.button;

        params.add(new BasicNameValuePair("itsnat_evt_relatedTarget","")); // listener.getItsNatDocImpl().getStringPathFromNode(evt.relatedTarget);
        return params;
    }

}
