package org.itsnat.droid.event;

import android.view.View;

/**
 * Created by jmarranz on 29/07/14.
 */
public interface NormalEvent extends Event
{
    public String getType();
    public View getCurrentTarget();
}
