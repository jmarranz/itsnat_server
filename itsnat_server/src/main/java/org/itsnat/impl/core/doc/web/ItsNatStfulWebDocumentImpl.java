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

package org.itsnat.impl.core.doc.web;

import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.registry.dom.domstd.ItsNatDOMStdEventListenerRegistryImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateVersionImpl;
import org.w3c.dom.Document;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatStfulWebDocumentImpl extends ItsNatStfulDocumentImpl
{
    protected ItsNatDOMStdEventListenerRegistryImpl domStdListenerRegistry;
    
    public ItsNatStfulWebDocumentImpl(Document doc, ItsNatDocumentTemplateVersionImpl docTemplate, Browser browser, String requestURL, ItsNatSessionImpl ownerSession, boolean stateless)
    {
        super(doc, docTemplate, browser, requestURL, ownerSession, stateless);
    }
    


    public ItsNatDOMStdEventListenerWrapperImpl getDOMStdEventListenerById(String listenerId)
    {
        if (!hasDOMStdEventListeners()) return null;

        return getDOMStdEventListenerRegistry().getItsNatDOMStdEventListenerById(listenerId);
    }    
    
    @Override
    public void addPlatformEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        addDOMStdEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }        
    
    public void addDOMStdEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        getDOMStdEventListenerRegistry().addItsNatDOMStdEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }    
    
    @Override
    public void removePlatformEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        removeDOMStdEventListener(target,type,listener,useCapture,updateClient);
    }        
    
    public void removeDOMStdEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        getDOMStdEventListenerRegistry().removeItsNatDOMStdEventListener(target,type,listener,useCapture,updateClient);
    }    
    
    @Override
    public void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture,int commMode,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        getDOMStdEventListenerRegistry().addMutationEventListener(target,listener,useCapture,commMode,preSendCode,eventTimeout,bindToCustomFunc);
    }
    
    @Override
    public void addMutationEventListener(EventTarget nodeTarget,EventListener mutationListener,boolean useCapture)
    {
        getDOMStdEventListenerRegistry().addMutationEventListener(nodeTarget,mutationListener,useCapture,getCommMode(),getEventTimeout());
    }

    @Override
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
            this.domStdListenerRegistry = new ItsNatDOMStdEventListenerRegistryImpl(this,null);
        return domStdListenerRegistry;
    }
    
    @Override
    public void renderPlatformEventListeners(ClientDocumentAttachedClientImpl clientDoc)    
    {
        if (hasDOMStdEventListeners())
            getDOMStdEventListenerRegistry().renderItsNatNormalEventListeners(clientDoc);        
    }
    
    @Override
    public int removeAllPlatformEventListeners(EventTarget target,boolean updateClient)
    {
        if (!hasDOMStdEventListeners()) return 0;

        return getDOMStdEventListenerRegistry().removeAllItsNatDOMStdEventListeners(target,updateClient);
    }    

}
