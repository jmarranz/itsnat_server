package org.itsnat.droid.impl.browser;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.params.HttpParams;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidNetworkException;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.browser.ProcessingAsyncTask;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.util.ValueUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by jmarranz on 4/06/14.
 */
public abstract class HttpPostAsyncTask extends ProcessingAsyncTask<String>
{
    protected ItsNatDocImpl parent;
    protected HttpParams httpParamsRequest;
    protected String url;
    protected List<NameValuePair> params;

    public HttpPostAsyncTask(ItsNatDocImpl parent, HttpParams httpParamsRequest, String url,List<NameValuePair> params)
    {
        this.parent = parent;
        this.httpParamsRequest = httpParamsRequest;
        this.url = url;
        this.params = params;
    }

    protected String executeInBackground()
    {
        ItsNatDroidBrowserImpl browser = parent.getPageImpl().getItsNatDroidBrowserImpl();
        String servletPath = parent.getServletPath();
        StatusLine[] status = new StatusLine[1];
        byte[] result = HttpUtil.httpPost(servletPath, browser.getHttpContext(), httpParamsRequest, browser.getHttpParams(),params,status);
        if (status[0].getStatusCode() != 200)
            throw new ItsNatDroidNetworkException(status[0].getStatusCode(),status[0].getReasonPhrase(),result);

        return ValueUtil.toString(result);
    }

}
