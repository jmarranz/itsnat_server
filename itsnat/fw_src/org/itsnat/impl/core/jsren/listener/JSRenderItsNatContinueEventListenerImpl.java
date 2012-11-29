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

package org.itsnat.impl.core.jsren.listener;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.listener.domext.ItsNatContinueEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatContinueEventListenerImpl extends JSRenderItsNatDOMExtEventListenerImpl
{
    public static final JSRenderItsNatContinueEventListenerImpl SINGLETON = new JSRenderItsNatContinueEventListenerImpl();

    /** Creates a new instance of JSRenderItsNatContinueEventListenerImpl */
    public JSRenderItsNatContinueEventListenerImpl()
    {
    }

    private String addItsNatContinueEventListenerCode(ItsNatContinueEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        EventTarget currentTarget = itsNatListener.getCurrentTarget();

        String listenerId = itsNatListener.getId();
        int commMode = itsNatListener.getCommModeDeclared();
        long eventTimeout = getEventTimeout(itsNatListener,clientDoc);

        StringBuffer code = new StringBuffer();

        String functionVarName = addCustomCodeFunction(itsNatListener,code);

        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation((Node)currentTarget,true);
        // Hay que tener en cuenta que el currentTarget puede ser nulo
        code.append( "itsNatDoc.sendContinueEvent(" + nodeLoc.toJSArray(false) + ",\"" + listenerId + "\"," + functionVarName + "," + commMode + "," + eventTimeout + ");\n" );

        return code.toString();
    }

    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        return addItsNatContinueEventListenerCode((ItsNatContinueEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        return null; // Nada que hacer
    }
}
