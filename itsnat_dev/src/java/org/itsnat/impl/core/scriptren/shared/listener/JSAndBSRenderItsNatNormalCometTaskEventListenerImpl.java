/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.listener;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatNormalCometEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderItsNatNormalCometTaskEventListenerImpl
{
    public static String addNormalCometTaskEventListenerCode(ItsNatNormalCometEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateImpl clientDoc,RenderItsNatNormalCometTaskEventListener render)
    {
        StringBuilder code = new StringBuilder();        
        
        String listenerId = itsNatListener.getId();
        int sync = itsNatListener.getCommModeDeclared();
        long eventTimeout = itsNatListener.getEventTimeout();
        String functionVarName = render.addCustomFunctionCode(itsNatListener,code,clientDoc);
        
        code.append( "itsNatDoc.sendCometTaskEvent(\"" + listenerId + "\"," + functionVarName + "," + sync + "," + eventTimeout + ");\n" );

        return code.toString();
    }    
}
