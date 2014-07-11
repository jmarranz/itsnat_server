package org.itsnat.droid.impl.browser;

import android.os.AsyncTask;

import org.apache.http.params.HttpParams;

/**
 * Created by jmarranz on 11/07/14.
 */
public abstract class ProcessingAsyncTask<ResOk> extends AsyncTask<Void, Void, Object>
{
    public ProcessingAsyncTask()
    {
    }

    @Override
    protected Object doInBackground(Void... params)
    {
        try
        {
            ResOk result = executeInBackground();
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
            onFinishOk((ResOk)result);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    protected abstract ResOk executeInBackground();
    protected abstract void onFinishOk(ResOk result);
    protected abstract void onFinishError(Exception ex);
}

