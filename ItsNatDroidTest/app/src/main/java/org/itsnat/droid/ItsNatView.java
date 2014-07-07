package org.itsnat.droid;

import android.view.View;

/**
 * Created by jmarranz on 4/07/14.
 */
public interface ItsNatView
{
    public View getView();
    public String getXMLId();

    public void setOnClickListener(View.OnClickListener l);
    public void setOnTouchListener(View.OnTouchListener l);
}
