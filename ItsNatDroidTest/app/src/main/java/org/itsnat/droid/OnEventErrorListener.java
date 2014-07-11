package org.itsnat.droid;

import android.view.InputEvent;

/**
 * Created by jmarranz on 11/07/14.
 */
public interface OnEventErrorListener
{
    public void onError(Exception ex,String type,InputEvent evt);
}
