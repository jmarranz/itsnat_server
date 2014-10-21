package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import org.itsnat.droid.impl.browser.clientdoc.DroidEventDispatcher;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;

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
