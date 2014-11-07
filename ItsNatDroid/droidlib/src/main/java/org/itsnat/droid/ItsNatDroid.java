package org.itsnat.droid;

/**
 * Created by jmarranz on 3/05/14.
 */
public interface ItsNatDroid
{
    public ItsNatDroidBrowser createItsNatDroidBrowser();
    public InflateLayoutRequest createInflateLayoutRequest();
    public String getVersionName();
    public int getVersionCode();
}
