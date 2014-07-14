package org.itsnat.droid;

import android.content.Context;
import android.view.View;

import org.apache.http.params.HttpParams;

/**
 * Created by jmarranz on 4/06/14.
 */
public interface Page
{
    public String getURL();
    public String getId();
    public Context getContext();
    public InflatedLayout getInflatedLayout();
    public HttpParams getHttpParams();
    public byte[] getContent();
    public ItsNatSession getItsNatSession();
    public ItsNatView getItsNatView(View view);
    public UserData getUserData();
    public ItsNatDoc getItsNatDoc();
    public void setOnEventErrorListener(OnEventErrorListener listener);
    public void setOnServerStateLostListener(OnServerStateLostListener listener);
    public void dispose();
}
