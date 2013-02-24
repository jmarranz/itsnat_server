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

package org.itsnat.impl.core.jsren.dom.event.domstd.msie;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.event.HTMLEventInternal;
import org.itsnat.impl.core.jsren.dom.event.domstd.JSRenderItsNatDOMStdEventImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.events.UIEvent;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderMSIEOldEventImpl extends JSRenderItsNatDOMStdEventImpl
{

    /**
     * Creates a new instance of JSRenderMSIEOldEventImpl
     */
    public JSRenderMSIEOldEventImpl()
    {
    }

    public static JSRenderMSIEOldEventImpl getJSRenderMSIEOldEvent(ItsNatDOMStdEvent event,BrowserMSIEOld browser)
    {
        if (event instanceof MouseEvent)
            return JSRenderMSIEOldMouseEventImpl.SINGLETON;
        else if (event instanceof ItsNatKeyEvent)
            return JSRenderMSIEOldKeyEventImpl.SINGLETON;
        else if (event instanceof UIEvent)
            return JSRenderMSIEOldUIEventDefaultImpl.SINGLETON;
        else if (event instanceof HTMLEventInternal)
            return JSRenderMSIEOldEventDefaultImpl.SINGLETON;
        else if (event instanceof MutationEvent)
            throw new ItsNatException("MSIE does not support mutation events");
        else
            return JSRenderMSIEOldEventDefaultImpl.SINGLETON;
    }

    public String getCreateEventInstance(Event evt,ClientDocumentStfulImpl clientDoc)
    {
       return "itsNatDoc.doc.createEventObject()";
    }

    public String getInitEventDefault(Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        return "";
    }

    public String getStopPropagation(String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        // stopPropagation NO es estándar MSIE, sólo válido para ItsNat pero en el cliente se detecta
        // hay que tener en cuenta que el capture lo hace ItsNat
        return evtVarName + ".cancelBubble = true;" + evtVarName + ".itsnat_stopPropagation = true; \n";
    }

    public String getPreventDefault(String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        return evtVarName + ".returnValue = false;\n";
    }

}
