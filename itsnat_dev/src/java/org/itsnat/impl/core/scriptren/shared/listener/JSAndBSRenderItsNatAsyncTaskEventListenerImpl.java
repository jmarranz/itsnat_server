/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.listener;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatAsyncTaskEventListenerWrapperImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderItsNatAsyncTaskEventListenerImpl
{
    public static String addItsNatAsyncTaskEventListenerCode(ItsNatAsyncTaskEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateImpl clientDoc,RenderItsNatAsyncTaskEventListener render)
    {
        EventTarget currentTarget = itsNatListener.getCurrentTarget();

        String listenerId = itsNatListener.getId();
        int sync = itsNatListener.getCommModeDeclared();
        long eventTimeout = itsNatListener.getEventTimeout();

        StringBuilder code = new StringBuilder();

        String functionVarName = render.addCustomFunctionCode(itsNatListener,code,clientDoc);

        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation((Node)currentTarget,true);
        // Hay que tener en cuenta que el currentTarget puede ser nulo
        code.append( "itsNatDoc.sendAsyncTaskEvent(" + nodeLoc.toScriptNodeLocation(false) + ",\"" + listenerId + "\"," + functionVarName + "," + sync + "," + eventTimeout + ");\n" );

        return code.toString();
    }    
}
