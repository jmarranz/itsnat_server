package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.TransportUtil;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.NormalEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 7/07/14.
 */
public class NormalEvent extends EventStful
{
    protected long timeStamp;
    protected Map<String,Object> extraParams;

    public NormalEvent(NormalEventListener listener)
    {
        super(listener);
        this.timeStamp = System.currentTimeMillis();
    }

    public NormalEventListener getNormalEventListener()
    {
        return (NormalEventListener)listener;
    }

    public View getView()
    {
        return getNormalEventListener().getView();
    }


    public Map<String,Object> getExtraParams()
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
        if (extraParams == null) this.extraParams = new HashMap<String,Object>();
        extraParams.put(name,value);
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = super.genParamURL();
        params.add(new BasicNameValuePair("itsnat_evt_timeStamp","" + timeStamp)); // En vez del problematico Event.timeStamp

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

        return params;
    }
}

