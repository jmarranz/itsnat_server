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
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.comp.iframe.HTMLIFrameFileUploadImpl;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.doc.web.ItsNatStfulWebDocumentImpl;
import org.itsnat.impl.core.scriptren.jsren.JSScriptUtilFromClientImpl;
import org.itsnat.impl.core.scriptren.jsren.node.JSRenderNodeImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.dompath.NodeLocationWithParentImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.registry.dom.domstd.ItsNatDOMStdEventListenerRegistryImpl;
import org.itsnat.impl.core.util.MapUniqueId;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
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
    protected ItsNatDOMStdEventListenerRegistryImpl domStdListenerRegistry;     
    
    public ClientDocumentStfulDelegateWebImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
        
    }
   
    public ItsNatStfulWebDocumentImpl getItsNatStfulWebDocument()
    {
        return (ItsNatStfulWebDocumentImpl)super.getItsNatStfulDocument();
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
    


    @Override
    protected String renderAddNodeToCache(NodeLocationWithParentImpl nodeLoc)
    {
        return JSRenderNodeImpl.addNodeToCache(nodeLoc);
    }
    
    @Override
    protected String renderRemoveNodeFromCache(String id)
    {
        return JSRenderNodeImpl.removeNodeFromCache(id);
    }

    protected String getCodeDispatchEvent(EventTarget target,Event evt,String varResName,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSRenderNodeImpl.getCodeDispatchEvent(target,evt,"res",this);
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
            this.domStdListenerRegistry = new ItsNatDOMStdEventListenerRegistryImpl(getItsNatStfulDocument(),getClientDocumentStful());
        return domStdListenerRegistry;
    }    
    
    public int removeAllPlatformEventListeners(EventTarget target,boolean updateClient)    
    {
        return removeAllDOMStdEventListeners(target,updateClient);
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
            listener = ((ItsNatStfulWebDocumentImpl)getItsNatStfulDocument()).getDOMStdEventListenerById(listenerId);

        return listener;
    }    
    
    public void addDOMStdEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        getDOMStdEventListenerRegistry().addItsNatDOMStdEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }    
    
    public void addMutationEventListener(EventTarget nodeTarget,EventListener mutationListener,boolean useCapture)
    {
        getDOMStdEventListenerRegistry().addMutationEventListener(nodeTarget,mutationListener,useCapture,getCommMode(),getEventTimeout());
    }

    public void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture,int commMode,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        getDOMStdEventListenerRegistry().addMutationEventListener(target,listener,useCapture,commMode,preSendCode,eventTimeout,bindToCustomFunc);
    }

    public void removeMutationEventListener(EventTarget target,EventListener listener,boolean useCapture)
    {
        getDOMStdEventListenerRegistry().removeMutationEventListener(target,listener,useCapture,true);
    }    
    
    public void removeDOMStdEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        getDOMStdEventListenerRegistry().removeItsNatDOMStdEventListener(target,type,listener,useCapture,updateClient);
    }        
    
    public void addPlatformEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        addDOMStdEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }

    @Override
    public void removePlatformEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, boolean updateClient)
    {
        removeDOMStdEventListener(target,type,listener,useCapture,updateClient);
    }
    
    
}
