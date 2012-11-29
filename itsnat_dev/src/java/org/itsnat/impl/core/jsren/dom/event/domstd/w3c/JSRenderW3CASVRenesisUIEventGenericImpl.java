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
import org.w3c.dom.events.Event;
import org.w3c.dom.events.UIEvent;

/**
 *
 * @author jmarranz
 */
public class JSRenderW3CASVRenesisUIEventGenericImpl extends JSRenderW3CASVRenesisEventImpl
{
    public static final JSRenderW3CASVRenesisUIEventGenericImpl SINGLETON = new JSRenderW3CASVRenesisUIEventGenericImpl();

    /**
     * Creates a new instance of JSRenderW3CUIEventGenericImpl
     */
    public JSRenderW3CASVRenesisUIEventGenericImpl()
    {
    }

    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        // Leer notas del render del MouseEvent
        UIEvent uiEvt = (UIEvent)evt;

        StringBuffer code = new StringBuffer();
        code.append( super.getInitEvent(evt,evtVarName,clientDoc) );

        code.append( evtVarName + ".view = " + getViewPath(uiEvt.getView(),clientDoc) + ";\n" );
        code.append( evtVarName + ".detail = " + uiEvt.getDetail() + ";\n" );

        return code.toString();
    }
}
