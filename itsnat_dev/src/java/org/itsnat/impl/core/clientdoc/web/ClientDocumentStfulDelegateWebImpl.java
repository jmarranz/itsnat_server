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

package org.itsnat.impl.core.clientdoc.web;

import java.util.HashSet;
import java.util.Set;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatContinueEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.comp.iframe.HTMLIFrameFileUploadImpl;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.NodeCacheRegistryImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.EventInternal;
import org.itsnat.impl.core.event.EventListenerInternal;
import org.itsnat.impl.core.scriptren.jsren.JSScriptUtilFromClientImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderNodeImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.dompath.NodeLocationWithParentImpl;
import org.itsnat.impl.core.registry.ItsNatContinueEventListenerRegistryImpl;
import org.itsnat.impl.core.util.MapUniqueId;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ClientDocumentStfulDelegateWebImpl extends ClientDocumentStfulDelegateImpl
{
    protected Set<String> clientCodeMethodSet;    
    protected SVGWebInfoImpl svgWebInfo;
    protected MapUniqueId<HTMLIFrameFileUploadImpl> fileUploadsMap;          
    protected ItsNatContinueEventListenerRegistryImpl continueListenerRegistry;    
    
    public ClientDocumentStfulDelegateWebImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
        
    }
   
    
    public BrowserWeb getBrowserWeb()
    {
        return (BrowserWeb)clientDoc.getBrowser();
    }
    
    public String getNodeReference(Node node,boolean cacheIfPossible,boolean errIfNull)
    {
        return JSRenderNodeImpl.getNodeReference(node,cacheIfPossible,errIfNull,this);
    }    
    
    public ScriptUtil createScriptUtil()
    {
        return new JSScriptUtilFromClientImpl(this);
    }    
        
    public SVGWebInfoImpl getSVGWebInfo()
    {
        return svgWebInfo;
    }

    public void setSVGWebInfo(boolean forceFlash,int metaForceFlashPos)
    {
        this.svgWebInfo = new SVGWebInfoImpl(this,forceFlash,metaForceFlashPos);
    }        
    




    public NodeLocationImpl getNodeLocationRelativeToParent(Node node)
    {
        return NodeLocationImpl.getNodeLocationRelativeToParent(this, node);
    }    
    
    public boolean hasClientMethodBound()
    {
        if (clientCodeMethodSet == null) return false;
        return !clientCodeMethodSet.isEmpty();
    }

    public Set<String> getClientMethodBoundSet()
    {
        if (clientCodeMethodSet == null)
            this.clientCodeMethodSet = new HashSet<String>();
        return clientCodeMethodSet;
    }

    public boolean isClientMethodBounded(String methodName)
    {
        if (clientCodeMethodSet == null)
            return false;
        return clientCodeMethodSet.contains(methodName);
    }

    public void bindClientMethod(String methodName)
    {
        Set<String> methods = getClientMethodBoundSet();
        boolean res = methods.add(methodName);
        if (!res) throw new ItsNatException("INTERNAL ERROR",this); // Se supone que antes de registrar se pregunta, evitamos así usar un mismo nombre para diferentes fines
    }
