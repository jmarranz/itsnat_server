package org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter;

import org.itsnat.droid.impl.browser.serveritsnat.DroidEventDispatcher;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatViewImpl;

/**
 * Created by jmarranz on 4/07/14.
 */
public abstract class DroidEventListenerViewAdapter
{
    protected ItsNatViewImpl viewData;

    public DroidEventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        this.viewData = viewData;
    }

    protected void dispatch(String type,Object nativeEvt)
    {
        DroidEventDispatcher evtDisp = viewData.getItsNatDocImpl().getDroidEventDispatcher();
        evtDisp.dispatch(viewData, type, nativeEvt);
    }
}
