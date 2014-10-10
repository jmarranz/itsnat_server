package org.itsnat.droid;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by jmarranz on 10/10/14.
 */
public interface GenericHttpClient
{
    public int getErrorMode();
    public void setErrorMode(int errorMode);

    public GenericHttpClient setOnHttpRequestErrorListener(OnHttpRequestErrorListener listener);
    public GenericHttpClient setRequestHeader(String header, Object value);

    public void requestSync(String servletPath, List<NameValuePair> params);
    public void requestAsync(String servletPath, List<NameValuePair> params,OnHttpRequestListener listener);
}
