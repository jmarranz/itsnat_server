package org.itsnat.droid.impl.browser.serveritsnat.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.serveritsnat.CustomFunction;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.UserEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.NormalEventImpl;

/**
 * Created by jmarranz on 4/07/14.
 */
public class UserEventListener extends DOMExtEventListener
{
    protected String name;

    public UserEventListener(ItsNatDocImpl parent, View currentTarget,String name, CustomFunction customFunc, String id, int commMode, long timeout)
    {
        super(parent,"user",currentTarget,customFunc,id,commMode,timeout);
    }

    public String getName() { return name; }

    @Override
    public NormalEventImpl createNormalEvent(Object evt)
    {
        return new UserEventImpl(this,(UserEventImpl)evt);
    }
}
