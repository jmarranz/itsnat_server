/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.bsren.event.droid;

import org.itsnat.core.event.droid.DroidKeyEvent;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class BSRenderItsNatDroidKeyEventImpl extends BSRenderItsNatDroidInputEventImpl
{
    public static final BSRenderItsNatDroidKeyEventImpl SINGLETON = new BSRenderItsNatDroidKeyEventImpl();
    
    @Override
    public String getCreateEventInstance(Event evt, ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        DroidKeyEvent evt2 = (DroidKeyEvent)evt; 
        return "itsNatDoc.createKeyEvent(\"" + evt.getType() + "\"," + evt2.getKeyCode() + ")"; 
    }

    
}
