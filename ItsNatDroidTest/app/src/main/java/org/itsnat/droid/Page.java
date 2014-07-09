package org.itsnat.droid;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;

/**
 * Created by jmarranz on 4/06/14.
 */
public interface Page
{
    public String getURL();
    public String getId();
    public Context getContext();
    public InflatedLayout getInflatedLayout();
    public byte[] getContent();
    public ItsNatSession getItsNatSession();
    public ItsNatView getItsNatView(View view);
    public UserData getUserData();
    public ItsNatDoc getItsNatDoc();
    public void dispose();
}
