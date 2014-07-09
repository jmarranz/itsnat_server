package org.itsnat.droid;

import android.view.View;

import org.itsnat.droid.Page;

/**
 *
 * Created by jmarranz on 12/06/14.
 */
public interface ItsNatDoc
{
    public Page getPage();

    public void alert(Object value);
    public void alert(String title,Object value);
    public void toast(Object value,int duration);
    public void toast(Object value);

    public void addEventMonitor(EventMonitor monitor);
    public boolean removeEventMonitor(EventMonitor monitor);
    public void setEnableEventMonitors(boolean value);
}
