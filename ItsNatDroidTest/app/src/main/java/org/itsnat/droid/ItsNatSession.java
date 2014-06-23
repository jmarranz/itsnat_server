package org.itsnat.droid;

import java.util.List;

/**
 * Created by jmarranz on 10/06/14.
 */
public interface ItsNatSession
{
    public String getId();
    public int getPageCount();
    public List<Page> getPageList();
}
