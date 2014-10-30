package org.itsnat.droid.impl.browser.serveritsnat.event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;

import java.util.List;

/**
 * Created by jmarranz on 8/08/14.
 */
public class AttachedClientCometTaskRefreshEventImpl extends AttachedClientEventImpl
{
    protected String listenerId;

    public AttachedClientCometTaskRefreshEventImpl(ItsNatDocImpl parent,String listenerId, int commMode, long timeout)
    {
        super(parent,commMode,timeout);
        this.listenerId = listenerId;
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = super.genParamURL();

        params.add(new BasicNameValuePair("itsnat_listener_id",listenerId));

        return params;
    }

}
