package org.itsnat.droid.impl.browser.clientdoc.event;

import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.ItsNatDroidException;
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

    public static KeyEvent createKeyEventNative(String type, int keyCode)
    {
        int action = getActionFromType(type);

        return new KeyEvent(action,keyCode);
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

    public static final String getTypeFromAction(KeyEvent keyEvent)
    {
        String type = null;
        int action = keyEvent.getAction();
        switch (action)
        {
            case KeyEvent.ACTION_DOWN:
                type = "keydown";
                break;
            case KeyEvent.ACTION_UP:
                type = "keyup";
                break;
        }
        return type;
    }

    public static final int getActionFromType(String type)
    {
        if (type.equals("keydown"))
            return KeyEvent.ACTION_DOWN;
        else if (type.equals("keyup"))
            return KeyEvent.ACTION_UP;
        else
            throw new ItsNatDroidException("Unexpected type: " + type);
    }
}
