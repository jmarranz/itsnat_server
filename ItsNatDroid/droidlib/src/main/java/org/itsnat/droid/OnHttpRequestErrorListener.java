package org.itsnat.droid;

/**
 * Created by jmarranz on 11/07/14.
 */
public interface OnHttpRequestErrorListener
{
    public void onError(Exception ex,HttpRequestResult response);
}
