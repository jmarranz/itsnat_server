package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.InputEvent;
import android.view.MotionEvent;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;

/**
 * Created by jmarranz on 8/07/14.
 */
public class W3CMouseEvent extends W3CEvent
{
    public W3CMouseEvent(DOMStdEventListener listener, MotionEvent evtNative)
    {
        super(listener,evtNative);
    }

    /*
    public String genParamURL()
    {
        String url = super.genParamURL();

        url += "&itsnat_evt_screenX=" + evt.screenX;
        url += "&itsnat_evt_screenY=" + evt.screenY;
        url += "&itsnat_evt_clientX=" + evt.clientX;
        url += "&itsnat_evt_clientY=" + evt.clientY;
        url += "&itsnat_evt_ctrlKey=" + evt.ctrlKey;
        url += "&itsnat_evt_metaKey=" + evt.metaKey;
        url += "&itsnat_evt_shiftKey=" + evt.shiftKey;
        url += "&itsnat_evt_altKey=" + evt.altKey;
        url += "&itsnat_evt_button=" + evt.button;

        url += "&itsnat_evt_relatedTarget=" + itsNatDoc.getStringPathFromNode(evt.relatedTarget);
        return url;
    }

    */

}
