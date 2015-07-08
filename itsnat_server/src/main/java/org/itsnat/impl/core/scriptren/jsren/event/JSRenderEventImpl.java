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

package org.itsnat.impl.core.scriptren.jsren.event;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.ItsNatDOMExtEvent;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.jsren.JSRenderImpl;
import org.itsnat.impl.core.scriptren.jsren.event.domstd.JSRenderItsNatDOMStdEventImpl;
import org.itsnat.impl.core.scriptren.jsren.event.domext.JSRenderItsNatDOMExtEventImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderEventImpl extends JSRenderImpl
{

    /** Creates a new instance of JSRenderEventImpl */
    public JSRenderEventImpl()
    {
    }

    public static JSRenderEventImpl getJSEventRender(Event event,Browser browser)
    {
        if (event instanceof ItsNatDOMStdEvent)
            return JSRenderItsNatDOMStdEventImpl.getJSItsNatDOMStdEventRender((ItsNatDOMStdEvent)event,browser);
        else if (event instanceof ItsNatDOMExtEvent)
            return JSRenderItsNatDOMExtEventImpl.getJSRenderItsNatDOMExtEvent((ItsNatDOMExtEvent)event);
        else
            throw new ItsNatException("This event type is not supported",event);
    }

    public abstract String getDispatchEvent(String varResName,NodeLocationImpl nodeLoc,Event evt,ClientDocumentStfulDelegateWebImpl clientDoc);
    public abstract String getDispatchEvent(String targetVarName,Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc);

}
