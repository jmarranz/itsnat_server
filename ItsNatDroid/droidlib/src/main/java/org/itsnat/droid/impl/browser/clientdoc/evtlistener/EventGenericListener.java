package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGeneric;
import org.itsnat.droid.impl.util.IOUtil;

import java.util.List;
import java.util.Map;

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

    public void genParamURL(EventGeneric evt,List<NameValuePair> params)
    {
        params.add(new BasicNameValuePair("itsnat_action",action));

        Map<String,Object> extraParams = evt.getExtraParams();
        if (extraParams != null)
        {
            for (Map.Entry<String,Object> entry : extraParams.entrySet())
            {
                String name = entry.getKey();
                Object value = entry.getValue();
                if (value != null && value instanceof Object[]) // Aunque sea String[] es válido el instanceof pues Object[] es la "clase base"
                {
                    Object[] valueArr = (Object[])value;
                    for (int i = 0; i < valueArr.length; i++)
                        params.add(new BasicNameValuePair(name,(String)valueArr[i]));
                }
                else params.add(new BasicNameValuePair(name,(String)value) );
            }
        }
    }
}
