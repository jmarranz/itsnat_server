/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.listener.attachcli;

import org.itsnat.impl.core.listener.attachcli.ItsNatAttachedClientCometEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderItsNatAttachedClientCometEventListenerImpl
{

    public static String addItsNatAttachedClientCometEventListenerCode(ItsNatAttachedClientCometEventListenerWrapperImpl itsNatListener)
    {
        String listenerId = itsNatListener.getId();
        int commMode = itsNatListener.getCommModeDeclared();
        long eventTimeout = itsNatListener.getEventTimeout();

        return "itsNatDoc.sendAttachCometTaskRefresh(\"" + listenerId + "\"," + commMode + "," + eventTimeout + ");\n";
    }
    

}
