package org.itsnat.droid.impl.browser.clientdoc;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.util.ValueUtil;

import java.util.List;

/**
 * Created by jmarranz on 10/07/14.
 */
public class EventSender
{
    protected EventManager evtManager;

    public EventSender(EventManager evtManager)
    {
        this.evtManager = evtManager;
    }

    public String requestSyncText(String servletPath,List<NameValuePair> params,long timeout)
    {
        ItsNatDroidBrowserImpl browser = evtManager.getItsNatDocImpl().getPageImpl().getItsNatDroidBrowserImpl();

        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = evtManager.getItsNatDocImpl().getPageImpl().getHttpParams();
        httpParamsRequest = httpParamsRequest.copy();
        int soTimeout = timeout < 0 ? Integer.MAX_VALUE : (int)timeout;
        HttpConnectionParams.setSoTimeout(httpParamsRequest,soTimeout);
        HttpParams httpParamsDefault = browser.getHttpParams();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        StatusLine[] status = new StatusLine[1];
        byte[] result = HttpUtil.httpPost(servletPath, httpContext, httpParamsRequest, httpParamsDefault,sslSelfSignedAllowed,params,status);
        if (status[0].getStatusCode() != 200)
            throw new ItsNatDroidServerResponseException(status[0].getStatusCode(),status[0].getReasonPhrase(),result);

        return ValueUtil.toString(result);
    }
}
