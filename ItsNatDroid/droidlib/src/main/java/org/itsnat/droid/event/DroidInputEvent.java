package org.itsnat.droid.event;

import android.view.InputEvent;

/**
 * Created by jmarranz on 29/07/14.
 */
public interface DroidInputEvent extends DroidEvent
{
    public InputEvent getInputEvent();
}
