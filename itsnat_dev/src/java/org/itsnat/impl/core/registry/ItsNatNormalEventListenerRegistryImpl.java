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

package org.itsnat.impl.core.registry;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.clientdoc.web.SVGWebInfoImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.EventListenerInternal;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.util.MapUniqueId;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatNormalEventListenerRegistryImpl extends EventListenerRegistryImpl implements Serializable
{
    protected ItsNatStfulDocumentImpl itsNatDoc;
    protected ClientDocumentStfulImpl clientDocTarget; // Si es null es que el registro es a nivel de documento y pertenece a todos los clientes
    protected int capturingCount;
    protected MapUniqueId<ItsNatNormalEventListenerWrapperImpl> eventListenersById; // No es weak porque necesitamos sujetar el listener wrapper pues es un objeto de uso interno para este fin

    /**
     * Creates a new instance of ItsNatNormalEventListenerRegistryImpl
     */
    public ItsNatNormalEventListenerRegistryImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc)
    {
        this.itsNatDoc = itsNatDoc;
        this.clientDocTarget = clientDoc; // puede ser null

        this.eventListenersById = new MapUniqueId<ItsNatNormalEventListenerWrapperImpl>(itsNatDoc.getUniqueIdGenerator());
    }

    public ClientDocumentStfulImpl getClientDocumentStfulTarget()
    {
        return clientDocTarget; // puede ser null
    }
    
    public ClientDocumentStfulDelegateImpl getClientDocumentStfulDelegateFromDocument()
    {
        return ((ClientDocumentStfulImpl)itsNatDoc.getClientDocumentOwnerImpl()).getClientDocumentStfulDelegate();
    }    
    
    public ClientDocumentStfulDelegateImpl getClientDocumentStfulDelegateTarget()
    {    
        // Puede ser null si clientDocTarget es null
        return clientDocTarget != null ? clientDocTarget.getClientDocumentStfulDelegate() : null;
    }
    
    public boolean isEmpty()
    {
        return eventListenersById.isEmpty();
    }

    public void checkValidEventTarget(EventTarget target)
    {
        isValidEventTarget(target,true);
    }

    public boolean isValidEventTarget(EventTarget target,boolean throwErr)
    {
        // MSIE no admite asociar eventos DOM a text nodes y FireFox lo permite pero no los procesa por ej. los clicks
        // En los comentarios sí se permite aunque es absurdo (no pueden ser pulsados etc) delegamos en el programador
        if (target == null) return true; // Derivar si no se permite
        Node node = (Node)target; // nuestro AbstractView implementa Node
        int type = node.getNodeType();
        if (type == Node.TEXT_NODE)
            if (throwErr) throw new ItsNatException("Text node is not allowed",target);
            else return false;
        return true;
    }

    public int getCapturingCount()
    {
        return capturingCount;
    }

    public boolean canAddItsNatNormalEventListener(EventTarget target,EventListener listener)
    {
        if (!ItsNatNormalEventListenerWrapperImpl.canAddItsNatNormalEventListenerWrapper(listener,itsNatDoc, clientDocTarget))
            return false;
        checkValidEventTarget(target); // Lanza excepción si no es válido
        return true;
    }

    protected void addItsNatNormalEventListener(ItsNatNormalEventListenerWrapperImpl listenerWrapper)
    {
        eventListenersById.put(listenerWrapper);

        addItsNatEventListenerCode(listenerWrapper,getClientDocumentStfulDelegateTarget());
                
        if (listenerWrapper.getUseCapture())
            capturingCount++;
    }

    public void removeItsNatNormalEventListener(ItsNatNormalEventListenerWrapperImpl listener,boolean updateClient,boolean expunged)
    {
        // expunged tiene sentido en clases derivadas
        ItsNatNormalEventListenerWrapperImpl listenerRes = removeItsNatNormalEventListenerByIdPrivate(listener.getId(),updateClient);
        if (listenerRes == null)
            return; // Ya se eliminó seguramente
        if (listenerRes != listener)
            throw new ItsNatException("INTERNAL ERROR");
    }

    private ItsNatNormalEventListenerWrapperImpl removeItsNatNormalEventListenerByIdPrivate(String id,boolean updateClient)
    {
        ItsNatNormalEventListenerWrapperImpl listenerWrapper = eventListenersById.removeById(id);
        if (listenerWrapper == null)
            return null; // Ya se eliminó o nunca se añadió (raro)

        if (updateClient)
        {
            removeItsNatEventListenerCode(listenerWrapper,getClientDocumentStfulDelegateTarget());           
        }
        
        if (listenerWrapper.getUseCapture())
            capturingCount--;

        return listenerWrapper;
    }
   
    
    protected ItsNatNormalEventListenerWrapperImpl removeItsNatNormalEventListenerById(String id,boolean updateClient)
    {
        return removeItsNatNormalEventListenerByIdPrivate(id,updateClient);
    }

    public ItsNatNormalEventListenerWrapperImpl getItsNatNormalEventListenerById(String listenerId)
    {
        return eventListenersById.get(listenerId);
    }

    /**
     * Devolvemos un array y no un iterador o una colección interna porque al procesar los
     * handlers es posible que se añadan o se quiten listenerList (el iterador fallaría)
     */
    public abstract EventListener[] getEventListenersArrayCopy(EventTarget target,String type,boolean useCapture);

    public void renderItsNatNormalEventListeners(final ClientDocumentAttachedClientImpl clientDoc)
    {
        // Usado para renderizar los listeners de documento para un cliente nuevo
        if ((clientDoc == null) || (getClientDocumentStfulTarget() != null))
            throw new ItsNatException("INTERNAL ERROR");

        if (eventListenersById.isEmpty()) return;

        LinkedList<ItsNatNormalEventListenerWrapperImpl> svgWebNodes = null;
        for(Map.Entry<String,ItsNatNormalEventListenerWrapperImpl> entry : eventListenersById.entrySet())
        {
            ItsNatNormalEventListenerWrapperImpl listenerWrapper = entry.getValue();

            if (!clientDoc.canReceiveNormalEvents(listenerWrapper))
                continue;

            ClientDocumentStfulDelegateImpl clientDocDeleg = clientDoc.getClientDocumentStfulDelegate();
            
            if (clientDocDeleg instanceof ClientDocumentStfulDelegateWebImpl)
            {
                EventTarget currTarget = listenerWrapper.getCurrentTarget();
                if (SVGWebInfoImpl.isSVGNodeProcessedBySVGWebFlash((Node)currTarget,(ClientDocumentStfulDelegateWebImpl)clientDocDeleg))
                {
                    // Si el nodo es procesado por SVGWeb Flash no podemos enviar ahora el registro del listener
                    // al cliente pues el nodo SVG no existe hasta que se renderize lo cual
                    // sabemos que ha ocurrido tras el evento SVGLoad de la página.
                    if (svgWebNodes == null) svgWebNodes = new LinkedList<ItsNatNormalEventListenerWrapperImpl>();
                    svgWebNodes.add(listenerWrapper);
                    continue;
                }

                addItsNatEventListenerCode(listenerWrapper,clientDocDeleg);
            }
            else if (clientDocDeleg instanceof ClientDocumentStfulDelegateDroidImpl)
            {
                addItsNatEventListenerCode(listenerWrapper,clientDocDeleg);         
            }
        }

        if (svgWebNodes != null)
        {
            final LinkedList<ItsNatNormalEventListenerWrapperImpl> svgWebNodes2 = svgWebNodes;
            EventListener listener = new EventListenerInternal()
            {
                public void handleEvent(Event evt)
                {
                    for(ItsNatNormalEventListenerWrapperImpl listenerWrapper : svgWebNodes2)
                    {
                        if (!eventListenersById.containsKey(listenerWrapper)) 
                            continue; // Ha sido eliminado mientras se procesaba el evento SVGLoad

                        ClientDocumentStfulDelegateImpl clientDocDeleg = clientDoc.getClientDocumentStfulDelegate();                        
                        
                        addItsNatEventListenerCode(listenerWrapper,clientDocDeleg);                        
                    }
                }
            };
            AbstractView view = ((DocumentView)itsNatDoc.getDocument()).getDefaultView();
            clientDoc.addEventListener((EventTarget)view,"SVGLoad",listener,false);
        }
    }

    public void removeAllItsNatNormalEventListeners(boolean updateClient)
    {
        if (eventListenersById.isEmpty()) return;

        ItsNatNormalEventListenerWrapperImpl[] listenerList = eventListenersById.toArray(new ItsNatNormalEventListenerWrapperImpl[eventListenersById.size()]);
        
        for(int i = 0; i < listenerList.length; i++)
        {
            ItsNatNormalEventListenerWrapperImpl listener = listenerList[i];
            removeItsNatNormalEventListener(listener,updateClient,false);
        }
    }
}
