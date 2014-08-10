package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.event.NormalEvent;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.AttachedClientEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.NormalEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class AttachedClientEventImpl extends EventStfulImpl
{
    public AttachedClientEventImpl(ItsNatDocImpl parent,int commMode, long timeout)
    {
        super(new AttachedClientEventListener(parent,commMode,timeout));
    }

    public AttachedClientEventListener getAttachedClientEventListener()
    {
        return (AttachedClientEventListener)listener;
    }

    @Override
    public void saveEvent()
    {
    }

}

