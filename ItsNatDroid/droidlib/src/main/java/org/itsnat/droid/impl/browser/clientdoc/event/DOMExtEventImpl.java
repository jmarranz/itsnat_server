package org.itsnat.droid.impl.browser.clientdoc.event;

import org.itsnat.droid.event.DOMExtEvent;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMExtEventListener;

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
