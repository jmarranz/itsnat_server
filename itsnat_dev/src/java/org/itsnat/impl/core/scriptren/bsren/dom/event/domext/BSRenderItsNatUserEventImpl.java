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

package org.itsnat.impl.core.scriptren.bsren.dom.event.domext;

import java.util.Map;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.event.ItsNatEventImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class BSRenderItsNatUserEventImpl extends BSRenderItsNatDOMExtEventImpl
{
    public static final BSRenderItsNatUserEventImpl SINGLETON = new BSRenderItsNatUserEventImpl();

    /** Creates a new instance of JSRenderItsNatUserEventImpl */
    public BSRenderItsNatUserEventImpl()
    {
    }

    public String getCreateEventCode(Event evt,String evtVarName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        ItsNatEventImpl userEvt = (ItsNatEventImpl)evt;
        String name = ((ItsNatUserEvent)userEvt).getName();

        StringBuilder code = new StringBuilder();
        code.append("var evt = itsNatDoc.createUserEvent(\"" + name + "\");\n");
        if (userEvt.hasExtraParams())
        {
            Map<String,Object> extraParams = userEvt.getExtraParamMap();
            for(Map.Entry<String,Object> entry : extraParams.entrySet())
            {
                String nameParam = entry.getKey();
                Object value = entry.getValue();
                code.append( "evt.setExtraParam(\"" + nameParam + "\"," + javaToJS(value,true,clientDoc) + ");\n" );
            }
        }

        return code.toString();
    }

    @Override
    public String getDispatchEvent(String varResName,NodeLocationImpl nodeLoc,Event evt,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();
        code.append( getCreateEventCode(evt,"evt",clientDoc) );
        // Hay que tener en cuenta que el nodo target puede ser nulo
        code.append( "var " + varResName + " = itsNatDoc.dispatchUserEvent2(" + nodeLoc.toScriptNodeLocation(false) + ",evt);\n" );
        return code.toString();
    }

    @Override    
    public String getDispatchEvent(String targetVarName,Event evt,String evtVarName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();
        code.append( getCreateEventCode(evt,evtVarName,clientDoc) );
        code.append( "itsNatDoc.dispatchUserEvent(" + targetVarName + "," + evtVarName + ");\n" );
        return code.toString();
    }
}
