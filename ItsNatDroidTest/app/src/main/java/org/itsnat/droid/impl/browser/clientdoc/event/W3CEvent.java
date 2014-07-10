package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.InputEvent;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.NodeImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;

import java.util.List;

/**
 * Created by jmarranz on 8/07/14.
 */
public abstract class W3CEvent extends DOMStdEvent
{
    public W3CEvent(DOMStdEventListener listener,InputEvent evtNative)
    {
        super(listener,evtNative);
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = super.genParamURL();

        View view = getView();
        params.add(new BasicNameValuePair("itsnat_evt_target",listener.getItsNatDocImpl().getStringPathFromNode(NodeImpl.create(view))));  // evt.target
        params.add(new BasicNameValuePair("itsnat_evt_eventPhase","" + 2));  // evt.eventPhase;   Event.AT_TARGET = 2
        params.add(new BasicNameValuePair("itsnat_evt_bubbles","" + false)); //evt.bubbles;
        params.add(new BasicNameValuePair("itsnat_evt_cancelable","" + true)); // evt.cancelable;
        return params;
    }
}
