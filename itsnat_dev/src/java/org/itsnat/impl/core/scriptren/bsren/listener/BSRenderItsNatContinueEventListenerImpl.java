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

package org.itsnat.impl.core.scriptren.bsren.listener;

import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatContinueEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class BSRenderItsNatContinueEventListenerImpl extends BSRenderItsNatDOMExtEventListenerImpl
{
    public static final BSRenderItsNatContinueEventListenerImpl SINGLETON = new BSRenderItsNatContinueEventListenerImpl();

    /** Creates a new instance of BSRenderItsNatContinueEventListenerImpl */
    public BSRenderItsNatContinueEventListenerImpl()
    {
    }

    private String addItsNatContinueEventListenerCode(ItsNatContinueEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        EventTarget currentTarget = itsNatListener.getCurrentTarget();

        String listenerId = itsNatListener.getId();
        int commMode = itsNatListener.getCommModeDeclared();
        long eventTimeout = itsNatListener.getEventTimeout();

        StringBuilder code = new StringBuilder();

        String functionVarName = addCustomFunctionCode(itsNatListener,code,clientDoc);

        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation((Node)currentTarget,true);
        // Hay que tener en cuenta que el currentTarget puede ser nulo
        code.append( "itsNatDoc.sendContinueEvent(" + nodeLoc.toScriptNodeLocation(false) + ",\"" + listenerId + "\"," + functionVarName + "," + commMode + "," + eventTimeout + ");\n" );

        return code.toString();
    }

    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return addItsNatContinueEventListenerCode((ItsNatContinueEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return null; // Nada que hacer
    }
}
