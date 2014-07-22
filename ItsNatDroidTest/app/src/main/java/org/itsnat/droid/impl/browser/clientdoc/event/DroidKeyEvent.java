package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.KeyEvent;
import android.view.MotionEvent;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

/**
 * Created by jmarranz on 22/07/14.
 */
public class DroidKeyEvent extends DroidInputEvent
{
    public DroidKeyEvent(DroidEventListener listener, KeyEvent evtNative)
    {
        super(listener, evtNative);
    }
}
