/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.listener;

import org.itsnat.impl.core.listener.ItsNatDOMEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderItsNatDOMEventListenerImpl
{
    public static String getUserCodeInsideCustomFunc(ItsNatDOMEventListenerWrapperImpl itsNatListener)
    {
        StringBuilder code = new StringBuilder();

        String extraParams = itsNatListener.getCodeToSendParamTransports();
        String preSendCode = itsNatListener.getPreSendCode();

        if (extraParams != null)
            code.append( extraParams );
        if (preSendCode != null)
            code.append( preSendCode );

        return code.toString();
    }    
}
