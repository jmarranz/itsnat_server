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
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author jmarranz
 */
public class JSRenderW3CMouseEventBatikImpl extends JSRenderW3CMouseEventImpl
{
    public static final JSRenderW3CMouseEventBatikImpl SINGLETON = new JSRenderW3CMouseEventBatikImpl();

    /** Creates a new instance of JSRenderW3CMouseEventDefaultImpl */
    public JSRenderW3CMouseEventBatikImpl()
    {
    }

    protected String getEventTypeGroup(Event evt)
    {
        return "MouseEvents";
    }

    @Override    
    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // En teoría Batik soporta bien DOM 2 Events pero hay un error
        // raro de la clase DOMMouseEvent de Batik con las teclas,
        // por ello usamos la alternativa initMouseEventNS de DOM 3
        // también soportada por Batik y que curiosamente funciona bien:
        // http://www.w3.org/TR/DOM-Level-3-Events/#events-event-type-initMouseEventNS
        // Como namespaceURIArg pasaremos null

        MouseEvent mouseEvt = (MouseEvent)evt;

        StringBuilder modifierList = new StringBuilder();
        if (mouseEvt.getCtrlKey())  modifierList.append("Control ");
        if (mouseEvt.getAltKey())   modifierList.append("Alt ");
        if (mouseEvt.getShiftKey()) modifierList.append("Shift ");
        if (mouseEvt.getMetaKey())  modifierList.append("Meta ");

        return evtVarName + ".initMouseEventNS("
                + "null,"
                + "\"" + mouseEvt.getType() + "\","
                + mouseEvt.getBubbles() + ","
                + mouseEvt.getCancelable() + ","
                + getViewPath(mouseEvt.getView(),clientDoc) + ","
                + mouseEvt.getDetail() + ","
                + mouseEvt.getScreenX() + ","
                + mouseEvt.getScreenY() + ","
                + mouseEvt.getClientX() + ","
                + mouseEvt.getClientY() + ","
                + mouseEvt.getButton() + ","
                + clientDoc.getNodeReference((Node)mouseEvt.getRelatedTarget(),true,false) + ","
                + "\"" + modifierList + "\"" + ");\n";
    }
}
