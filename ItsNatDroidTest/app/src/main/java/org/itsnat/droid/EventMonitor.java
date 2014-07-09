package org.itsnat.droid;

/**
 * Created by jmarranz on 8/07/14.
 */
public interface EventMonitor
{
    public void before(Event event);
    public void after(Event event,boolean timeout);
}
