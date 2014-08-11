/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.resp.attachcli.droid;

import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClientLoadDocImpl;
import org.itsnat.impl.core.resp.attachcli.ResponseAttachedClientLoadDocImpl;
import org.itsnat.impl.core.scriptren.bsren.listener.attachcli.BSRenderItsNatAttachedClientEventListenerImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ResponseAttachedClientLoadDocDroidImpl extends ResponseAttachedClientLoadDocImpl
{

    public ResponseAttachedClientLoadDocDroidImpl(RequestAttachedClientLoadDocImpl request)
    {
        super(request);
    }
    
    public static ResponseAttachedClientLoadDocDroidImpl createResponseAttachedClientLoadDocDroid(RequestAttachedClientLoadDocImpl request)
    {
        return new ResponseAttachedClientLoadDocDroidImpl(request);
    }    

    @Override
    protected boolean isIgnoredNodeForCaching(Node node)
    {
        // Es con SVGWeb (propio de HTML) con el que hay problemas
        return false;
    }
       
    @Override
    public String genAddAttachUnloadListenerCode()
    {
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();        
        return BSRenderItsNatAttachedClientEventListenerImpl.addAttachUnloadListenerCode(clientDoc);    
    }     
}
