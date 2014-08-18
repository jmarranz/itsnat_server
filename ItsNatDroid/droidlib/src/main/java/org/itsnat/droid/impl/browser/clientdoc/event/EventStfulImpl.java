package org.itsnat.droid.impl.browser.clientdoc.event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventStfulListener;
import org.itsnat.droid.impl.util.MapLight;

import java.util.List;
import java.util.Map;

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
