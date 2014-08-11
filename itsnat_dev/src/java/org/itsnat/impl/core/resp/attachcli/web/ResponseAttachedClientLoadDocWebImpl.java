/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.resp.attachcli.web;

import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.web.ItsNatHTMLDocumentImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventImpl;
import org.itsnat.impl.core.listener.attachcli.ItsNatAttachedClientEventListenerUtil;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClientLoadDocImpl;
import org.itsnat.impl.core.resp.attachcli.ResponseAttachedClientLoadDocImpl;
import org.itsnat.impl.core.scriptren.jsren.listener.attachcli.JSRenderItsNatAttachedClientEventListenerImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseAttachedClientLoadDocWebImpl extends ResponseAttachedClientLoadDocImpl
{

    public ResponseAttachedClientLoadDocWebImpl(RequestAttachedClientLoadDocImpl request)
    {
        super(request);
    }
    
    public static ResponseAttachedClientLoadDocWebImpl createResponseAttachedClientLoadDocWeb(RequestAttachedClientLoadDocImpl request)
    {
        ItsNatStfulDocumentImpl itsNatDoc = request.getItsNatStfulDocument();
        if (itsNatDoc instanceof ItsNatHTMLDocumentImpl)
            return new ResponseAttachedClientLoadDocHTMLImpl(request);
        else
            return new ResponseAttachedClientLoadDocOtherNSImpl(request);
    }    
    
    @Override
    public String genAddAttachUnloadListenerCode()
    {
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();        
        return JSRenderItsNatAttachedClientEventListenerImpl.addAttachUnloadListenerCode(clientDoc);
    }    
}
