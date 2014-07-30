/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.listener;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatTimerEventListenerWrapperImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderItsNatTimerEventListenerImpl
{
    public static String addItsNatTimerEventListenerCode(ItsNatTimerEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateImpl clientDoc,RenderItsNatTimerEventListener render)
    {
        StringBuilder code = new StringBuilder();

        long delay = itsNatListener.getDelayFirstTime();

        EventTarget currentTarget = itsNatListener.getCurrentTarget();

        String listenerId = itsNatListener.getId();
        int commMode = itsNatListener.getCommModeDeclared();
        long eventTimeout = itsNatListener.getEventTimeout();

        String functionVarName = render.addCustomFunctionCode(itsNatListener,code,clientDoc);

        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation((Node)currentTarget,true);
        // Hay que tener en cuenta que currentTarget puede ser NULO
        code.append( "itsNatDoc.addTimerEL(" + nodeLoc.toScriptNodeLocation(false) + ",\"" + listenerId + "\"," + functionVarName + "," + commMode + "," + eventTimeout + "," + delay + ");\n" );

        return code.toString();
    }    
    
    public static String removeItsNatTimerEventListenerCode(ItsNatTimerEventListenerWrapperImpl itsNatListener)
    {
        String listenerId = itsNatListener.getId();

        StringBuilder code = new StringBuilder();

        code.append( "itsNatDoc.removeTimerEL(\"" + listenerId + "\");\n" );

        return code.toString();
    }    
    
    public static String updateItsNatTimerEventListenerCode(ItsNatTimerEventListenerWrapperImpl itsNatListener,long computedPeriod)
    {
        String listenerId = itsNatListener.getId();

        StringBuilder code = new StringBuilder();

        code.append( "\n" );
        code.append( "itsNatDoc.updateTimerEL(\"" + listenerId + "\"," + computedPeriod + ");\n" );

        return code.toString();
    }    
}
