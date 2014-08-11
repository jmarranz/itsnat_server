/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.bsren.event.droid;

import org.itsnat.core.event.droid.DroidTextChangeEvent;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class BSRenderItsNatDroidTextChangeEventImpl extends BSRenderItsNatDroidEventImpl
{
    public static final BSRenderItsNatDroidTextChangeEventImpl SINGLETON = new BSRenderItsNatDroidTextChangeEventImpl();

    @Override
    public String getCreateEventInstance(Event evt, ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        DroidTextChangeEvent evt2 = (DroidTextChangeEvent)evt; 
        return "itsNatDoc.createTextChangeEvent(\"" + evt2.getNewText() + "\")"; 
    }

}
