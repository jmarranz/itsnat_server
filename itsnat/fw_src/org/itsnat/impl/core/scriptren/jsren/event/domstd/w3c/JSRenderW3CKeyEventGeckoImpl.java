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

import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class JSRenderW3CKeyEventGeckoImpl extends JSRenderW3CKeyEventImpl
{
    public static final JSRenderW3CKeyEventGeckoImpl SINGLETON = new JSRenderW3CKeyEventGeckoImpl();

    /**
     * Creates a new instance of JSRenderW3CKeyEventGeckoImpl
     */
    public JSRenderW3CKeyEventGeckoImpl()
    {
    }

    protected String getEventGroup(Event evt)
    {
        return "KeyEvents";
    }

    @Override    
    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
         ItsNatKeyEvent keyEvt = (ItsNatKeyEvent)evt;

        return evtVarName + ".initKeyEvent("
                + "\"" + keyEvt.getType() + "\","
                + keyEvt.getBubbles() + ","
                + keyEvt.getCancelable() + ","
                + getViewPath(keyEvt.getView(),clientDoc) + ","
                + keyEvt.getCtrlKey() + ","
                + keyEvt.getAltKey() + ","
                + keyEvt.getShiftKey() + ","
                + keyEvt.getMetaKey() + ","
                + keyEvt.getKeyCode() + ","
                + keyEvt.getCharCode()
                + ");\n";
    }
}
