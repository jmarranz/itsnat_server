package org.itsnat.droid;

/**
 * Created by jmarranz on 10/10/14.
 */
public interface GenericHttpClient
{
    //public int getErrorMode();
    public GenericHttpClient setErrorMode(int errorMode);
    public GenericHttpClient setOnHttpRequestListener(OnHttpRequestListener listener);
    public GenericHttpClient setOnHttpRequestErrorListener(OnHttpRequestErrorListener listener);
    public GenericHttpClient setURL(String url);
    public GenericHttpClient addParam(String name,String value);
    public GenericHttpClient clearParams();
    public GenericHttpClient setRequestHeader(String header, Object value);
    public GenericHttpClient setOverrideMimeType(String mime);

    public HttpRequestResult requestSync();
    public void requestAsync();
}
