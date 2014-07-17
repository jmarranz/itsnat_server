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

package org.itsnat.impl.core.scriptren.jsren.dom.event.domstd.msie;

import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class JSRenderMSIEOldKeyEventImpl extends JSRenderMSIEOldUIEventImpl
{
    public static final JSRenderMSIEOldKeyEventImpl SINGLETON = new JSRenderMSIEOldKeyEventImpl();

    /** Creates a new instance of JSRenderMSIEOldKeyEventImpl */
    public JSRenderMSIEOldKeyEventImpl()
    {
    }

    @Override    
    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        ItsNatKeyEvent keyEvt = (ItsNatKeyEvent)evt;

        StringBuilder code = new StringBuilder();
        code.append( evtVarName + ".altKey=" + keyEvt.getAltKey() + ";\n" );
        code.append( evtVarName + ".ctrlKey=" + keyEvt.getCtrlKey() + ";\n" );
        code.append( evtVarName + ".shiftKey=" + keyEvt.getShiftKey() + ";\n" );

        long keyCode;
        String type = keyEvt.getType();
        if (type.equals("keypress"))
            keyCode = keyEvt.getCharCode();
        else
            keyCode = keyEvt.getKeyCode();
        code.append( evtVarName + ".keyCode=" + keyCode + ";\n" );

        return code.toString();
    }
}
