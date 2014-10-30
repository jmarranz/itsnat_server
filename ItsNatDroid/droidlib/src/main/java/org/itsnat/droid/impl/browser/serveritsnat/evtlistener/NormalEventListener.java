package org.itsnat.droid.impl.browser.serveritsnat.evtlistener;

import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.serveritsnat.CustomFunction;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.EventGenericImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.NormalEventImpl;

import java.util.List;

/**
 * Created by jmarranz on 6/07/14.
 */
public abstract class NormalEventListener extends EventStfulListener
{
    protected View currentTarget;
    protected CustomFunction customFunc;
    protected String id;

    public NormalEventListener(ItsNatDocImpl parent,String eventType,View currentTarget,CustomFunction customFunc,String id,int commMode, long timeout)
    {
        super(parent,eventType,commMode,timeout);

        this.currentTarget = currentTarget;
        this.customFunc = customFunc;
        this.id = id;
    }

    public View getCurrentTarget()
    {
        return currentTarget;
    }

    public String getId()
    {
        return id;
    }

    public void dispatchEvent(NormalEventImpl evtWrapper)
    {
        if (customFunc != null) customFunc.exec(evtWrapper);
        evtWrapper.sendEvent();
    }

    public void genParamURL(EventGenericImpl evt,List<NameValuePair> params)
    {
        super.genParamURL(evt,params);
        params.add(new BasicNameValuePair("itsnat_listener_id", "" + getId()));
    }

    public abstract NormalEventImpl createNormalEvent(Object evt);
}
