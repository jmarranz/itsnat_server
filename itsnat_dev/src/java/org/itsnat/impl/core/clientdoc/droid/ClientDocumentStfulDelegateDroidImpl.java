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

package org.itsnat.impl.core.clientdoc.droid;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.core.browser.droid.BrowserDroid;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.doc.droid.ItsNatStfulDroidDocumentImpl;
import org.itsnat.impl.core.dompath.NodeLocationWithParentImpl;
import org.itsnat.impl.core.listener.droid.ItsNatDroidEventListenerWrapperImpl;
import org.itsnat.impl.core.registry.droid.ItsNatDroidEventListenerRegistryImpl;
import org.itsnat.impl.core.scriptren.bsren.BSScriptUtilFromClientImpl;
import org.itsnat.impl.core.scriptren.bsren.dom.node.BSRenderNodeImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderNodeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ClientDocumentStfulDelegateDroidImpl extends ClientDocumentStfulDelegateImpl
{
    protected ItsNatDroidEventListenerRegistryImpl droidEvtListenerRegistry;
    
    public ClientDocumentStfulDelegateDroidImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
    }
    
    public BrowserDroid getBrowserDroid()
    {
        return (BrowserDroid)clientDoc.getBrowser();
    }    
    
    public ScriptUtil createScriptUtil()    
    {
        return new BSScriptUtilFromClientImpl(this);
    }

    protected String getCodeDispatchEvent(EventTarget target,Event evt,String varResName,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return BSRenderNodeImpl.getCodeDispatchEvent(target,evt,"res",this);
    }    

    @Override
    public String getNodeReference(Node node, boolean cacheIfPossible, boolean errIfNull)
    {
        return BSRenderNodeImpl.getNodeReference(node,cacheIfPossible,errIfNull,this);
    }
    
    @Override
    protected String renderAddNodeToCache(NodeLocationWithParentImpl nodeLoc)
    {
        return BSRenderNodeImpl.addNodeToCache(nodeLoc);
    }    
    
    @Override
    protected String renderRemoveNodeFromCache(String id)
    {
        return BSRenderNodeImpl.removeNodeFromCache(id);
    }    

    public boolean hasDroidEventListeners()
    {
        if (droidEvtListenerRegistry == null)
            return false;
        return !droidEvtListenerRegistry.isEmpty();
    }

    public ItsNatDroidEventListenerRegistryImpl getDroidEventListenerRegistry()
    {
        if (droidEvtListenerRegistry == null) // Evita instanciar si no se usa, caso de servir XML
            this.droidEvtListenerRegistry = new ItsNatDroidEventListenerRegistryImpl(getItsNatStfulDocument(),getClientDocumentStful());
        return droidEvtListenerRegistry;
    }       
    
    public void addPlatformEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        addDroidEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }
    
    public void addDroidEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        getDroidEventListenerRegistry().addItsNatDroidEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }     
    
    public void removeDroidEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        getDroidEventListenerRegistry().removeItsNatDroidEventListener(target,type,listener,useCapture,updateClient);
    }          

    @Override
    public void removePlatformEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, boolean updateClient)
    {
        removeDroidEventListener(target,type,listener,useCapture,updateClient);
    }

    public int removeAllPlatformEventListeners(EventTarget target,boolean updateClient)    
    {
        return removeAllDroidEventListeners(target,updateClient);
    }
    
    public int removeAllDroidEventListeners(EventTarget target,boolean updateClient)
    {
        if (!hasDroidEventListeners()) return 0;

        return getDroidEventListenerRegistry().removeAllItsNatDroidEventListeners(target,updateClient);
    }    
    
    public ItsNatDroidEventListenerWrapperImpl getDroidEventListenerById(String listenerId)
    {
        ItsNatDroidEventListenerWrapperImpl listener = null;

        if (hasDroidEventListeners())
            listener = getDroidEventListenerRegistry().getItsNatDroidEventListenerById(listenerId);

        if (listener == null)
            listener = ((ItsNatStfulDroidDocumentImpl)getItsNatStfulDocument()).getDroidEventListenerById(listenerId);

        return listener;
    }    

    
    @Override
    public void addMutationEventListener(EventTarget nodeTarget, EventListener mutationListener, boolean useCapture)
    {
        throw new ItsNatException("Mutation events are not supported in Droid ItsNat documents");
    }

    @Override
    public void addMutationEventListener(EventTarget target, EventListener listener, boolean useCapture, int commMode, String preSendCode, long eventTimeout, String bindToCustomFunc)
    {
        throw new ItsNatException("Mutation events are not supported in Droid ItsNat documents");
    }

    @Override
    public void removeMutationEventListener(EventTarget target, EventListener listener, boolean useCapture)
    {
        throw new ItsNatException("Mutation events are not supported in Droid ItsNat documents");
    }
}
