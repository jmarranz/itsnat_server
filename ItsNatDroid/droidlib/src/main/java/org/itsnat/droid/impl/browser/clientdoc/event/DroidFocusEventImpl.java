package org.itsnat.droid.impl.browser.clientdoc.event;

import android.os.Parcel;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.event.DroidFocusEvent;
import org.itsnat.droid.event.DroidInputEvent;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.List;

/**
 * Created by jmarranz on 7/07/14.
 */
public class DroidFocusEventImpl extends DroidEventImpl implements DroidFocusEvent
{
    protected Boolean evtNative;

    public DroidFocusEventImpl(DroidEventListener listener, Boolean evtNative)
    {
        super(listener);
        this.evtNative = evtNative;
    }

    public static Boolean createFocusEvent(boolean hasFocus)
    {
        return new Boolean(hasFocus);
    }

    public DroidEventListener getDroidEventListener()
    {
        return (DroidEventListener)listener;
    }

    public Boolean getNativeEvent()
    {
        return evtNative;
    }

    public boolean hasFocus()
    {
        return evtNative;
    }

    @Override
    public void saveEvent()
    {

    }

    public List<NameValuePair> genParamURL()
    {
        boolean hasFocus = hasFocus();

        List<NameValuePair> params = super.genParamURL();
        params.add(new BasicNameValuePair("itsnat_evt_hasFocus","" + hasFocus));
        return params;
    }

}
