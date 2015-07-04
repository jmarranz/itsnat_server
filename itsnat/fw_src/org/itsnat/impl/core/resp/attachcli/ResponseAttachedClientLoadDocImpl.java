/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.resp.attachcli;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulOwnerImpl;
import org.itsnat.impl.core.clientdoc.NodeCacheRegistryImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.BoundElementDocContainerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.droid.ItsNatStfulDroidDocumentImpl;
import org.itsnat.impl.core.doc.web.ItsNatStfulWebDocumentImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventImpl;
import org.itsnat.impl.core.scriptren.jsren.node.JSRenderNodeImpl;
import org.itsnat.impl.core.dompath.NodeLocationWithParentImpl;
import org.itsnat.impl.core.listener.attachcli.ItsNatAttachedClientEventListenerUtil;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClient;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClientLoadDocImpl;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.itsnat.impl.core.resp.attachcli.droid.ResponseAttachedClientLoadDocDroidImpl;
import org.itsnat.impl.core.resp.attachcli.web.ResponseAttachedClientLoadDocWebImpl;
import org.itsnat.impl.core.resp.shared.ResponseDelegateStfulLoadDocImpl;
import org.itsnat.impl.core.scriptren.bsren.node.BSRenderNodeImpl;
import org.itsnat.impl.core.util.MapUniqueId;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseAttachedClientLoadDocImpl extends ResponseAttachedClientLoadDocBaseImpl implements ResponseLoadStfulDocumentValid
{
    protected ResponseDelegateStfulLoadDocImpl responseDelegate;

    /** Creates a new instance of ResponseAttachedClientLoadDocImpl */
    public ResponseAttachedClientLoadDocImpl(RequestAttachedClientLoadDocImpl request)
    {
        super(request);

        this.responseDelegate = ResponseDelegateStfulLoadDocImpl.createResponseDelegateStfulLoadDoc(this);
    }

    public static ResponseAttachedClientLoadDocImpl createResponseAttachedClientLoadDoc(RequestAttachedClientLoadDocImpl request)
    {
        ItsNatStfulDocumentImpl itsNatDoc = request.getItsNatStfulDocument();
        if (itsNatDoc instanceof ItsNatStfulWebDocumentImpl)
            return ResponseAttachedClientLoadDocWebImpl.createResponseAttachedClientLoadDocWeb(request);
        else if (itsNatDoc instanceof ItsNatStfulDroidDocumentImpl)
            return new ResponseAttachedClientLoadDocDroidImpl(request);
        return null;
    }

    @Override
    public RequestAttachedClient getRequestAttachedClient()
    {
        return (RequestAttachedClient)request;
    }

    @Override
    public RequestAttachedClientLoadDocImpl getRequestAttachedClientLoadDoc()
    {
        return (RequestAttachedClientLoadDocImpl)request;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return getRequestAttachedClient().getItsNatStfulDocument();
    }

    public ClientDocumentAttachedClientImpl getClientDocumentAttachedClient()
    {
        return (ClientDocumentAttachedClientImpl)getClientDocument();
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return (ClientDocumentAttachedClientImpl)getClientDocument();
    }

    protected void processResponse()
    {
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();

        copyCacheFromOwner(clientDoc); // Antes de ejecutar código del usuario o notificar a los componentes, en general cualquier código que acceda al DOM.
        clientDoc.registerInSession();
        
        // Caso ItsNatAttachedClientEvent.LOAD:
        responseDelegate.processResponse();
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

        String code = genAddAttachUnloadListenerCode();

        clientDoc.addCodeToSend(code);
      
        clientDoc.startAttachedClient();  // Genera el JavaScript adecuado para enviar al servidor el primer evento para empezar a sincronizar (inicio de Comet o el primer evento timer).
    }

    public abstract String genAddAttachUnloadListenerCode();    
    

    
    public boolean isSerializeBeforeDispatching()
    {
        // El usuario con su AttachedClient listener tiene la oportunidad
        // de cambiar el DOM y los cambios deben manifestarse en el cliente
        // generando JavaScript.
        return true;
    }
    
    public ItsNatAttachedClientEventImpl createItsNatAttachedClientEvent()
    {
        RequestAttachedClientLoadDocImpl request = getRequestAttachedClientLoadDoc();
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
        return request.createItsNatAttachedClientEvent(clientDoc);
    }

    @Override
    public String serializeDocument()
    {
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();

        if (itsNatDoc.hasBoundElementDocContainers())
        {
            ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
            MapUniqueId<BoundElementDocContainerImpl> bindInfoList = itsNatDoc.getBoundElementDocContainerMap();
            for(Iterator<Map.Entry<String,BoundElementDocContainerImpl>> it = bindInfoList.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<String,BoundElementDocContainerImpl> entry = it.next();
                BoundElementDocContainerImpl bindInfo = entry.getValue();

                bindInfo.setURLForClientAttached(clientDoc);
            }

            String docMarkup = super.serializeDocument();

            for(Iterator<Map.Entry<String,BoundElementDocContainerImpl>> it = bindInfoList.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<String,BoundElementDocContainerImpl> entry = it.next();
                BoundElementDocContainerImpl bindInfo = entry.getValue();

                bindInfo.restoreOriginalURL(clientDoc);
            }

            return docMarkup;
        }
        else
            return super.serializeDocument();
    }

    public void copyCacheFromOwner(ClientDocumentAttachedClientImpl clientAttached)
    {
        // Copiamos la caché del cliente propietarios pues contiene los nodos más frecuentemente
        // usados, así aceleramos el cálculo de paths para el observador.
        ItsNatStfulDocumentImpl itsNatDoc = clientAttached.getItsNatStfulDocument();
        ClientDocumentStfulOwnerImpl clientDocOwner = itsNatDoc.getClientDocumentStfulOwner();
        ClientDocumentStfulDelegateImpl clientDocOwnerDeleg = clientDocOwner.getClientDocumentStfulDelegate();
        NodeCacheRegistryImpl nodeCacheOwner = clientDocOwnerDeleg.getNodeCacheRegistry();
        if ((nodeCacheOwner == null) || nodeCacheOwner.isEmpty())
            return;

        StringBuilder code = new StringBuilder();
        ClientDocumentStfulDelegateImpl clientAttachedDeleg = clientAttached.getClientDocumentStfulDelegate();        
        NodeCacheRegistryImpl nodeCacheObserver = clientAttachedDeleg.getNodeCacheRegistry(); // DEBE existir
        if (!nodeCacheObserver.isEmpty()) throw new ItsNatException("INTERNAL ERROR"); // Debe estar "virgen" no sea que hayamos ya antes cacheado nodos en el observador y estaríamos cacheando dos veces aunque sea con el mismo id lo cual no está permitido, provocamos error antes.
        ArrayList<LinkedList<Map.Entry<Node,String>>> cacheCopy = nodeCacheOwner.getOrderedByHeight();
        boolean cacheParentIfPossible = false;  // De esta manera evitamos un cacheado indirecto, el objetivo de este código es copiar una caché a otra, exactamente los mismos nodos
        for(int h = 0; h < cacheCopy.size(); h++)
        {
            LinkedList<Map.Entry<Node,String>> sameH = cacheCopy.get(h);
            if (sameH == null) continue;
            for(Map.Entry<Node,String> entry : sameH)
            {
                Node node = entry.getKey();
                String id = entry.getValue();
                // Los ids de los nodos son generados por el ItsNatDocumentImpl
                // por lo que pueden compartirse entre cachés de clientes.

                if (isIgnoredNodeForCaching(node)) continue;

                nodeCacheObserver.addNode(node,id); // node no puede ser nulo

                NodeLocationWithParentImpl nodeLoc = NodeLocationWithParentImpl.getNodeLocationWithParentUsingCache(node,id,cacheParentIfPossible,nodeCacheObserver);
                if (!nodeLoc.isJustCached())
                    throw new ItsNatException("INTERNAL ERROR");
                
                if (clientAttachedDeleg instanceof ClientDocumentStfulDelegateWebImpl)
                    code.append( JSRenderNodeImpl.addNodeToCache(nodeLoc) );
                else if (clientAttachedDeleg instanceof ClientDocumentStfulDelegateDroidImpl)
                    code.append( BSRenderNodeImpl.addNodeToCache(nodeLoc) );
            }
        }

        clientAttached.addCodeToSend(code);
    }


    public void preSerializeDocumentStful()
    {
        // Nada que hacer
    }

    public boolean isOnlyReturnMarkupOfFinalScripts()
    {
        return false;
    }

    public boolean isNeededAbsoluteURL()
    {
        return false;
    }

    public boolean isInlineLoadFrameworkScripts()
    {
        return false;
    }

    protected abstract boolean isIgnoredNodeForCaching(Node node);
}
