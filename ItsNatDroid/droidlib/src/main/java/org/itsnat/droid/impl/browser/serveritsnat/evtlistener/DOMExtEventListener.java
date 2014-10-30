package org.itsnat.droid.impl.browser.serveritsnat.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.serveritsnat.CustomFunction;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;

/**
 * Created by jmarranz on 4/07/14.
 */
public abstract class DOMExtEventListener extends NormalEventListener
{

    public DOMExtEventListener(ItsNatDocImpl parent,String eventType, View currentTarget, CustomFunction customFunc, String id, int commMode, long timeout)
    {
        super(parent,eventType,currentTarget,customFunc,id,commMode,timeout);
    }

}
