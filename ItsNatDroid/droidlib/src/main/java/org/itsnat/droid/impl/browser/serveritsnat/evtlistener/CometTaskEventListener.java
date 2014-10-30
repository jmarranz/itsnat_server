package org.itsnat.droid.impl.browser.serveritsnat.evtlistener;

import org.itsnat.droid.impl.browser.serveritsnat.CustomFunction;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DOMExtEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.NormalEventImpl;

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
    public NormalEventImpl createNormalEvent(Object evt)
    {
        return new DOMExtEventImpl(this);
    }
}
