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
import org.itsnat.impl.core.event.DOMStdEventTypeInfo;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderItsNatDOMStdEventListenerImpl extends BSRenderItsNatDOMEventListenerImpl
{
    /** Creates a new instance of BSRenderItsNatDOMStdEventListenerImpl */
    public BSRenderItsNatDOMStdEventListenerImpl()
    {
    }

    public static BSRenderItsNatDOMStdEventListenerImpl getBSRenderItsNatDOMStdEventListener(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return BSRenderItsNatDOMStdEventListenerDefaultImpl.SINGLETON;
    }

    protected abstract boolean needsAddListenerReturnElement();
    protected abstract boolean needsRemoveListenerReturnElement();

    protected String addItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        String type = itsNatListener.getType();
        EventTarget nodeTarget = itsNatListener.getCurrentTarget();

        String listenerId = itsNatListener.getId();

        int eventTypeCode = DOMStdEventTypeInfo.getEventTypeCode(type);
        boolean useCapture = itsNatListener.getUseCapture();
        int commMode = itsNatListener.getCommModeDeclared();
        long eventTimeout = itsNatListener.getEventTimeout();

        StringBuilder code = new StringBuilder();

        String functionVarName = addCustomFunctionCode(itsNatListener,code);

        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation((Node)nodeTarget,true);
        // El target en eventos estándar DOM NO puede ser nulo
        if (needsAddListenerReturnElement())
            code.append( "var elem = ");
        code.append( "itsNatDoc.addDOMEL(" + nodeLoc.toScriptNodeLocation(true) + ",\"" + type + "\",\"" + listenerId + "\"," + functionVarName + "," + useCapture + "," + commMode + "," + eventTimeout + "," + eventTypeCode + ");\n" );
        // El "elem" es utilizado por clases derivadas, elem puede ser window
        return code.toString();
    }

    protected String removeItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();
        String listenerId = itsNatListener.getId();
        if (needsRemoveListenerReturnElement())
            code.append( "var elem = ");
        code.append( "itsNatDoc.removeDOMEL(\"" + listenerId + "\");\n" );
        return code.toString();
    }

    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return addItsNatDOMStdEventListenerCode((ItsNatDOMStdEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return removeItsNatDOMStdEventListenerCode((ItsNatDOMStdEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

}
