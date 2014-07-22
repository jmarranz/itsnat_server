package org.itsnat.droid.impl.browser.clientdoc.event;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventGenericListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventStfulListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class EventStful extends EventGeneric
{
    public EventStful(EventStfulListener listener)
    {
        super(listener);
    }
}
