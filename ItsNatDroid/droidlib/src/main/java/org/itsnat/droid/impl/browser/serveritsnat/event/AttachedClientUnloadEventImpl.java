package org.itsnat.droid.impl.browser.serveritsnat.event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;

import java.util.List;

/**
 * Created by jmarranz on 8/08/14.
 */
public class AttachedClientUnloadEventImpl extends AttachedClientEventImpl
{
    public AttachedClientUnloadEventImpl(ItsNatDocImpl parent,int commMode, long timeout)
    {
        super(parent,commMode,timeout);
    }

    public boolean isIgnoreHold()
    {
        return true; // Es un unload
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = super.genParamURL();

        params.add(new BasicNameValuePair("itsnat_unload","true"));

        return params;
    }
}
