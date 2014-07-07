package org.itsnat.droid.impl.browser.clientdoc.event;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventGenericListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class EventGeneric
{
    protected EventGenericListener listener;

    public EventGeneric(EventGenericListener listener)
    {
        this.listener = listener;
    }
}
