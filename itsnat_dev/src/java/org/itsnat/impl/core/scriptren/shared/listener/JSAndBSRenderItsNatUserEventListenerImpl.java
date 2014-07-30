/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.listener;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatUserEventListenerWrapperImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderItsNatUserEventListenerImpl
{
    public static String addItsNatUserEventListenerCode(ItsNatUserEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateImpl clientDoc,RenderItsNatUserEventListener render)
    {
        EventTarget currentTarget = itsNatListener.getCurrentTarget();

        String name = itsNatListener.getName();
        String listenerId = itsNatListener.getId();
        int commMode = itsNatListener.getCommModeDeclared();
        long eventTimeout = itsNatListener.getEventTimeout();

        StringBuilder code = new StringBuilder();

        String functionVarName = render.addCustomFunctionCode(itsNatListener,code,clientDoc);

        // Hay que tener en cuenta que currentTarget puede ser NULO
        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation((Node)currentTarget,true);

        code.append( "itsNatDoc.addUserEL(" + nodeLoc.toScriptNodeLocation(false) + ",\"" + name + "\",\"" + listenerId + "\"," + functionVarName + "," + commMode + "," + eventTimeout + ");\n" );

        return code.toString();
    }    
    
    public static String removeItsNatUserEventListenerCode(ItsNatUserEventListenerWrapperImpl itsNatListener)
    {
        String listenerId = itsNatListener.getId();

        StringBuilder code = new StringBuilder();

        code.append( "itsNatDoc.removeUserEL(\"" + listenerId + "\");\n" );

        return code.toString();
    }    
}
