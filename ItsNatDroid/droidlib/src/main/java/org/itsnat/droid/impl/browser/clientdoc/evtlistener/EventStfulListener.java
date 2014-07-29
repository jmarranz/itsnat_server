package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGenericImpl;

import java.util.List;

/**
 * Created by jmarranz on 6/07/14.
 */
public abstract class EventStfulListener extends EventGenericListener
{
    protected String eventType;

    public EventStfulListener(ItsNatDocImpl parent,String eventType, int commMode, long timeout)
    {
        super(parent,"event",commMode, timeout);

        this.eventType = eventType;
    }

    public String getEventType()
    {
        return eventType;
    }

    public void genParamURL(EventGenericImpl evt,List<NameValuePair> params)
    {
        super.genParamURL(evt,params);
        params.add(new BasicNameValuePair("itsnat_eventType", "" + getEventType()));
    }
}
