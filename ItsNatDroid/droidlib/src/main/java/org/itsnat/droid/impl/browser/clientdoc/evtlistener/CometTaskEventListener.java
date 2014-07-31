package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.CustomFunction;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DOMExtEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.NormalEventImpl;

/**
 * Created by jmarranz on 4/07/14.
 */
public class CometTaskEventListener extends DOMExtEventListener
{
    public CometTaskEventListener(ItsNatDocImpl parent, String id,CustomFunction customFunc, int commMode, long timeout)
    {
        super(parent,"cometret",null,customFunc,id,commMode,timeout);
    }

    @Override
    public NormalEventImpl createEventWrapper(Object evt)
    {
        return new DOMExtEventImpl(this);
    }
}
