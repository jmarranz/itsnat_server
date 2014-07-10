package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGeneric;
import org.itsnat.droid.impl.util.IOUtil;

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

    public StringBuilder genParamURL(EventGeneric evt)
    {
        StringBuilder url = new StringBuilder();
        url.append( "&itsnat_action=" + this.action );

        Map<String,Object> params = evt.getExtraParams();
        if (params != null)
        {
            for (Map.Entry<String,Object> entry : params.entrySet())
            {
                String name = entry.getKey();
                Object value = entry.getValue();
                if (value != null && value instanceof Object[]) // Aunque sea String[] es válido el instanceof pues Object[] es la "clase base"
                {
                    Object[] valueArr = (Object[])value;
                    for (int i = 0; i < valueArr.length; i++)
                        url.append( "&" + name + "=" + IOUtil.encodeURIComponent((String)valueArr[i]) );
                }
                else url.append( "&" + name + "=" + IOUtil.encodeURIComponent((String)value) );
            }
        }
        return url;
    }
}
