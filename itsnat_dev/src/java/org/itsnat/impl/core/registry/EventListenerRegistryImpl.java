/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.registry;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.scriptren.bsren.listener.BSRenderItsNatEventListenerImpl;
import org.itsnat.impl.core.scriptren.jsren.listener.JSRenderItsNatEventListenerImpl;


/**
 *
 * @author jmarranz
 */
public abstract class EventListenerRegistryImpl
{
    
    public static void addItsNatEventListenerCode(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateImpl clientDoc)
    {
        if (clientDoc != null)
        {
            // Aunque el listener sea de todos los clientes sólo deseamos renderizar para un cliente concreto
            addItsNatEventListenerCode2(itsNatListener,clientDoc);
        }
        else
        {
            // Para todos los clientes
            ItsNatStfulDocumentImpl itsNatDoc = itsNatListener.getItsNatStfulDocument();
            if (!itsNatDoc.hasClientDocumentAttachedClient())
            {
                clientDoc = itsNatDoc.getClientDocumentStfulOwner().getClientDocumentStfulDelegate();

                addItsNatEventListenerCode2(itsNatListener,clientDoc);
            }
            else
            {
                ClientDocumentStfulImpl[] clients = itsNatDoc.getAllClientDocumentStfulsCopy();
                for(int i = 0; i < clients.length; i++)
                {
                    ClientDocumentStfulImpl clientDocParent = clients[i];
                    clientDoc = clientDocParent.getClientDocumentStfulDelegate();
                    
                    addItsNatEventListenerCode2(itsNatListener,clientDoc);
                }
            }
        }
    }
    
    private static void addItsNatEventListenerCode2(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateImpl clientDoc)
    {    
        String code = null;
        if (clientDoc instanceof ClientDocumentStfulDelegateWebImpl)
        {
            JSRenderItsNatEventListenerImpl render = JSRenderItsNatEventListenerImpl.getJSRenderItsNatEventListener(itsNatListener,(ClientDocumentStfulDelegateWebImpl)clientDoc);
            code = render.addItsNatEventListenerCodeClient(itsNatListener,(ClientDocumentStfulDelegateWebImpl)clientDoc);
        }
        else if (clientDoc instanceof ClientDocumentStfulDelegateDroidImpl)
        {
            BSRenderItsNatEventListenerImpl render = BSRenderItsNatEventListenerImpl.getBSRenderItsNatEventListener(itsNatListener,(ClientDocumentStfulDelegateDroidImpl)clientDoc);
            code = render.addItsNatEventListenerCodeClient(itsNatListener,(ClientDocumentStfulDelegateDroidImpl)clientDoc);                
        }    
        
        if (code != null) clientDoc.addCodeToSend(code);
    }
    
    public static void removeItsNatEventListenerCode(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateImpl clientDoc)
    {
        if (clientDoc != null)
        {
            // Aunque el listener sea de todos los clientes sólo deseamos renderizar para un cliente concreto
            removeItsNatEventListenerCode2(itsNatListener,clientDoc);
        }
        else
        {
            // Para todos
            ItsNatStfulDocumentImpl itsNatDoc = itsNatListener.getItsNatStfulDocument();
            if (!itsNatDoc.hasClientDocumentAttachedClient())
            {
                clientDoc = itsNatDoc.getClientDocumentStfulOwner().getClientDocumentStfulDelegate();                

                removeItsNatEventListenerCode2(itsNatListener,clientDoc);
            }
            else
            {
                ClientDocumentStfulImpl[] clients = itsNatDoc.getAllClientDocumentStfulsCopy();
                for(int i = 0; i < clients.length; i++)
                {
                    ClientDocumentStfulImpl clientDocParent = clients[i];                    
                    clientDoc = clientDocParent.getClientDocumentStfulDelegate();

                    removeItsNatEventListenerCode2(itsNatListener,clientDoc);
                }
            }
        }
    }        
    
    private static void removeItsNatEventListenerCode2(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateImpl clientDoc)
    {    
        String code = null;        
        if (clientDoc instanceof ClientDocumentStfulDelegateWebImpl)
        {
            JSRenderItsNatEventListenerImpl render = JSRenderItsNatEventListenerImpl.getJSRenderItsNatEventListener(itsNatListener,(ClientDocumentStfulDelegateWebImpl)clientDoc);
            code = render.removeItsNatEventListenerCodeClient(itsNatListener,(ClientDocumentStfulDelegateWebImpl)clientDoc);
        }
        else if (clientDoc instanceof ClientDocumentStfulDelegateDroidImpl)
        {
            BSRenderItsNatEventListenerImpl render = BSRenderItsNatEventListenerImpl.getBSRenderItsNatEventListener(itsNatListener,(ClientDocumentStfulDelegateDroidImpl)clientDoc);
            code = render.removeItsNatEventListenerCodeClient(itsNatListener,(ClientDocumentStfulDelegateDroidImpl)clientDoc);                
        }   
        
        if (code != null) clientDoc.addCodeToSend(code);        
    }    
}
