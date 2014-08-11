/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.bsren.event.droid;

import org.itsnat.core.event.droid.DroidEvent;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class BSRenderItsNatDroidOtherEventImpl extends BSRenderItsNatDroidEventImpl
{
    public static final BSRenderItsNatDroidOtherEventImpl SINGLETON = new BSRenderItsNatDroidOtherEventImpl();

    @Override
    public String getCreateEventInstance(Event evt, ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        DroidEvent evt2 = (DroidEvent)evt; // No hay necesidad por ahora de definir un DroidOtherEvent público 
        return "itsNatDoc.createOtherEvent()"; 
    }

}
