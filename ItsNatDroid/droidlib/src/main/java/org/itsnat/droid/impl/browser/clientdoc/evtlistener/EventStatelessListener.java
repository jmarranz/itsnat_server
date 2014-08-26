package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGenericImpl;

import java.util.List;

/**
 * Created by jmarranz on 6/07/14.
 */
public class EventStatelessListener extends EventGenericListener
{
    public EventStatelessListener(ItsNatDocImpl parent, int commMode, long timeout)
    {
        super(parent,"event_stateless",commMode, timeout);
    }

    public void genParamURL(EventGenericImpl evt,List<NameValuePair> params)
    {
        super.genParamURL(evt,params);
        // Recuerda que al ser stateless tenemos que enviar el commMode y el eventTimeout, de hecho los proporciona el usuario desde el cliente
        params.add(new BasicNameValuePair("itsnat_commMode", "" + commMode));
        params.add(new BasicNameValuePair("itsnat_eventTimeout", "" + timeout));
    }
}
