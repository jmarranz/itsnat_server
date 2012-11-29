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
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatUserEventListenerWrapperImpl;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatUserEventListenerImpl extends JSRenderItsNatDOMExtEventListenerImpl
{
    public static final JSRenderItsNatUserEventListenerImpl SINGLETON = new JSRenderItsNatUserEventListenerImpl();

    /** Creates a new instance of JSRenderItsNatUserEventListenerImpl */
    public JSRenderItsNatUserEventListenerImpl()
    {
    }

    private String addItsNatUserEventListenerCode(ItsNatUserEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        EventTarget currentTarget = itsNatListener.getCurrentTarget();

        String name = itsNatListener.getName();
        String listenerId = itsNatListener.getId();
        int commMode = itsNatListener.getCommModeDeclared();
        long eventTimeout = getEventTimeout(itsNatListener,clientDoc);

        StringBuffer code = new StringBuffer();

        String functionVarName = addCustomCodeFunction(itsNatListener,code);

        // Hay que tener en cuenta que currentTarget puede ser NULO
        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation((Node)currentTarget,true);

        code.append( "itsNatDoc.addUserEventListener(" + nodeLoc.toJSArray(false) + ",\"" + name + "\",\"" + listenerId + "\"," + functionVarName + "," + commMode + "," + eventTimeout + ");\n" );

        return code.toString();
    }

    private String removeItsNatUserEventListenerCode(ItsNatUserEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        String listenerId = itsNatListener.getId();

        StringBuffer code = new StringBuffer();

        code.append( "\n" );
        code.append( "itsNatDoc.removeUserEventListener(\"" + listenerId + "\");\n" );

        return code.toString();
    }

    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        return addItsNatUserEventListenerCode((ItsNatUserEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        return removeItsNatUserEventListenerCode((ItsNatUserEventListenerWrapperImpl)itsNatListener,clientDoc);
    }
}
