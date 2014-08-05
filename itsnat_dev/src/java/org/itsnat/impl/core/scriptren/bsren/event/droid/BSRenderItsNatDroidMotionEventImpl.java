/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.bsren.event.droid;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.droid.DroidMotionEvent;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class BSRenderItsNatDroidMotionEventImpl extends BSRenderItsNatDroidInputEventImpl
{
    public static final BSRenderItsNatDroidMotionEventImpl SINGLETON = new BSRenderItsNatDroidMotionEventImpl();
    
    @Override
    public String getCreateEventInstance(Event evt, ClientDocumentStfulDelegateDroidImpl clientDoc)
    {       
        DroidMotionEvent evt2 = (DroidMotionEvent)evt; 
        if (evt2.getRawX() != 0) throw new ItsNatException("rawX property is obtained from native in the device, use an explicit 0 (default value) in creation of event");
        if (evt2.getRawY() != 0) throw new ItsNatException("rawY property is obtained from native in the device, use an explicit 0 (default value) in creation of event");        
        return "itsNatDoc.createMotionEvent(\"" + evt.getType() + "\"," + evt2.getX() + "f," + evt2.getY() + "f)";   // RawX y RawY se sacan de X e Y dentro de MotionEvent (al revés realmente)
    }
    

}
