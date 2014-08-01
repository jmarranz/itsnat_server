/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.bsren.dom.event.droid;

import org.itsnat.core.event.droid.DroidInputEvent;
import org.itsnat.core.event.droid.DroidKeyEvent;
import org.itsnat.core.event.droid.DroidMotionEvent;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderItsNatDroidInputEventImpl extends BSRenderItsNatDroidEventImpl
{
    public static BSRenderItsNatDroidInputEventImpl getBSRenderItsNatDroidInputEvent(DroidInputEvent event)
    {
        if (event instanceof DroidMotionEvent)
            return BSRenderItsNatDroidMotionEventImpl.SINGLETON;
        else if (event instanceof DroidKeyEvent)
            return BSRenderItsNatDroidKeyEventImpl.SINGLETON;        
        return null;
    }
}
