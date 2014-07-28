package org.itsnat.droid;

import android.view.View;

/**
 * Created by jmarranz on 16/06/14.
 */
public interface InflatedLayout
{
    public ItsNatDroid getItsNatDroid();
    public View getRootView();
    public View findViewByXMLId(String id);
}
