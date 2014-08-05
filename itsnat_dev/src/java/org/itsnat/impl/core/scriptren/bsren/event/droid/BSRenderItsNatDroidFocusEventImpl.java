/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.bsren.event.droid;

import org.itsnat.core.event.droid.DroidFocusEvent;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class BSRenderItsNatDroidFocusEventImpl extends BSRenderItsNatDroidEventImpl
{
    public static final BSRenderItsNatDroidFocusEventImpl SINGLETON = new BSRenderItsNatDroidFocusEventImpl();

    @Override
    public String getCreateEventInstance(Event evt, ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        DroidFocusEvent evt2 = (DroidFocusEvent)evt; 
        return "itsNatDoc.createFocusEvent(\"" + evt.getType() + "\"," + evt2.hasFocus() + ")"; 
    }

}
