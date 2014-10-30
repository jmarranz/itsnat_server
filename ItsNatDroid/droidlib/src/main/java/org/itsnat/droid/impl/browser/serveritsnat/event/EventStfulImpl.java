package org.itsnat.droid.impl.browser.serveritsnat.event;

import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.EventStfulListener;
import org.itsnat.droid.impl.util.MapLight;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class EventStfulImpl extends EventGenericImpl
{
    public EventStfulImpl(EventStfulListener listener)
    {
        super(listener);
    }

    public MapLight<String,Object> getExtraParams()
    {
        return extraParams; // Puede ser null
    }


}
