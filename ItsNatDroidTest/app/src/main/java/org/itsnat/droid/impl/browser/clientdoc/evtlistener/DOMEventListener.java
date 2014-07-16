package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.InputEvent;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.CustomFunction;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGeneric;
import org.itsnat.droid.impl.browser.clientdoc.event.NormalEvent;

import java.util.List;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class DOMEventListener extends NormalEventListener
{
    protected View view;
    protected CustomFunction customFunc;
    protected String id;

    public DOMEventListener(ItsNatDocImpl parent,String eventType,View view,CustomFunction customFunc,String id,int commMode, long timeout)
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

    public String getId()
    {
        return id;
    }

    public void dispatchEvent(View view,NormalEvent evtWrapper)
    {
        if (customFunc != null) customFunc.exec(evtWrapper);
        evtWrapper.sendEvent();
    }

    public void genParamURL(EventGeneric evt,List<NameValuePair> params)
    {
        super.genParamURL(evt,params);
        params.add(new BasicNameValuePair("itsnat_listener_id", "" + getId()));
    }
}
