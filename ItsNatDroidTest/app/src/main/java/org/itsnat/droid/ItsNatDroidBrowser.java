package org.itsnat.droid;

import android.content.Context;

import org.apache.http.params.HttpParams;

/**
 * Created by jmarranz on 4/06/14.
 */
public interface ItsNatDroidBrowser
{
    public HttpParams getHttpParams();
    public void setHttpParams(HttpParams httpParams);
    public PageRequest createPageRequest();
}
