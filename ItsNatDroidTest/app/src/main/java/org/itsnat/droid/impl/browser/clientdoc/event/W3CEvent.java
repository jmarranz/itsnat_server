package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.InputEvent;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;

/**
 * Created by jmarranz on 8/07/14.
 */
public abstract class W3CEvent extends DOMStdEvent
{
    public W3CEvent(DOMStdEventListener listener,InputEvent evtNative)
    {
        super(listener,evtNative);
    }

    /*
    public String genParamURL()
    {
        String url = "";
        url += "&itsnat_evt_target=" + listener.getItsNatDocImpl().getStringPathFromNode(evt.target);
        url += "&itsnat_evt_eventPhase=" + 2 ;  // evt.eventPhase;   Event.AT_TARGET = 2
        url += "&itsnat_evt_bubbles=" + false; //evt.bubbles;
        url += "&itsnat_evt_cancelable=" + true; // evt.cancelable;
        return url;
    }
    */

}
