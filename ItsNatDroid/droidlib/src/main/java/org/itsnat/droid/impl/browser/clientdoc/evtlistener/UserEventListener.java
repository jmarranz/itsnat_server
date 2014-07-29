package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.CustomFunction;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.UserEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.NormalEventImpl;

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
    public NormalEventImpl createEventWrapper(Object evt)
    {
        return new UserEventImpl(this,(UserEventImpl)evt);
    }
}
