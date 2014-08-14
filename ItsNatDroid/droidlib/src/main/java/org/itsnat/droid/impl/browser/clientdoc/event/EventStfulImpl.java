package org.itsnat.droid.impl.browser.clientdoc.event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventStfulListener;
import org.itsnat.droid.impl.util.MapLight;

import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class EventStfulImpl extends EventGenericImpl
{
    protected MapLight<String,Object> extraParams;

    public EventStfulImpl(EventStfulListener listener)
    {
        super(listener);
    }

    public MapLight<String,Object> getExtraParams()
    {
        return extraParams; // Puede ser null
    }

    public Object getExtraParam(String name)
    {
        if (extraParams == null) return null;
        return extraParams.get(name);
    }

    public void setExtraParam(String name,Object value)
    {
        if (extraParams == null) this.extraParams = new MapLight<String,Object>();
        extraParams.put(name,value);
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = super.genParamURL();

        if (extraParams != null)
        {
            for (Map.Entry<String,Object> entry : extraParams.getEntryList())
            {
                String name = entry.getKey();
                Object value = entry.getValue();
                if (value != null && value instanceof Object[]) // Aunque sea String[] es válido el instanceof pues Object[] es la "clase base"
                {
                    Object[] valueArr = (Object[])value;
                    for (int i = 0; i < valueArr.length; i++)
                    {
                        if (valueArr[i] == null) continue;
                        params.add(new BasicNameValuePair(name,valueArr[i].toString()));
                    }
                }
                else
                {
                    if (value != null)
                        params.add(new BasicNameValuePair(name,value.toString()));
                }
            }
        }

        return params;
    }
}
