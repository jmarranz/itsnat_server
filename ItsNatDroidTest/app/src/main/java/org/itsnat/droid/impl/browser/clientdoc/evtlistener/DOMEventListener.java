package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class DOMEventListener extends NormalEventListener
{
    protected View view;
    protected String customFunc;
    protected String id;

    public DOMEventListener(ItsNatDocImpl parent,String eventType,View view,String customFunc,String id,int commMode, long timeout)
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

    public void dispatchEvent(View view,Object evt)
    {
        /*
        var evtWrapper = this.createEventWrapper(evt);
        var customFunc = this.getCustomFunc();
        if (customFunc != null) customFunc(evtWrapper);
        evtWrapper.sendEvent();
        */
    }
}
