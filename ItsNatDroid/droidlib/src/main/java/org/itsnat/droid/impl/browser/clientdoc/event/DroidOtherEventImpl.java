package org.itsnat.droid.impl.browser.clientdoc.event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.event.DroidEvent;
import org.itsnat.droid.event.DroidFocusEvent;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.List;

/**
 * Created by jmarranz on 7/07/14.
 */
public class DroidOtherEventImpl extends DroidEventImpl implements DroidEvent
{
    protected Object evtNative; // Por poner algo

    public DroidOtherEventImpl(DroidEventListener listener, Object evtNative)
    {
        super(listener);
        this.evtNative = evtNative;
    }

    public static Object createOtherEventNative()
    {
        return new Object();
    }

    public DroidEventListener getDroidEventListener()
    {
        return (DroidEventListener)listener;
    }

    public Object getNativeEvent()
    {
        return evtNative;
    }

    @Override
    public void saveEvent()
    {
    }

    public boolean isIgnoreHold()
    {
        return "unload".equals(getDroidEventListener().getType()); // Si es un unload
    }

}
