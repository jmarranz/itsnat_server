package org.itsnat.droid;

import android.content.Context;

import org.apache.http.params.HttpParams;

/**
 * Created by jmarranz on 4/06/14.
 */
public interface Page
{
    public ItsNatDroidBrowser getItsNatDroidBrowser();
    public String getURL();
    public String getId();
    public Context getContext();
    public HttpParams getHttpParams();
    public String getLoadedContent();
    public ItsNatSession getItsNatSession();
    public UserData getUserData();
    public ItsNatDoc getItsNatDoc();
    public void setOnEventErrorListener(OnEventErrorListener listener);
    public void setOnServerStateLostListener(OnServerStateLostListener listener);
    public PageRequest reusePageRequest();
    public void dispose();
}
