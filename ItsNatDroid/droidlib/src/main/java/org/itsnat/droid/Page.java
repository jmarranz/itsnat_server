package org.itsnat.droid;

import android.content.Context;
import android.view.View;

import org.apache.http.params.HttpParams;

/**
 * Created by jmarranz on 4/06/14.
 */
public interface Page
{
    public ItsNatDroidBrowser getItsNatDroidBrowser();
    public String getURL();
    public String getId();
    public View getRootView();
    public View findViewByXMLId(String id);
    public Context getContext();
    public HttpParams getHttpParams();
    public String getLoadedContent();
    public ItsNatSession getItsNatSession();
    public ItsNatView getItsNatView(View view);
    public UserData getUserData();
    public ItsNatDoc getItsNatDoc();
    public void setOnEventErrorListener(OnEventErrorListener listener);
    public void setOnServerStateLostListener(OnServerStateLostListener listener);
    public void addEventMonitor(EventMonitor monitor);
    public boolean removeEventMonitor(EventMonitor monitor);
    public void setEnableEventMonitors(boolean value);
    public PageRequest reusePageRequest();
    public void dispose();
}
