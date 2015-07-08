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

package org.itsnat.impl.core.scriptren.jsren.event.domstd.w3c;

import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class JSRenderW3CEventDefaultImpl extends JSRenderW3CEventImpl
{
    public static final JSRenderW3CEventDefaultImpl SINGLETON = new JSRenderW3CEventDefaultImpl();

    /** Creates a new instance of JSHTMLEventRenderImpl */
    public JSRenderW3CEventDefaultImpl()
    {
    }

    protected String getEventGroup(Event evt)
    {
        return "Events";
    }

    @Override    
    public String getInitEvent(Event evt, String evtVarName, ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return getInitEventDefault(evt,evtVarName,clientDoc);
    }

}
