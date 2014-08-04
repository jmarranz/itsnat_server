/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.scriptren.bsren.dom.event.droid;

import org.itsnat.core.event.droid.DroidEvent;
import org.itsnat.core.event.droid.DroidFocusEvent;
import org.itsnat.core.event.droid.DroidInputEvent;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.event.domstd.w3c.JSRenderW3CEventImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.event.DroidEventGroupInfo;
import org.itsnat.impl.core.scriptren.bsren.dom.event.BSRenderNormalEventImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderItsNatDroidEventImpl extends BSRenderNormalEventImpl
{

    /**
     * Creates a new instance of JSRenderItsNatDOMStdEventImpl
     */
    public BSRenderItsNatDroidEventImpl()
    {
    }

    public static BSRenderItsNatDroidEventImpl getBSRenderItsNatDroidEvent(DroidEvent event)
    {
        if (event instanceof DroidInputEvent)
            return BSRenderItsNatDroidInputEventImpl.getBSRenderItsNatDroidInputEvent((DroidInputEvent)event);
        else if (event instanceof DroidFocusEvent)
            return BSRenderItsNatDroidFocusEventImpl.SINGLETON;
        return null;
    }

    public String getEventGroup(Event evt)    
    {
        return DroidEventGroupInfo.getEventGroup(evt);
    }
    
    public abstract String getCreateEventInstance(Event evt,ClientDocumentStfulDelegateDroidImpl clientDoc);


    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return "";
    }        
    
    
    // public abstract String getStopPropagation(String evtVarName,ClientDocumentStfulDelegateDroidImpl clientDoc);

    // public abstract String getPreventDefault(String evtVarName,ClientDocumentStfulDelegateDroidImpl clientDoc);

    /*
    public String stopAndPreventDefault(Event evt,String evtVarName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        EventInternal evtInt = (EventInternal)evt;

        StringBuilder code = new StringBuilder();
        if (evtInt.getStopPropagation())
            code.append(getStopPropagation(evtVarName,clientDoc));

        if (evtInt.getPreventDefault())
            code.append(getPreventDefault(evtVarName,clientDoc));

        return code.toString();
    }
    */
    
    public String getInitEventSystem(ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return "";
    }
    
    public String getCreateEventCode(Event evt,String evtVarName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        code.append( getInitEventSystem(clientDoc) );

        code.append( "var " + evtVarName + " = " + getCreateEventInstance(evt,clientDoc) + ";\n" );

        code.append( getInitEvent(evt,evtVarName,clientDoc) );

        //code.append( stopAndPreventDefault(evt,evtVarName,clientDoc) ); // Yo creo que puede ser prescindile pero por si acaso

        return code.toString();
    }

    @Override    
    public String getDispatchEvent(String varResName,NodeLocationImpl nodeLoc,Event evt,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        code.append( getCreateEventCode(evt,"evt",clientDoc) );

        code.append( getCallDispatchEvent(varResName,nodeLoc,evt,"evt",clientDoc) );

        return code.toString();
    }

    @Override    
    public String getDispatchEvent(String targetRef,Event evt,String evtVarName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        code.append( getCreateEventCode(evt,evtVarName,clientDoc) );

        code.append( getCallDispatchEvent(targetRef,evt,evtVarName,clientDoc) );

        return code.toString();
    }

    public String getCallDispatchEvent(String varResName,NodeLocationImpl nodeLoc,Event evt,String evtVarName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return "var " + varResName + " = itsNatDoc.dispatchEvent2(" + nodeLoc.toScriptNodeLocation(true) + ",\"" + evt.getType() + "\"," + evtVarName + ");\n";
    }

    public String getCallDispatchEvent(String targetRef,Event evt,String evtVarName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return "itsNatDoc.dispatchEvent(" + targetRef + ",\"" + evt.getType() + "\"," + evtVarName + ");\n";        
    }
}
