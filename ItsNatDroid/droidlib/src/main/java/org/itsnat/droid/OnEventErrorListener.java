package org.itsnat.droid;

import org.itsnat.droid.event.Event;

/**
 * Created by jmarranz on 11/07/14.
 */
public interface OnEventErrorListener
{
    public void onError(Exception ex,Event evt);
}
