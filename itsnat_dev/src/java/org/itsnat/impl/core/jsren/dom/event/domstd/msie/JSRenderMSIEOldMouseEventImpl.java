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

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author jmarranz
 */
public class JSRenderMSIEOldMouseEventImpl extends JSRenderMSIEOldUIEventImpl
{
    public static final JSRenderMSIEOldMouseEventImpl SINGLETON = new JSRenderMSIEOldMouseEventImpl();

    /**
     * Creates a new instance of JSRenderMSIEOldMouseEventImpl
     */
    public JSRenderMSIEOldMouseEventImpl()
    {
    }

    public String getEventType()
    {
        return "MouseEvents";
    }

    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        MouseEvent mouseEvt = (MouseEvent)evt;

        StringBuffer code = new StringBuffer();
        code.append( evtVarName + ".screenX=" + mouseEvt.getScreenX() + ";\n" );
        code.append( evtVarName + ".screenY=" + mouseEvt.getScreenY() + ";\n" );
        code.append( evtVarName + ".clientX=" + mouseEvt.getClientX() + ";\n" );
        code.append( evtVarName + ".clientY=" + mouseEvt.getClientY() + ";\n" );
        code.append( evtVarName + ".ctrlKey=" + mouseEvt.getCtrlKey() + ";\n" );
        code.append( evtVarName + ".shiftKey=" + mouseEvt.getShiftKey() + ";\n" );
        code.append( evtVarName + ".altKey=" + mouseEvt.getAltKey() + ";\n" );

        int button = 0;
        switch(mouseEvt.getButton())
        {
            case 0: button = 1; break; // botón izquierdo
            case 2: button = 2; break; // botón derecho
            case 1: button = 4; break; // botón de enmedio
        }
        code.append( evtVarName + ".button=" + button + ";\n" );

        // fromElement ? , toElement ?
        return code.toString();
    }
}
