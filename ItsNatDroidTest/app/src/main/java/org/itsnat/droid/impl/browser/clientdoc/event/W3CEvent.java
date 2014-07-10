package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.InputEvent;
import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.NodeImpl;
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

    public StringBuilder genParamURL()
    {
        View view = getView();

        StringBuilder url = new StringBuilder();
        url.append( "&itsnat_evt_target=" + listener.getItsNatDocImpl().getStringPathFromNode(NodeImpl.create(view)) );  // evt.target
        url.append( "&itsnat_evt_eventPhase=" + 2 );  // evt.eventPhase;   Event.AT_TARGET = 2
        url.append( "&itsnat_evt_bubbles=" + false ); //evt.bubbles;
        url.append( "&itsnat_evt_cancelable=" + true ); // evt.cancelable;
        return url;
    }
}
