package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.CustomFunction;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DOMExtEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.NormalEventImpl;

/**
 * Created by jmarranz on 4/07/14.
 */
public class ContinueEventListener extends DOMExtEventListener
{
    public ContinueEventListener(ItsNatDocImpl parent, View currentTarget, CustomFunction customFunc, String id, int commMode, long timeout)
    {
        super(parent,"continue",currentTarget,customFunc,id,commMode,timeout);
    }

    @Override
    public NormalEventImpl createNormalEvent(Object evt)
    {
        return new DOMExtEventImpl(this);
    }
}
