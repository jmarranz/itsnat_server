package org.itsnat.droid.impl.browser.clientdoc.event;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventStfulListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public class DOMStdEvent extends DOMEvent
{
    public DOMStdEvent(DOMStdEventListener listener)
    {
        super(listener);
    }
}
