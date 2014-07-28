package org.itsnat.droid.impl.browser.clientdoc.event;

import android.os.Parcel;
import android.view.InputEvent;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.ContinueEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.List;

/**
 * Created by jmarranz on 7/07/14.
 */
public class DOMExtEvent extends NormalEvent
{
    public DOMExtEvent(ContinueEventListener listener)
    {
        super(listener);
    }

    public ContinueEventListener getContinueEventListener()
    {
        return (ContinueEventListener)listener;
    }
}
