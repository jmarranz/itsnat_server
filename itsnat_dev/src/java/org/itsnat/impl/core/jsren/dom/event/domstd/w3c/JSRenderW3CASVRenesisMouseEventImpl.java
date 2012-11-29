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

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author jmarranz
 */
public class JSRenderW3CASVRenesisMouseEventImpl extends JSRenderW3CASVRenesisEventImpl
{
    public static final JSRenderW3CASVRenesisMouseEventImpl SINGLETON = new JSRenderW3CASVRenesisMouseEventImpl();

    /** Creates a new instance of JSW3CDefaultMouseEventRenderImpl */
    public JSRenderW3CASVRenesisMouseEventImpl()
    {
    }

    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        MouseEvent mouseEvt = (MouseEvent)evt;

        StringBuffer code = new StringBuffer();
        code.append( super.getInitEvent(evt,evtVarName,clientDoc) );

        code.append( evtVarName + ".view = " + getViewPath(mouseEvt.getView(),clientDoc) + ";\n" );
        code.append( evtVarName + ".detail = " + mouseEvt.getDetail() + ";\n" );
        code.append( evtVarName + ".screenX = " + mouseEvt.getScreenX() + ";\n" );
        code.append( evtVarName + ".screenY = " + mouseEvt.getScreenY() + ";\n" );
        code.append( evtVarName + ".clientX = " + mouseEvt.getClientX() + ";\n" );
        code.append( evtVarName + ".clientY = " + mouseEvt.getClientY() + ";\n" );
        code.append( evtVarName + ".ctrlKey = " + mouseEvt.getCtrlKey() + ";\n" );
        code.append( evtVarName + ".altKey = " + mouseEvt.getAltKey() + ";\n" );
        code.append( evtVarName + ".shiftKey = " + mouseEvt.getShiftKey() + ";\n" );
        code.append( evtVarName + ".metaKey = " + mouseEvt.getMetaKey() + ";\n" );
        code.append( evtVarName + ".button = " + mouseEvt.getButton() + ";\n" );
        code.append( evtVarName + ".relatedTarget = " + clientDoc.getNodeReference((Node)mouseEvt.getRelatedTarget(),true,false) + ";\n" );

        return code.toString();
    }

}
