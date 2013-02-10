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

package org.itsnat.impl.core.jsren.dom.event.domext;

import java.util.Iterator;
import java.util.Map;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.event.ItsNatEventImpl;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatUserEventImpl extends JSRenderItsNatDOMExtEventImpl
{
    public static final JSRenderItsNatUserEventImpl SINGLETON = new JSRenderItsNatUserEventImpl();

    /** Creates a new instance of JSRenderItsNatUserEventImpl */
    public JSRenderItsNatUserEventImpl()
    {
    }

    public String getCreateEventCode(Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        ItsNatEventImpl userEvt = (ItsNatEventImpl)evt;
        String name = ((ItsNatUserEvent)userEvt).getName();

        StringBuilder code = new StringBuilder();
        code.append("var evt = itsNatDoc.createUserEvent(\"" + name + "\");\n");
        if (userEvt.hasExtraParams())
        {
            Map extraParams = userEvt.getExtraParamMap();
            for(Iterator it = extraParams.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = (Map.Entry)it.next();
                String nameParam = (String)entry.getKey();
                Object value = entry.getValue();
                code.append( "evt.setExtraParam(\"" + nameParam + "\"," + javaToJS(value,true,clientDoc) + ");\n" );
            }
        }

        return code.toString();
    }

    public String getDispatchEvent(String varResName,NodeLocationImpl nodeLoc,Event evt)
    {
        StringBuilder code = new StringBuilder();
        code.append( getCreateEventCode(evt,"evt",nodeLoc.getClientDocumentStful()) );
        // Hay que tener en cuenta que el nodo target puede ser nulo
        code.append( "var " + varResName + " = itsNatDoc.dispatchUserEvent2(" + nodeLoc.toJSArray(false) + ",evt);\n" );
        return code.toString();
    }

    public String getDispatchEvent(String targetVarName,Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();
        code.append( getCreateEventCode(evt,evtVarName,clientDoc) );
        code.append( "itsNatDoc.dispatchUserEvent(" + targetVarName + "," + evtVarName + ");\n" );
        return code.toString();
    }
}
