package org.itsnat.droid;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;

/**
 * Created by jmarranz on 4/06/14.
 */
public interface Page
{
    public String getURL();
    public String getId();
    public InflatedLayout getInflatedLayout();
    public byte[] getContent();
    public ItsNatSession getItsNatSession();
    public ItsNatView getItsNatView(View view);
    public void dispose();
}
