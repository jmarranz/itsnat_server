package org.itsnat.droid.impl.browser.clientdoc.event;

import org.itsnat.droid.event.EventStateless;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventStatelessListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public class EventStatelessImpl extends EventGenericImpl implements EventStateless
{
    /*
    Creates the private instance
     */
    public EventStatelessImpl(ItsNatDocImpl parent,EventStatelessImpl publicEvt,int commMode, long timeout)
    {
        super(new EventStatelessListener(parent,commMode,timeout));

        this.extraParams = publicEvt.extraParams;
    }

    /*
    Creates the public instance
     */
    public EventStatelessImpl()
    {
        super(null);
    }

    public EventStatelessListener getEventStatelessListener()
    {
        return (EventStatelessListener)listener;
    }


    @Override
    public void saveEvent()
    {
    }
}
