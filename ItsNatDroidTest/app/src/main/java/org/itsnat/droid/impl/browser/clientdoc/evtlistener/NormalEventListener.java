package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.NormalEvent;

/**
 * Created by jmarranz on 6/07/14.
 */
public abstract class NormalEventListener extends EventStfulListener
{
    public NormalEventListener(ItsNatDocImpl parent,String eventType,int commMode, long timeout)
    {
        super(parent,eventType,commMode,timeout);
    }

    public abstract NormalEvent createEventWrapper(Object evt);
}
