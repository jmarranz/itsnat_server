package org.itsnat.droid.impl.browser.clientdoc.event;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.NormalEventListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public class NormalEvent extends EventStful
{
    public NormalEvent(NormalEventListener listener)
    {
        super(listener);
    }
}

