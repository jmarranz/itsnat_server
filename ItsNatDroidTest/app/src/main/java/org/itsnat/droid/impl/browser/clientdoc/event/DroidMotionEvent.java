package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.InputEvent;
import android.view.MotionEvent;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

/**
 * Created by jmarranz on 22/07/14.
 */
public class DroidMotionEvent extends DroidInputEvent
{
    public DroidMotionEvent(DroidEventListener listener, MotionEvent evtNative)
    {
        super(listener, evtNative);
    }
}
