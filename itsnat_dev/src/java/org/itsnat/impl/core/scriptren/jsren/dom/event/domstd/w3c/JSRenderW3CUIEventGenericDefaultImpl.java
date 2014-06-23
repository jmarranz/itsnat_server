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

package org.itsnat.impl.core.scriptren.jsren.dom.event.domstd.w3c;

import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.UIEvent;

/**
 *
 * @author jmarranz
 */
public class JSRenderW3CUIEventGenericDefaultImpl extends JSRenderW3CUIEventImpl
{
    public static final JSRenderW3CUIEventGenericDefaultImpl SINGLETON = new JSRenderW3CUIEventGenericDefaultImpl();

    /**
     * Creates a new instance of JSRenderW3CUIEventGenericImpl
     */
    public JSRenderW3CUIEventGenericDefaultImpl()
    {
    }

    protected String getEventTypeGroup(Event evt)
    {
        return "UIEvents";
    }

    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        UIEvent uiEvt = (UIEvent)evt;

        return evtVarName + ".initUIEvent("
                + "\"" + uiEvt.getType() + "\","
                + uiEvt.getBubbles() + ","
                + uiEvt.getCancelable() + ","
                + getViewPath(uiEvt.getView(),clientDoc) + ","
                + uiEvt.getDetail()
                + ");\n";
    }
}