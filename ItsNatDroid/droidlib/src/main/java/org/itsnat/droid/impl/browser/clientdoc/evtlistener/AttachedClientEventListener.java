package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;

/**
 * Created by jmarranz on 8/08/14.
 */
public class AttachedClientEventListener extends EventStfulListener
{
    public AttachedClientEventListener(ItsNatDocImpl parent,int commMode, long timeout)
    {
        super(parent,parent.getAttachType(),commMode,timeout);
    }
}
