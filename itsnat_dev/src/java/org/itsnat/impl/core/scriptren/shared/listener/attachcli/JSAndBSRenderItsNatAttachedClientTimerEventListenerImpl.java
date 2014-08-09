/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.listener.attachcli;

import org.itsnat.impl.core.listener.attachcli.ItsNatAttachedClientTimerEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderItsNatAttachedClientTimerEventListenerImpl
{
    public static String addItsNatAttachedClientTimerEventListenerCode(ItsNatAttachedClientTimerEventListenerWrapperImpl listener)
    {
        StringBuilder code = new StringBuilder();

        int commMode = listener.getCommModeDeclared(); // Llama a ClientDocumentAttachedClientTimerImpl.getCommMode()
        int interval = listener.getRefreshInterval(); // Llama a ClientDocumentAttachedClientTimerImpl.getRefreshInterval()
        long timeout = listener.getEventTimeout();
        
        code.append( "itsNatDoc.initAttachTimerRefresh(" + interval + "," + commMode + "," + timeout +");\n" );
                       
        return code.toString();
    }    
    
    public static String stopAttachTimerRefresh()
    {
        return "itsNatDoc.stopAttachTimerRefresh();";
    }        
    
    public static String stopAttachTimerRefreshTryCatch()
    {
        return "try{ itsNatDoc.stopAttachTimerRefresh(); } catch(e) { }";
    }     
    

}
