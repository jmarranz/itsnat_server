package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.InputEvent;
import android.view.KeyEvent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;

import java.util.List;

/**
 * Created by jmarranz on 8/07/14.
 */
public abstract class W3CUIEvent extends W3CEvent
{
    public W3CUIEvent(DOMStdEventListener listener, InputEvent evtNative)
    {
        super(listener,evtNative);
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = super.genParamURL();
        params.add(new BasicNameValuePair("itsnat_evt_detail","" + 1)); // evt.detail;  por poner algo
        return params;
    }
}