/*
    public void bindClientMethod(String methodName,String code)
    {
        bindClientMethod(methodName);
        addCodeToSend(code);
    }
*/    
    
    public boolean hasHTMLIFrameFileUploads()
    {
        if (fileUploadsMap == null) return false;
        return !fileUploadsMap.isEmpty();
    }
    
    public MapUniqueId<HTMLIFrameFileUploadImpl> getHTMLIFrameFileUploadMap()
    {
        if (fileUploadsMap == null)
            this.fileUploadsMap = new MapUniqueId<HTMLIFrameFileUploadImpl>(clientDoc.getUniqueIdGenerator()); // Así ahorramos memoria si no se usa
        return fileUploadsMap;
    }

    public HTMLIFrameFileUploadImpl getHTMLIFrameFileUploadImpl(String id)
    {
        if (fileUploadsMap == null) return null; // RARO
        return getHTMLIFrameFileUploadMap().get(id);
    }

    public void addHTMLIFrameFileUploadImpl(HTMLIFrameFileUploadImpl upload)
    {
        getHTMLIFrameFileUploadMap().put(upload);
    }

    public void removeHTMLIFrameFileUploadImpl(HTMLIFrameFileUploadImpl upload)
    {
        getHTMLIFrameFileUploadMap().remove(upload);
    }
    
    public Node getNodeFromStringPathFromClient(String pathStr,boolean cacheIfPossible)
    {
        if (pathStr.equals("null"))
            return null;

        NodeCacheRegistryImpl nodeCache = getNodeCacheRegistry();

        // El pathStr es generado por el navegador
        if (pathStr.startsWith("id:"))
        {
            // Formato: id:idvalue
            String id = pathStr.substring("id:".length());
            return nodeCache.getNodeById(id); // La caché debe estar activado sí o sí
        }
        else
        {
            // Nodo no cacheado
            Node node;
            String path;
            Node parent;
            String parentId;

            if (pathStr.startsWith("pid:"))
            {
                // El nodo no está cacheado pero el padre sí.
                // Formato: pid:idparent:pathrel
                int posSepPath = pathStr.lastIndexOf(':');
                parentId = pathStr.substring("pid:".length(),posSepPath);
                path = pathStr.substring(posSepPath + 1);
                // La caché debe estar activada sí o sí
                parent = nodeCache.getNodeById(parentId);  // parent no puede ser null
                if (parent == null) throw new ItsNatException("INTERNAL ERROR");
            }
            else
            {
                // Formato: pathabs
                path = pathStr;
                parent = null;
                parentId = null;
            }

            node = getNodeFromPath(path,parent);

            // En teoría node ha de encontrarse pues existe en el cliente
            // pero hay un caso en el que sí que puede ser null
            // y aun así ser tolerable y es el caso en el que en el servidor el nodo (y algunos padres)
            // no esté porque está cacheado porque está en un subárbol estático
            // Es el caso por ejemplo de listener asociado a un elemento
            // cuyos hijos son estáticos y cacheados en el servidor, el currentTarget
            // no es nulo pero sí puede ser el nodo "target" que puede ser un nodo
            // bajo el currentTarget cacheado en el servidor. El método getTarget()
            // devolverá nulo lo cual puede ser aceptable (el programador
            // deberá investigar que la causa es que en el servidor no está por ser cacheado).
            if (node == null) return null;

            if (cacheIfPossible && (nodeCache != null))
            {
                // Intentamos guardar en la caché y enviamos al cliente, así mejoramos el rendimiento
                // Hay que tener en cuenta que el nodo no está cacheado en el cliente
                // pero quizás al resolver otra referencia anteriormente que apunta al mismo
                // nodo es posible que ya lo hayamos cacheado en el servidor, por lo que
                // evitamos un intento de cachear de nuevo (pues da error).
                String id = nodeCache.getId(node);
                if (id != null) return node; // Ya fue cacheado

                id = nodeCache.addNode(node);  // node no puede ser null
                if (id != null) // Si es null es que el nodo no es cacheable o la caché está bloqueada
                {
                    NodeLocationWithParentImpl nodeLoc = NodeLocationWithParentImpl.getNodeLocationWithParent(node,id,path,parent,parentId,true,this);
                    addCodeToSend( JSRenderNodeImpl.addNodeToCache(nodeLoc) );
                }
            }

            return node;
        }
    }    
   

    
    public String removeNodeFromCacheAndSendCode(Node node)
    {    
        String oldId = removeNodeFromCache(node);
        if (oldId == null) return null;
         // Estaba cacheado
        addCodeToSend( JSRenderNodeImpl.removeNodeFromCache(oldId) );          
        return oldId;
    }    
    
    public boolean dispatchEvent(EventTarget target,Event evt,int commMode,long eventTimeout) throws EventException
    {
        final ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        if (Thread.holdsLock(itsNatDoc))
            throw new ItsNatException("Document must be unlocked in this call",this);
        if (getClientDocumentStful() != itsNatDoc.getEventDispatcherClientDocByThread())
            throw new ItsNatException("This thread is not an event dispatcher thread");

        ((EventInternal)evt).checkInitializedEvent();
        ((EventInternal)evt).setTarget(target);

        final long evtDispMaxWait = itsNatDoc.getEventDispatcherMaxWait();
        final boolean[] monitor = new boolean[1];

        synchronized(itsNatDoc)
        {
            JSRenderNodeImpl.addCodeDispatchEvent(target,evt,"res",this);

            EventListener listener = new EventListenerInternal()
            {
                public void handleEvent(Event evt)
                {
                    ItsNatContinueEvent contEvt = (ItsNatContinueEvent)evt;
                    // El hilo que ejecuta este método es un hilo request/response
                    monitor[0] = Boolean.getBoolean((String)contEvt.getExtraParam("itsnat_res"));
                    synchronized(monitor)
                    {
                        monitor.notifyAll(); // Desbloquea el hilo dispatcher de eventos
                    }
                    itsNatDoc.lockThread(evtDispMaxWait); // Bloquea el hilo del request/response para una posible siguiente llamada a dispatchEvent
                }
            };
            CustomParamTransport param = new CustomParamTransport("itsnat_res","res");
            getClientDocumentStful().addContinueEventListener(null,listener,commMode,new ParamTransport[]{param},null,eventTimeout);

            itsNatDoc.notifyAll();  // Desbloquea el hilo del request/response para que se envíe el código al browser
        }

        synchronized(monitor)
        {
            // Bloqueamos el hilo dispatcher de eventos esperando la respuesta del navegador
            try { monitor.wait(evtDispMaxWait); } catch(InterruptedException ex) { throw new ItsNatException(ex,this); }
        }

        return monitor[0];
    }
    
    
}
