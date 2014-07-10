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

    public MotionEvent getMotionEvent()
    {
        return (MotionEvent)evtNative;
    }

    public StringBuilder genParamURL()
    {
        StringBuilder url = super.genParamURL();

        MotionEvent evt = getMotionEvent();

        url.append( "&itsnat_evt_screenX=" + evt.getRawX() ); // evt.screenX;
        url.append( "&itsnat_evt_screenY=" + evt.getRawY() ); // evt.screenY;
        url.append( "&itsnat_evt_clientX=" + evt.getX() ); // evt.clientX;
        url.append( "&itsnat_evt_clientY=" + evt.getY() ); // evt.clientY;
        url.append( "&itsnat_evt_ctrlKey=" + false ); // evt.ctrlKey;
        url.append( "&itsnat_evt_metaKey=" + false ); // evt.metaKey;
        url.append( "&itsnat_evt_shiftKey=" + false ); // evt.shiftKey;
        url.append( "&itsnat_evt_altKey=" + false ); // evt.altKey;
        url.append( "&itsnat_evt_button=" + 0 ); // evt.button;

        url.append( "&itsnat_evt_relatedTarget=" + null ); // listener.getItsNatDocImpl().getStringPathFromNode(evt.relatedTarget);
        return url;
    }

}
