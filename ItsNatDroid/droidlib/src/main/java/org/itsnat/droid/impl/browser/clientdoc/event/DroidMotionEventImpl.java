package org.itsnat.droid.impl.browser.clientdoc.event;

import android.os.SystemClock;
import android.view.MotionEvent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.ItsNatDroidException;
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
        params.add(new BasicNameValuePair("itsnat_evt_x","" + evtNative.getX()));
        params.add(new BasicNameValuePair("itsnat_evt_y","" + evtNative.getY()));
        params.add(new BasicNameValuePair("itsnat_evt_rawX","" + evtNative.getRawX()));
        params.add(new BasicNameValuePair("itsnat_evt_rawY","" + evtNative.getRawY()));
        return params;
    }

    public static MotionEvent createMotionEventNative(String type, float x, float y)
    {
        int action = getActionFromType(type);

        // Viendo el código fuente del método obtain con menos params sacamos los valores por defecto excepto algún parámetro
        return MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), action, x, y, 1.0f, 1.0f, 0, 1.0f, 1.0f, 0, 0);
    }


    public static final String getTouchTypeFromAction(MotionEvent motionEvent)
    {
        String type = null;
        int action = motionEvent.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                type = "touchstart";
                break;
            case MotionEvent.ACTION_UP:
                type = "touchend";
                break;
            case MotionEvent.ACTION_MOVE:
                type = "touchmove";
                break;
            case MotionEvent.ACTION_CANCEL:
                type = "touchcancel";
                break;
        }
        return type;
    }

    public static final int getActionFromTouchType(String type)
    {
        if (type.equals("touchstart"))
            return MotionEvent.ACTION_DOWN;
        else if (type.equals("touchend"))
            return MotionEvent.ACTION_UP;
        else if (type.equals("touchmove"))
            return MotionEvent.ACTION_MOVE;
        else if (type.equals("touchcancel"))
            return MotionEvent.ACTION_CANCEL;
        else
            throw new ItsNatDroidException("Unexpected type: " + type);
    }

    public static final int getActionFromType(String type)
    {
        if (type.equals("click"))
            return MotionEvent.ACTION_UP;
        else return getActionFromTouchType(type);
    }

}
