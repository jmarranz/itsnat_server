package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.KeyEvent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.List;

/**
 * Created by jmarranz on 22/07/14.
 */
public class DroidKeyEventImpl extends DroidInputEventImpl
{
    public DroidKeyEventImpl(DroidEventListener listener, KeyEvent evtNative)
    {
        super(listener, evtNative);
    }

    public KeyEvent getKeyEvent()
    {
        return (KeyEvent)evtNative;
    }

    public List<NameValuePair> genParamURL()
    {
        KeyEvent evtNative = getKeyEvent();

        List<NameValuePair> params = super.genParamURL();
        params.add(new BasicNameValuePair("itsnat_evt_keyCode","" + evtNative.getKeyCode()));
        return params;
    }
}
