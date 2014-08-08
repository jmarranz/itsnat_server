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
    public void dispatchRequestListeners()
    {
        // Aunque sea en carga, se procesa como si fuera un evento.

        ItsNatAttachedClientEventImpl event = createItsNatAttachedClientEvent();
        ItsNatAttachedClientEventListenerUtil.handleEventIncludingGlobalListeners(event);

        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
        int phase = clientDoc.getPhase();
        if (phase == ItsNatAttachedClientEvent.UNLOAD) return;

        // Ahora sí tenemos claro que desde el cliente se deben enviar eventos

        int commMode = clientDoc.getCommModeDeclared();

        String nodeRefForUnload;
        String unloadType;
        if (clientDoc.getBrowser().isClientWindowEventTarget())
        {
            nodeRefForUnload = "itsNatDoc.win";
            unloadType = "unload";
        }
        else
        {
            nodeRefForUnload = "itsNatDoc.doc.documentElement";
            unloadType = "SVGUnload";  // En ASV  no se ejecuta pero en fin, por coherencia
        }

        String code = "itsNatDoc.addAttachUnloadListener(" + nodeRefForUnload + ",\"" + unloadType + "\"," + commMode + ");\n";

        clientDoc.addCodeToSend(code);

        super.dispatchRequestListeners(); 
    }    
}
