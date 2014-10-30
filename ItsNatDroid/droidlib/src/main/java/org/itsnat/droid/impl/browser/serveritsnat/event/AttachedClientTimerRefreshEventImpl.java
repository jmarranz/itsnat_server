package org.itsnat.droid.impl.browser.serveritsnat.event;

import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;

/**
 * Created by jmarranz on 8/08/14.
 */
public class AttachedClientTimerRefreshEventImpl extends AttachedClientEventImpl
{
    protected long interval;

    public AttachedClientTimerRefreshEventImpl(ItsNatDocImpl parent,int interval,int commMode, long timeout)
    {
        super(parent,commMode,timeout);
        this.interval = interval;
    }

    public void onEventReturned()
    {
        super.onEventReturned();

        ItsNatDocImpl itsNatDoc = getAttachedClientEventListener().getItsNatDocImpl();
        itsNatDoc.getHandler().postDelayed(itsNatDoc.getAttachTimerRefreshCallback(), interval);
    }
}
