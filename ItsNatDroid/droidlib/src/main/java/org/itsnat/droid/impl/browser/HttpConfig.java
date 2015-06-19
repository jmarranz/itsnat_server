package org.itsnat.droid.impl.browser;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.util.Map;

/**
 * Created by jmarranz on 12/11/14.
 */
public class HttpConfig
{
    public HttpFileCache httpFileCache;
    public HttpContext httpContext;
    public HttpParams httpParamsRequest;
    public HttpParams httpParamsDefault;
    public Map<String,String> httpHeaders;
    public boolean sslSelfSignedAllowed;

    public HttpConfig(PageImpl page)
    {
        this(page.getPageRequestImpl());
    }

    public HttpConfig(PageRequestImpl pageRequest)
    {
        ItsNatDroidBrowserImpl browser = pageRequest.getItsNatDroidBrowserImpl();

        HttpFileCache httpFileCache = browser.getHttpFileCache();
        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsRequest = pageRequest.getHttpParams();
        HttpParams httpParamsDefault = browser.getHttpParams();
        Map<String, String> httpHeaders = pageRequest.createHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        this.httpFileCache = httpFileCache;
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest != null ? httpParamsRequest.copy() : null;
        this.httpParamsDefault = httpParamsDefault != null ? httpParamsDefault.copy() : null;
        this.httpHeaders = httpHeaders; // No hace falta clone porque createHttpHeaders() crea un Map
        this.sslSelfSignedAllowed = sslSelfSignedAllowed;
    }

    public void setTimeout(long timeout)
    {
        if (httpParamsRequest == null) httpParamsRequest = new BasicHttpParams();
        setTimeout(timeout,httpParamsRequest);
    }

    public static void setTimeout(long timeout,HttpParams httpParamsRequest)
    {
        int soTimeout = timeout < 0 ? Integer.MAX_VALUE : (int) timeout;
        HttpConnectionParams.setSoTimeout(httpParamsRequest, soTimeout);
    }
}
