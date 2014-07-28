package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.CustomFunction;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DOMExtEvent;
import org.itsnat.droid.impl.browser.clientdoc.event.NormalEvent;

/**
 * Created by jmarranz on 4/07/14.
 */
public class ContinueEventListener extends NormalEventListener
{
    public ContinueEventListener(ItsNatDocImpl parent, View view, CustomFunction customFunc, String id, int commMode, long timeout)
    {
        super(parent,"continue",view,customFunc,id,commMode,timeout);
    }

    @Override
    public NormalEvent createEventWrapper(Object evt)
    {
        return new DOMExtEvent(this);
    }
}
