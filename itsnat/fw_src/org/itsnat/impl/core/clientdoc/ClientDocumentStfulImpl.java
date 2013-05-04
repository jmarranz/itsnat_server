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

package org.itsnat.impl.core.clientdoc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.itsnat.core.CometNotifier;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.event.CodeToSendListener;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatContinueEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.comp.iframe.HTMLIFrameFileUploadImpl;
import org.itsnat.impl.core.CommModeImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.comet.NormalCometNotifierImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatTimerImpl;
import org.itsnat.impl.core.event.EventInternal;
import org.itsnat.impl.core.event.EventListenerInternal;
import org.itsnat.impl.core.jsren.JSScriptUtilFromClientImpl;
import org.itsnat.impl.core.jsren.dom.node.JSRenderNodeImpl;
import org.itsnat.impl.core.listener.CometTaskEventListenerWrapper;
import org.itsnat.impl.core.listener.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatAsyncTaskEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatContinueEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatDOMEventStatelessListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatDOMExtEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatTimerEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatUserEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.mut.client.ClientMutationEventListenerStfulImpl;
import org.itsnat.impl.core.path.DOMPathResolver;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.itsnat.impl.core.path.NodeLocationWithParentImpl;
import org.itsnat.impl.core.registry.CometTaskRegistryImpl;
import org.itsnat.impl.core.registry.ItsNatAsyncTaskRegistryImpl;
import org.itsnat.impl.core.registry.ItsNatContinueEventListenerRegistryImpl;
import org.itsnat.impl.core.registry.ItsNatDOMStdEventListenerRegistryImpl;
import org.itsnat.impl.core.registry.ItsNatNormalCometTaskRegistryImpl;
import org.itsnat.impl.core.registry.ItsNatTimerEventListenerRegistryImpl;
import org.itsnat.impl.core.registry.ItsNatUserEventListenerRegistryImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.util.MapUniqueId;
import org.itsnat.impl.core.util.UniqueIdGenIntList;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ClientDocumentStfulImpl extends ClientDocumentImpl
{
    protected ItsNatStfulDocumentImpl itsNatDoc;
    protected CodeToSendRegistryImpl codeToSend = new CodeToSendRegistryImpl(this);
    protected String scriptLoadCode;
    protected NodeCacheRegistryImpl nodeCache;
    protected ItsNatTimerEventListenerRegistryImpl timerListenerRegistry;
    protected ItsNatContinueEventListenerRegistryImpl continueListenerRegistry;
    protected ItsNatAsyncTaskRegistryImpl asyncTaskRegistry;
    protected ItsNatDOMStdEventListenerRegistryImpl domStdListenerRegistry;
    protected ItsNatUserEventListenerRegistryImpl userListenerRegistry;
    protected Set<NormalCometNotifierImpl> cometNotifiers;
    protected ItsNatNormalCometTaskRegistryImpl cometTaskRegistry;
    protected Set<String> clientCodeMethodSet;
    protected ClientMutationEventListenerStfulImpl mutationListener;
    protected DelegateClientDocumentStfulImpl delegate;
    protected DOMPathResolver pathResolver;
    protected SVGWebInfoImpl svgWebInfo;
    protected MapUniqueId<HTMLIFrameFileUploadImpl> fileUploadsMap;
    protected JSScriptUtilFromClientImpl jsScriptUtil;
    protected LinkedList<EventListener> globalDomEventListeners;

    
    /** Creates a new instance of ClientDocumentStfulImpl */
    public ClientDocumentStfulImpl(ItsNatStfulDocumentImpl itsNatDoc,Browser browser,ItsNatSessionImpl session)
    {
        super(browser,session);

        this.itsNatDoc = itsNatDoc; // NO puede ser nulo.
        this.pathResolver = DOMPathResolver.createDOMPathResolver(this);
        this.mutationListener = ClientMutationEventListenerStfulImpl.createClientMutationEventListenerStful(this);
        
        // A día de hoy sólo los documentos HTML y SVG generan JavaScript necesario para mantener un caché de nodos en el cliente
        if (itsNatDoc.isNodeCacheEnabled())
            this.nodeCache = new NodeCacheRegistryImpl(this);
    }
    
    public UniqueIdGenIntList getUniqueIdGenerator()
    {
        return getItsNatDocumentImpl().getUniqueIdGenerator();
    }

    public SVGWebInfoImpl getSVGWebInfo()
    {
        return svgWebInfo;
    }

    public void setSVGWebInfo(boolean forceFlash,int metaForceFlashPos)
    {
        this.svgWebInfo = new SVGWebInfoImpl(this,forceFlash,metaForceFlashPos);
    }

    public DelegateClientDocumentStfulImpl getDelegateClientDocumentStful()
    {
        if (delegate == null) this.delegate = DelegateClientDocumentStfulImpl.createDelegateClientDocumentStful(this);
        return delegate;
    }

    public ItsNatDocumentImpl getItsNatDocumentImpl()
    {
        return getItsNatStfulDocument();
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return itsNatDoc;
    }

    public boolean canReceiveNormalEvents(ItsNatDOMEventListenerWrapperImpl evtListener)
    {
        if (evtListener instanceof ItsNatDOMEventStatelessListenerWrapperImpl)
            return true; // Es una excepción
        
        return canReceiveNormalEvents(evtListener.getEventListener());
    }
    
    
    public abstract boolean isEventsEnabled();
    public abstract boolean canReceiveALLNormalEvents();
    public abstract boolean canReceiveSOMENormalEvents();
    public abstract boolean canReceiveNormalEvents(EventListener listener);

    public void normalEventReceivedInDocument()
    {
        // Se redefine en un caso
    }

    public int getCommMode()
    {
        return itsNatDoc.getCommMode();
    }

    public long getEventTimeout()
    {
        return itsNatDoc.getEventTimeout();
    }

    public void normalEventReceived()
    {
        this.lastEventTime = System.currentTimeMillis();

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        itsNatDoc.normalEventReceived(this);
    }

    public ClientMutationEventListenerStfulImpl getClientMutationEventListenerStful()
    {
        return mutationListener;
    }

    public CodeToSendRegistryImpl getCodeToSendRegistry()
    {
        return codeToSend;
    }

    public String getCodeToSendAndReset()
    {
        return getCodeToSendRegistry().getCodeToSendAndReset();
    }

    public Object getLastCodeToSend()
    {
        return getCodeToSendRegistry().getLastCodeToSend();
    }

    public void addCodeToSend(Object code)
    {
        getCodeToSendRegistry().addCodeToSend(code);
    }
/*
    public void addCodeToSend(int index,Object code)
    {
        getCodeToSendRegistry().addCodeToSend(index,code);
    }
*/
    public boolean isSendCodeEnabled()
    {
        return getCodeToSendRegistry().isSendCodeEnabled();
    }

    public void disableSendCode()
    {
        getCodeToSendRegistry().disableSendCode();
    }

    public void enableSendCode()
    {
        getCodeToSendRegistry().enableSendCode();
    }

    public void addCodeToSendListener(CodeToSendListener listener)
    {
        getCodeToSendRegistry().addCodeToSendListener(listener);
    }

    public void removeCodeToSendListener(CodeToSendListener listener)
    {
        getCodeToSendRegistry().removeCodeToSendListener(listener);
    }

    public DOMPathResolver getDOMPathResolver()
    {
        return pathResolver;
    }

    public boolean isNodeCacheEnabled()
    {
        return nodeCache != null;
    }

    public NodeCacheRegistryImpl getNodeCacheRegistry()
    {
        return nodeCache; // puede ser null (no caché)
    }

    public String getCachedNodeId(Node node)
    {
        NodeCacheRegistryImpl cacheNode = getNodeCacheRegistry();
        if (cacheNode == null) return null;
        return cacheNode.getId(node);
    }

    public String removeNodeFromCache(Node node)
    {
        NodeCacheRegistryImpl cacheNode = getNodeCacheRegistry();
        if (cacheNode == null)
            return null;
        return cacheNode.removeNode(node);
    }

    public String removeNodeFromCacheAndSendCode(Node node)
    {    
        String oldId = removeNodeFromCache(node);
        if (oldId == null) return null;
         // Estaba cacheado
        addCodeToSend( JSRenderNodeImpl.removeNodeFromCache(oldId) );          
        return oldId;
    }

    public Node getNodeFromPath(String pathStr,Node topParent)
    {
        return getDOMPathResolver().getNodeFromPath(pathStr,topParent);
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

    public NodeLocationImpl getNodeLocation(Node node,boolean cacheIfPossible)
    {
        return NodeLocationImpl.getNodeLocation(this,node,cacheIfPossible);
    }

    public NodeLocationImpl getRefNodeLocationInsertBefore(Node newNode,Node nextSibling)
    {
        return NodeLocationImpl.getRefNodeLocationInsertBefore(this, newNode, nextSibling);
    }

    public NodeLocationImpl getNodeLocationRelativeToParent(Node node)
    {
        return NodeLocationImpl.getNodeLocationRelativeToParent(this, node);
    }

    public String getRelativeStringPathFromNodeParent(Node child)
    {
        return getDOMPathResolver().getRelativeStringPathFromNodeParent(child);
    }

    public String getStringPathFromNode(Node node)
    {
        return getDOMPathResolver().getStringPathFromNode(node);
    }

    public String getStringPathFromNode(Node node,Node topParent)
    {
        return getDOMPathResolver().getStringPathFromNode(node,topParent);
    }

    public Node getNextSiblingInClientDOM(Node node)
    {
        return getDOMPathResolver().getNextSiblingInClientDOM(node);        
    }
    
    public String getScriptLoadCode()
    {
        if (scriptLoadCode == null)
            throw new ItsNatException("Script code on load was already loaded",this);
        String code = scriptLoadCode;
        this.scriptLoadCode = null; // Para no gastar memoria
        return code;
    }

    public void setScriptLoadCode(String code)
    {
        this.scriptLoadCode = code;
    }

    public String getNodeReference(Node node,boolean cacheIfPossible,boolean errIfNull)
    {
        return JSRenderNodeImpl.getNodeReference(node,cacheIfPossible,errIfNull,this);
    }

    public boolean hasContinueEventListeners()
    {
        if (continueListenerRegistry == null)
            return false;
        return !continueListenerRegistry.isEmpty();
    }

    public ItsNatContinueEventListenerRegistryImpl getContinueEventListenerRegistry()
    {
        if (continueListenerRegistry == null)
            continueListenerRegistry = new ItsNatContinueEventListenerRegistryImpl(this); // para ahorrar memoria si no se usa
        return continueListenerRegistry;
    }

    public void addContinueEventListener(EventTarget target, EventListener listener)
    {
        int commMode = getCommMode();
        long eventTimeout = getEventTimeout();
        addContinueEventListener(target,listener,commMode,null,null,eventTimeout);
    }

    public void addContinueEventListener(EventTarget target,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        addContinueEventListener(target,listener,commMode,extraParams,preSendCode,eventTimeout,null);
    }

    public void addContinueEventListener(EventTarget target,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        // El propio request no será el encargado de esperar a que termine el proceso background
        // aunque podría, sino un nuevo request, así permitimos que el hilo que registra la tarea
        // no necesariamente es un hilo request y además permitimos fácilmente que puedan añadirse varias
        // tareas de este tipo en el mismo request. Finalmente: así encaja el modelo de event-listener con los demás tipos de eventos
        getContinueEventListenerRegistry().addContinueEventListener(target,listener,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
    }

    public ItsNatContinueEventListenerWrapperImpl removeContinueEventListener(String id)
    {
        // Es interna es llamada por el framework
        return getContinueEventListenerRegistry().removeContinueEventListenerById(id);
    }

    public int removeAllContinueEventListeners(EventTarget target,boolean updateClient)
    {
        if (!hasContinueEventListeners()) return 0;

        return getContinueEventListenerRegistry().removeAllItsNatContinueEventListeners(target,updateClient);
    }

    public ItsNatTimer createItsNatTimer()
    {
        return new ItsNatTimerImpl(this); // No hace falta que el programador lo "sujete" pues si registra alguna task el timer handler es el que se registra en el documento.
    }

    public boolean hasItsNatTimerEventListeners()
    {
        if (timerListenerRegistry == null)
            return false;
        return !timerListenerRegistry.isEmpty();
    }

    public ItsNatTimerEventListenerRegistryImpl getItsNatTimerEventListenerRegistry()
    {
        // Los handler/listener pueden ser de varios ItsNatTimer, pero no hay problema porque el idObj de cada uno lo genera el documento

        if (timerListenerRegistry == null)
            timerListenerRegistry = new ItsNatTimerEventListenerRegistryImpl(this); // para ahorrar memoria si no se usa
        return timerListenerRegistry;
    }

    public ItsNatTimerEventListenerWrapperImpl getTimerEventListenerById(String id)
    {
        return getItsNatTimerEventListenerRegistry().getItsNatTimerEventListenerById(id);
    }

    public int removeAllTimerEventListeners(EventTarget target,boolean updateClient)
    {
        if (!hasItsNatTimerEventListeners()) return 0;

        return getItsNatTimerEventListenerRegistry().removeAllItsNatTimerEventListeners(target,updateClient);
    }

    public ItsNatAsyncTaskRegistryImpl getAsyncTaskRegistry()
    {
        if (asyncTaskRegistry == null)
            this.asyncTaskRegistry = new ItsNatAsyncTaskRegistryImpl(this); // para ahorrar memoria si no se usa
        return asyncTaskRegistry;
    }

    public void addAsynchronousTask(Runnable task,EventListener listener)
    {
        // Si maxWait es 0 lo más coherente es que eventTimeout sea también -1 (indefinido, no timeout) y no el de por defecto de ItsNatDocument
        // Es muy conveniente el modo ASYNC o SCRIPT pues como ASYNC_HOLD o SCRIPT_HOLD el siguiente evento
        // tendría que esperar (retención JavaScript) a que terminara la tarea larga.
        int commMode = CommModeImpl.getPreferredPureAsyncMode(this);
        addAsynchronousTask(task,false,0,null,listener,commMode,null,null,-1);
    }

    public void addAsynchronousTask(Runnable task,boolean lockDoc,int maxWait,EventTarget element,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        addAsynchronousTask(task,lockDoc,maxWait,element,listener,commMode,extraParams,preSendCode,eventTimeout,null);
    }

    public void addAsynchronousTask(Runnable task,boolean lockDoc,int maxWait,EventTarget element,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        getAsyncTaskRegistry().addAsynchronousTask(task,lockDoc,maxWait,element,listener,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
    }

    public ItsNatAsyncTaskEventListenerWrapperImpl removeAsynchronousTask(String id)
    {
        return getAsyncTaskRegistry().removeAsynchronousTask(id);
    }

    public void addEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture)
    {
        int commMode = getCommMode();
        long eventTimeout = getEventTimeout();
        addEventListener(nodeTarget,type,listener,useCapture,commMode,null,null,eventTimeout,null);
    }

    public void addEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode)
    {
        long eventTimeout = getEventTimeout();
        addEventListener(nodeTarget,type,listener,useCapture,commMode,null,null,eventTimeout,null);
    }

    public void addEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,ParamTransport extraParam)
    {
        ParamTransport[] extraParams = new ParamTransport[]{ extraParam };
        addEventListener(nodeTarget,type,listener,useCapture,extraParams);
    }

    public void addEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,ParamTransport[] extraParams)
    {
        int commMode = getCommMode();
        long eventTimeout = getEventTimeout();
        addEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,null,eventTimeout,null);
    }

    public void addEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,String preSendCode)
    {
        int commMode = getCommMode();
        long eventTimeout = getEventTimeout();
        addEventListener(nodeTarget,type,listener,useCapture,commMode,null,preSendCode,eventTimeout,null);
    }

    public void addEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        addEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,null);
    }

    public void addEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        if (ItsNatDOMExtEventListenerWrapperImpl.isExtensionType(type))
            addDOMExtEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
        else
            addDOMStdEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
    }

    public void addDOMStdEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        getDOMStdEventListenerRegistry().addItsNatDOMStdEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
    }

    public void addDOMExtEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        if (useCapture) throw new ItsNatException("Capturing is not allowed for this type:" + type,this);

        if (ItsNatUserEventListenerWrapperImpl.isUserType(type))
        {
            String name = ItsNatUserEventListenerWrapperImpl.getNameFromType(type,false);
            addUserEventListener(nodeTarget,name,listener,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
        }
        else if (ItsNatContinueEventListenerWrapperImpl.isContinueType(type))
        {
            addContinueEventListener(nodeTarget,listener,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
        }
        else // itsnat:timer, itsnat:asynctask o itsnat:comet
            throw new ItsNatException("This method is not allowed to register this event listener type:" + type,this);
    }

    public void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture,int commMode,String preSendCode,long eventTimeout)
    {
        addMutationEventListener(target,listener,useCapture,commMode,preSendCode,eventTimeout,null);
    }

    public void addMutationEventListener(EventTarget nodeTarget,EventListener mutationListener,boolean useCapture)
    {
        getDOMStdEventListenerRegistry().addMutationEventListener(nodeTarget,mutationListener,useCapture,getCommMode(),getEventTimeout());
    }

    public void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture,int commMode,String preSendCode,long eventTimeout,String bindToListener)
    {
        getDOMStdEventListenerRegistry().addMutationEventListener(target,listener,useCapture,commMode,preSendCode,eventTimeout,bindToListener);
    }

    public void removeMutationEventListener(EventTarget target,EventListener listener,boolean useCapture)
    {
        getDOMStdEventListenerRegistry().removeMutationEventListener(target,listener,useCapture,true);
    }

    public boolean hasDOMStdEventListeners()
    {
        if (domStdListenerRegistry == null)
            return false;
        return !domStdListenerRegistry.isEmpty();
    }

    public ItsNatDOMStdEventListenerRegistryImpl getDOMStdEventListenerRegistry()
    {
        if (domStdListenerRegistry == null) // Evita instanciar si no se usa, caso de servir XML
            this.domStdListenerRegistry = new ItsNatDOMStdEventListenerRegistryImpl(getItsNatStfulDocument(),this);
        return domStdListenerRegistry;
    }

    public void removeEventListener(EventTarget target,String type,EventListener listener,boolean useCapture)
    {
        removeEventListener(target,type,listener,useCapture,true);
    }

    public void removeEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        if (ItsNatDOMExtEventListenerWrapperImpl.isExtensionType(type))
            removeDOMExtEventListener(target,type,listener,useCapture,updateClient);
        else
            removeDOMStdEventListener(target,type,listener,useCapture,updateClient);
    }

    public void removeDOMStdEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        getDOMStdEventListenerRegistry().removeItsNatDOMStdEventListener(target,type,listener,useCapture,updateClient);
    }

    public void removeDOMExtEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        if (useCapture) return; // Como no puede haber listeners registrados capturing no hacemos nada

        if (ItsNatUserEventListenerWrapperImpl.isUserType(type))
        {
            String name = ItsNatUserEventListenerWrapperImpl.getNameFromType(type,false);
            removeUserEventListener(target,name,listener,updateClient);
        }
        else  // itsnat:continue, itsnat:timer, itsnat:asynctask o itsnat:comet , en estos tipos el desregistro se hace internamente.
            throw new ItsNatException("This method is not allowed to unregister this event listener type:" + type,this);
    }

    public int removeAllDOMStdEventListeners(EventTarget target,boolean updateClient)
    {
        if (!hasDOMStdEventListeners()) return 0;

        return getDOMStdEventListenerRegistry().removeAllItsNatDOMStdEventListeners(target,updateClient);
    }

    public ItsNatDOMStdEventListenerWrapperImpl getDOMStdEventListenerById(String listenerId)
    {
        ItsNatDOMStdEventListenerWrapperImpl listener = null;

        if (hasDOMStdEventListeners())
            listener = getDOMStdEventListenerRegistry().getItsNatDOMStdEventListenerById(listenerId);

        if (listener == null)
            listener = itsNatDoc.getDOMStdEventListenerById(listenerId);

        return listener;
    }

    public boolean hasUserEventListeners()
    {
        if (userListenerRegistry == null)
            return false;
        return !userListenerRegistry.isEmpty();
    }

    public ItsNatUserEventListenerRegistryImpl getUserEventListenerRegistry()
    {
        if (userListenerRegistry == null)
            userListenerRegistry = new ItsNatUserEventListenerRegistryImpl(getItsNatStfulDocument(),this); // para ahorrar memoria si no se usa
        return userListenerRegistry;
    }

    public void addUserEventListener(EventTarget target,String name,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        addUserEventListener(target,name,listener,commMode,extraParams,preSendCode,eventTimeout,null);
    }

    public void addUserEventListener(EventTarget target,String name,EventListener listener)
    {
        addUserEventListener(target,name,listener,getCommMode(),null,null,getEventTimeout(), null);
    }

    public void addUserEventListener(EventTarget target,String name,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        getUserEventListenerRegistry().addItsNatUserEventListener(target,name,listener,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
    }

    public ItsNatUserEventListenerWrapperImpl getUserEventListenerById(String listenerId)
    {
        ItsNatUserEventListenerWrapperImpl listener = null;

        if (hasUserEventListeners())
            listener = getUserEventListenerRegistry().getItsNatUserEventListenerById(listenerId);

        if (listener == null)
            listener = itsNatDoc.getUserEventListenerById(listenerId);

        return listener;
    }

    public void removeUserEventListener(EventTarget target,String name,EventListener listener)
    {
        removeUserEventListener(target,name,listener,true);
    }

    public void removeUserEventListener(EventTarget target,String name,EventListener listener,boolean updateClient)
    {
        getUserEventListenerRegistry().removeItsNatUserEventListener(target,name,listener,updateClient);
    }

    public int removeAllUserEventListeners(EventTarget target,boolean updateClient)
    {
        if (!hasUserEventListeners()) return 0;

        return getUserEventListenerRegistry().removeAllItsNatUserEventListeners(target,updateClient);
    }

    public boolean hasGlobalEventListenerListeners()
    {
        if (globalDomEventListeners == null)
            return false;
        return !globalDomEventListeners.isEmpty();
    }     
    
    public LinkedList<EventListener> getGlobalEventListenerList()
    {
        if (globalDomEventListeners == null)
            this.globalDomEventListeners = new LinkedList<EventListener>();
        return globalDomEventListeners;
    }

    public void getGlobalEventListenerList(LinkedList<EventListener> list)
    {
        if (globalDomEventListeners == null)
            return;
        list.addAll(globalDomEventListeners);
    }

    public void addEventListener(EventListener listener)
    {
        LinkedList<EventListener> globalDomEventListeners = getGlobalEventListenerList();
        globalDomEventListeners.add(listener);
    }

    public void addEventListener(int index,EventListener listener)
    {
        LinkedList<EventListener> globalDomEventListeners = getGlobalEventListenerList();
        globalDomEventListeners.add(index,listener);
    }

    public void removeEventListener(EventListener listener)
    {
        LinkedList<EventListener> globalDomEventListeners = getGlobalEventListenerList();
        globalDomEventListeners.remove(listener);
    }


    public CometNotifier createCometNotifier()
    {
        long eventTimeout = getEventTimeout();
        return createCometNotifier(eventTimeout);
    }

    public CometNotifier createCometNotifier(long eventTimeout)
    {
        int commMode = CommModeImpl.getPreferredPureAsyncMode(this);
        return createCometNotifier(commMode,eventTimeout);
    }

    public CometNotifier createCometNotifier(int commMode,long eventTimeout)
    {
        return new NormalCometNotifierImpl(commMode,eventTimeout,this);
    }

    public boolean hasCometNotifiers()
    {
        if (cometNotifiers == null)
            return false;
        return !cometNotifiers.isEmpty();
    }

    public Set<NormalCometNotifierImpl> getCometNotifierSet()
    {
        if (cometNotifiers == null)
            this.cometNotifiers = new HashSet<NormalCometNotifierImpl>(); // para ahorrar memoria si no se usa
        return cometNotifiers;
    }

    public void addCometNotifier(NormalCometNotifierImpl notifier)
    {
        getCometNotifierSet().add(notifier);
    }

    public void removeCometNotifier(NormalCometNotifierImpl notifier)
    {
        getCometNotifierSet().remove(notifier);
    }

    public CometTaskRegistryImpl getCometTaskRegistry()
    {
        if (cometTaskRegistry == null)
            this.cometTaskRegistry = new ItsNatNormalCometTaskRegistryImpl(this); // para ahorrar memoria si no se usa
        return cometTaskRegistry;
    }

    public void addCometTask(NormalCometNotifierImpl notifier)
    {
        getCometTaskRegistry().addCometTask(notifier);
    }

    public CometTaskEventListenerWrapper removeCometTask(String id)
    {
        return getCometTaskRegistry().removeCometTask(id);
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
    @Override
    protected void setInvalidInternal()
    {
        super.setInvalidInternal();

        if (hasCometNotifiers())
        {
            // Liberamos así los hilos bloqueados etc
            Set<NormalCometNotifierImpl> notifiers = getCometNotifierSet();
            NormalCometNotifierImpl[] array = notifiers.toArray(new NormalCometNotifierImpl[notifiers.size()]);
            for(int i = 0; i < array.length; i++)
            {
                NormalCometNotifierImpl notifier = array[i];
                notifier.stop(); // Se quita solo del set
            }
            notifiers.clear(); // por si acaso
        }

        if (hasItsNatTimerEventListeners())
        {
            // Evitamos así que un timer pendiente nos provoque un error en el cliente
            ItsNatTimerEventListenerRegistryImpl registry = getItsNatTimerEventListenerRegistry();
            registry.removeAllItsNatDOMEventListeners(true);
        }

        if (hasHTMLIFrameFileUploads())
        {
            // Estos objetos están también registrados en otras colecciones,
            // no tienen sentido con un cliente invalidado.
            MapUniqueId<HTMLIFrameFileUploadImpl> map = getHTMLIFrameFileUploadMap();
            HTMLIFrameFileUploadImpl[] array = map.toArray(new HTMLIFrameFileUploadImpl[map.size()]);
            for(int i = 0; i < array.length; i++)
            {
                HTMLIFrameFileUploadImpl fileUp = array[i];
                fileUp.dispose(); // Se quita solo de las listas
            }
            map.clear(); // Por si acaso
        }
    }

    public void startEventDispatcherThread(final Runnable task)
    {
        // El hilo que llama este método debe ser un hilo asociado al request/response
        final ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        if (!Thread.holdsLock(itsNatDoc) || (itsNatDoc.getCurrentItsNatServletRequest() == null))
            throw new ItsNatException("Caller thread must be a normal browser-request thread",this);
        if (task instanceof Thread)
            throw new ItsNatException("Runnable object must not be a Thread",task);

        final Thread thread = new Thread()
        {
            public void run()
            {
                itsNatDoc.setEventDispatcherClientDocByThread(ClientDocumentStfulImpl.this);

                try
                {
                    // Al ser un hilo diferente el document no está sincronizado (no debe estarlo, aunque dentro del hilo puntualmente necesitará sincronizar para acceder al mismo)
                    task.run(); // Normalmente hará llamadas a EventTarget.dispatchEvent
                }
                finally
                {
                    itsNatDoc.setEventDispatcherClientDocByThread(null);
                    itsNatDoc.unlockThreads(); // la última posible llamada a EventTarget.dispatchEvent deja bloqueado el último hilo request/response
                }
            }
        };

        thread.start(); // No importa que empiece el hilo antes de que se pare el hilo actual pues cualquier llamada a EventTarget.dispatchEvent necesita bloquear el documento, el cual está ya bloqueado por este hilo request/response, y hasta que no termine este request/response y libere el documento deberá esperar

        long evtDispMaxWait = itsNatDoc.getEventDispatcherMaxWait();
        itsNatDoc.lockThread(evtDispMaxWait);
    }

    public boolean dispatchEvent(EventTarget target,Event evt) throws EventException
    {
        return dispatchEvent(target,evt,getCommMode(),getEventTimeout());
    }

    public boolean dispatchEvent(EventTarget target,Event evt,int commMode,long eventTimeout) throws EventException
    {
        final ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        if (Thread.holdsLock(itsNatDoc))
            throw new ItsNatException("Document must be unlocked in this call",this);
        if (this != itsNatDoc.getEventDispatcherClientDocByThread())
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
            addContinueEventListener(null,listener,commMode,new ParamTransport[]{param},null,eventTimeout);

            itsNatDoc.notifyAll();  // Desbloquea el hilo del request/response para que se envíe el código al browser
        }

        synchronized(monitor)
        {
            // Bloqueamos el hilo dispatcher de eventos esperando la respuesta del navegador
            try { monitor.wait(evtDispMaxWait); } catch(InterruptedException ex) { throw new ItsNatException(ex,this); }
        }

        return monitor[0];
    }

    public boolean hasHTMLIFrameFileUploads()
    {
        if (fileUploadsMap == null) return false;
        return !fileUploadsMap.isEmpty();
    }
    
    public MapUniqueId<HTMLIFrameFileUploadImpl> getHTMLIFrameFileUploadMap()
    {
        if (fileUploadsMap == null)
            this.fileUploadsMap = new MapUniqueId<HTMLIFrameFileUploadImpl>(getUniqueIdGenerator()); // Así ahorramos memoria si no se usa
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

    public ScriptUtil getScriptUtil()
    {
        return getJSScriptUtilFromClientImpl();
    }
    
    public JSScriptUtilFromClientImpl getJSScriptUtilFromClientImpl()
    {
        if (jsScriptUtil == null)
            this.jsScriptUtil = new JSScriptUtilFromClientImpl(this);
        return jsScriptUtil;
    }
}
