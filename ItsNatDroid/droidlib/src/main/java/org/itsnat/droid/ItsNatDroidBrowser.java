package org.itsnat.droid;

import android.content.Context;

import org.apache.http.params.HttpParams;

/**
 * Created by jmarranz on 4/06/14.
 */
public interface ItsNatDroidBrowser
{
    public ItsNatDroid getItsNatDroid();
    public HttpParams getHttpParams();
    public void setHttpParams(HttpParams httpParams);
    public PageRequest createPageRequest();
    public int getMaxPagesInSession();
    public void setMaxPagesInSession(int maxPages);
    public boolean isSSLSelfSignedAllowed();
    public void setSSLSelfSignedAllowed(boolean enable);
}
