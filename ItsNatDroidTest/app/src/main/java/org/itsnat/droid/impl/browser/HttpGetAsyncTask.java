package org.itsnat.droid.impl.browser;

import android.os.AsyncTask;

import org.apache.http.StatusLine;
import org.apache.http.params.HttpParams;
import org.itsnat.droid.ItsNatDroidNetworkException;

/**
 * Created by jmarranz on 4/06/14.
 */
public abstract class HttpGetAsyncTask extends ProcessingAsyncTask<byte[]>
{
    protected ItsNatDroidBrowserImpl browser;
    protected HttpParams httpParamsRequest;
    protected String url;

    public HttpGetAsyncTask(ItsNatDroidBrowserImpl browser, HttpParams httpParamsRequest, String url)
    {
        this.browser = browser;
        this.httpParamsRequest = httpParamsRequest;
        this.url = url;
    }

    protected byte[] executeInBackground()
    {
        StatusLine[] status = new StatusLine[1];
        byte[] result = HttpUtil.httpGet(url, browser.getHttpContext(), httpParamsRequest, browser.getHttpParams(),status);
        if (status[0].getStatusCode() != 200)
            throw new ItsNatDroidNetworkException(status[0].getStatusCode(),status[0].getReasonPhrase(),result);

        return result;
    }

}
