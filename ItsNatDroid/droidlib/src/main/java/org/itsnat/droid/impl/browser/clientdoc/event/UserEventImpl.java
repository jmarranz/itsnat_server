package org.itsnat.droid.impl.browser.clientdoc.event;

import org.itsnat.droid.event.UserEvent;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.UserEventListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public class UserEventImpl extends DOMExtEventImpl implements UserEvent
{
    protected String name;

    /*
    Creates the private instance
     */
    public UserEventImpl(UserEventListener listener,UserEventImpl publicEvt)
    {
        super(listener);

        this.extraParams = publicEvt.extraParams;
        this.name = publicEvt.getName();
    }

    /*
    Creates the public instance
     */
    public UserEventImpl(String name)
    {
        super(null);

        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public UserEventListener getUserEventListener()
    {
        return (UserEventListener)listener;
    }
}
