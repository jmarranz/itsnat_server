package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.NormalEvent;

/**
 * Created by jmarranz on 6/07/14.
 */
public class NormalEventListener extends EventStfulListener
{
    protected View view;
    protected String customFunc;
    protected String id;

    public NormalEventListener(ItsNatDocImpl parent,String eventType,View view,String customFunc,String id,int commMode, long timeout)
    {
        super(parent,eventType,commMode,timeout);

        this.view = view;
        this.customFunc = customFunc;
        this.id = id;
    }

    public View getView()
    {
        return view;
    }


    public void dispatchEvent()
    {
        /*
        var evtWrapper = this.createEventWrapper(evt);
        var customFunc = this.getCustomFunc();
        if (customFunc != null) customFunc(evtWrapper);
        evtWrapper.sendEvent();
        */
    }

}
