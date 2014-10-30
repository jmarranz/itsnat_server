package org.itsnat.droid.impl.browser.serveritsnat.event;

import org.itsnat.droid.event.DOMExtEvent;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.DOMExtEventListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public class DOMExtEventImpl extends NormalEventImpl implements DOMExtEvent
{
    public DOMExtEventImpl(DOMExtEventListener listener)
    {
        super(listener);
    }

    public DOMExtEventListener getDOMExtEventListener()
    {
        return (DOMExtEventListener)listener;
    }

    @Override
    public void saveEvent()
    {
    }

}
