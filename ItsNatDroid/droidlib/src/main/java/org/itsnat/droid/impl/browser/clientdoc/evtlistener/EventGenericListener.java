package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGenericImpl;

import java.util.List;

/**
 * Created by jmarranz on 6/07/14.
 */
public abstract class EventGenericListener
{
    protected ItsNatDocImpl parent;
    protected String action;
    protected int commMode;
    protected long timeout;

    public EventGenericListener(ItsNatDocImpl parent, String action, int commMode, long timeout)
    {
        this.parent = parent;
        this.action = action;
        this.commMode = commMode;
        this.timeout = timeout;
    }

    public ItsNatDocImpl getItsNatDocImpl()
    {
        return parent;
    }

    public String getAction()
    {
        return action;
    }

    public int getCommMode()
    {
        return commMode;
    }

    public long getTimeout()
    {
        return timeout;
    }

    public void genParamURL(EventGenericImpl evt,List<NameValuePair> params)
    {
        params.add(new BasicNameValuePair("itsnat_action",action));
    }
}
