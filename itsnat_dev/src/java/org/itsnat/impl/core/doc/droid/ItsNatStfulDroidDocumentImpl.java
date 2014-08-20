/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.doc.droid;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.droid.ItsNatStfulDroidDocComponentManagerImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DefaultElementGroupManagerImpl;
import org.itsnat.impl.core.domutil.ElementGroupManagerImpl;
import org.itsnat.impl.core.listener.droid.ItsNatDroidEventListenerWrapperImpl;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerImpl;
import org.itsnat.impl.core.mut.doc.droid.DocMutationEventListenerStfulDroidImpl;
import org.itsnat.impl.core.registry.droid.ItsNatDroidEventListenerRegistryImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.droid.ItsNatStfulDroidDocumentTemplateVersionImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDroidDocumentImpl extends ItsNatStfulDocumentImpl
{
    protected ItsNatDroidEventListenerRegistryImpl droidEvtListenerRegistry;
    
    public ItsNatStfulDroidDocumentImpl(Document doc, ItsNatDocumentTemplateVersionImpl docTemplate, Browser browser, String requestURL, ItsNatSessionImpl ownerSession, boolean stateless)
    {
        super(doc, docTemplate, browser, requestURL, ownerSession, stateless);
    }

    @Override
    public Element getVisualRootElement()
    {
        return getDocument().getDocumentElement();
    }

    @Override
    public boolean isNewNodeDirectChildOfContentRoot(Node newNode)
    {
        Node parentNode = newNode.getParentNode();
        if (parentNode == null) return false; // No ocurre nunca pero por si acaso
        Document doc = getDocument();
        return (parentNode.getParentNode() == doc.getDocumentElement());
    }

    @Override
    public DocMutationEventListenerImpl createInternalMutationEventListener()
    {
        return new DocMutationEventListenerStfulDroidImpl(this);
    }

    @Override
    public ElementGroupManagerImpl createElementGroupManager()
    {
        return new DefaultElementGroupManagerImpl(this);
    }

    @Override
    public ItsNatDocComponentManagerImpl createItsNatComponentManager()
    {
        return new ItsNatStfulDroidDocComponentManagerImpl(this);
    }
    
    public int removeAllPlatformEventListeners(EventTarget target,boolean updateClient)
    {
        if (!hasDroidEventListeners()) return 0;

        return getDroidEventListenerRegistry().removeAllItsNatDroidEventListeners(target,updateClient);
    }

    public ItsNatDroidEventListenerWrapperImpl getDroidEventListenerById(String listenerId)
    {
        if (!hasDroidEventListeners()) return null;

        return getDroidEventListenerRegistry().getItsNatDroidEventListenerById(listenerId);
    }    
    
    public void addPlatformEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        addDroidEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }        
    
    public void addDroidEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        getDroidEventListenerRegistry().addItsNatDroidEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }    
    
    public void removePlatformEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        removeDroidEventListener(target,type,listener,useCapture, updateClient);
    }
    
    public void removeDroidEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        getDroidEventListenerRegistry().removeItsNatDroidEventListener(target,type,listener,useCapture,updateClient);
    }    
    
    public void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture,int commMode,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        throw new ItsNatException("Mutation events are not supported in Droid ItsNat documents");
    }
    
    public void addMutationEventListener(EventTarget nodeTarget,EventListener mutationListener,boolean useCapture)
    {
        throw new ItsNatException("Mutation events are not supported in Droid ItsNat documents");
    }

    public void removeMutationEventListener(EventTarget target,EventListener listener,boolean useCapture)
    {
        throw new ItsNatException("Mutation events are not supported in Droid ItsNat documents");
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
            this.droidEvtListenerRegistry = new ItsNatDroidEventListenerRegistryImpl(this,null);
        return droidEvtListenerRegistry;
    }
    
    public void renderPlatformEventListeners(ClientDocumentAttachedClientImpl clientDoc)    
    {
        if (hasDroidEventListeners())
            getDroidEventListenerRegistry().renderItsNatNormalEventListeners(clientDoc);        
    }    

    public ItsNatStfulDroidDocumentTemplateVersionImpl getItsNatStfulDroidDocumentTemplateVersion()
    {
        return (ItsNatStfulDroidDocumentTemplateVersionImpl)getItsNatDocumentTemplateVersion();
    }
}
