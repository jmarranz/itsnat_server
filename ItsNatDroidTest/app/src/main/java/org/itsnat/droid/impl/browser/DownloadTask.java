package org.itsnat.droid.impl.browser;

import android.os.AsyncTask;

import org.apache.http.params.HttpParams;

/**
 * Created by jmarranz on 4/06/14.
 */
public abstract class DownloadTask extends AsyncTask<Void, Void, Object>
{
    protected ItsNatDroidBrowserImpl browser;
    protected HttpParams httpParamsRequest;
    protected String url;

    public DownloadTask(ItsNatDroidBrowserImpl browser,HttpParams httpParamsRequest,String url)
    {
        this.browser = browser;
        this.httpParamsRequest = httpParamsRequest;
        this.url = url;
    }

    @Override
    protected Object doInBackground(Void... params)
    {
        try
        {
            byte[] result = DownloaderUtil.connect(url,browser.getHttpContext(), httpParamsRequest,browser.getHttpParams());
            return result;
        }
        catch(Exception ex)
        {
            return ex;
        }
    }

    @Override
    protected void onPostExecute(Object result)
    {
        if (result instanceof Exception)
            onFinishError((Exception)result);
        else
            onFinishOk((byte[])result);
    }

    protected abstract void onFinishOk(byte[] result);
    protected abstract void onFinishError(Exception ex);

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
