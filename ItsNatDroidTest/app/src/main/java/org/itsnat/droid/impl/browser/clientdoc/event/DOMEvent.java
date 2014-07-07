package org.itsnat.droid.impl.browser.clientdoc.event;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventStfulListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class DOMEvent extends NormalEvent
{
    public DOMEvent(DOMEventListener listener)
    {
        super(listener);
    }
}
