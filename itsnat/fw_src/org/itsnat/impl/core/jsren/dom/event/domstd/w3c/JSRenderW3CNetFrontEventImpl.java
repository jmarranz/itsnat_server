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
package org.itsnat.impl.core.jsren.dom.event.domstd.w3c;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.event.HTMLEventInternal;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.events.UIEvent;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderW3CNetFrontEventImpl extends JSRenderW3CEventImpl
{
    public JSRenderW3CNetFrontEventImpl()
    {
    }

    public static JSRenderW3CNetFrontEventImpl getJSRenderW3CNetFrontEvent(ItsNatDOMStdEvent event)
    {
        if (event instanceof MouseEvent)
            return JSRenderW3CNetFrontMouseEventImpl.SINGLETON;
        else if (event instanceof ItsNatKeyEvent)
            return JSRenderW3CNetFrontKeyEventImpl.SINGLETON;
        else if (event instanceof UIEvent)
            return JSRenderW3CNetFrontUIEventGenericImpl.SINGLETON;
        else if (event instanceof HTMLEventInternal)
            return JSRenderW3CNetFrontEventDefaultImpl.SINGLETON;
        else if (event instanceof MutationEvent)
            return null; // No soporta MutationEvents
        else
            return JSRenderW3CNetFrontEventDefaultImpl.SINGLETON;
    }

    protected String getEventTypeGroup(Event evt)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public String getInitEventSystem(ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( super.getInitEventSystem(clientDoc) ); // Por si acaso

        code.append(JSRenderManualDispatchImpl.getInitEventSystem(clientDoc,false));

        return code.toString();
    }

    public String getCreateEventInstance(Event evt,ClientDocumentStfulImpl clientDoc)
    {
        return "new Object()";
    }

    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        return JSRenderManualDispatchImpl.getInitEvent(evt, evtVarName);
    }

    public String getCallDispatchEvent(String varResName,NodeLocationImpl nodeLoc,Event evt,String evtVarName)
    {
        return "var " + varResName + " = " + JSRenderManualDispatchImpl.getCallDispatchEvent(varResName, nodeLoc, evt, evtVarName) + ";";
    }

    public String getCallDispatchEvent(String targetRef,Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        return JSRenderManualDispatchImpl.getCallDispatchEvent(targetRef,evt,evtVarName,clientDoc) + ";";
    }
}
