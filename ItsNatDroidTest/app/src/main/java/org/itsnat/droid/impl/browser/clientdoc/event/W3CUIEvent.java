package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.InputEvent;
import android.view.KeyEvent;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;

/**
 * Created by jmarranz on 8/07/14.
 */
public abstract class W3CUIEvent extends W3CEvent
{
    public W3CUIEvent(DOMStdEventListener listener, InputEvent evtNative)
    {
        super(listener,evtNative);
    }

    public StringBuilder genParamURL()
    {
        StringBuilder url = super.genParamURL();
        url.append( "&itsnat_evt_detail=" + 1 ); // evt.detail;  por poner algo
        return url;
    }
}
