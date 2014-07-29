package org.itsnat.droid;

import org.itsnat.droid.event.Event;

/**
 * Created by jmarranz on 8/07/14.
 */
public interface EventMonitor
{
    public void before(Event event);
    public void after(Event event,boolean timeout);
}
