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
