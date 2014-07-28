package org.itsnat.droid;

import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;

import java.util.List;

/**
 * Created by jmarranz on 10/06/14.
 */
public interface ItsNatSession
{
    public ItsNatDroidBrowser getItsNatDroidBrowser();
    public String getId();
    public int getPageCount();
    public List<Page> getPageList();
}
