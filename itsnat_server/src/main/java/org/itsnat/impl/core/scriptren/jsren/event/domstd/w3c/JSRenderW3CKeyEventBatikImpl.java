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
public class JSRenderW3CKeyEventBatikImpl extends JSRenderW3CKeyEventImpl
{
    public static final JSRenderW3CKeyEventBatikImpl SINGLETON = new JSRenderW3CKeyEventBatikImpl();

    /**
     * Creates a new instance of JSRenderW3CKeyEventGeckoImpl
     */
    public JSRenderW3CKeyEventBatikImpl()
    {
    }

    protected String getEventGroup(Event evt)
    {
        return "KeyEvents";
    }

    @Override    
    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // Batik define el key event a su manera de acuerdo con
        // el interface: org.apache.batik.dom.events.DOMKeyEvent
        // public void initKeyEvent(String typeArg,
        //    boolean canBubbleArg,boolean cancelableArg,boolean ctrlKeyArg,
        //    boolean altKeyArg,boolean shiftKeyArg,boolean metaKeyArg,
        //    int keyCodeArg,int charCodeArg,AbstractView viewArg)

         ItsNatKeyEvent keyEvt = (ItsNatKeyEvent)evt;

        return evtVarName + ".initKeyEvent("
                + "\"" + keyEvt.getType() + "\","
                + keyEvt.getBubbles() + ","
                + keyEvt.getCancelable() + ","
                + keyEvt.getCtrlKey() + ","
                + keyEvt.getAltKey() + ","
                + keyEvt.getShiftKey() + ","
                + keyEvt.getMetaKey() + ","
                + keyEvt.getKeyCode() + ","
                + keyEvt.getCharCode() + ","
                + getViewPath(keyEvt.getView(),clientDoc)
                + ");\n";
    }
}
