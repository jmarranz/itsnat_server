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

package org.itsnat.impl.core.event.client.dom.domstd.w3c;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.event.client.dom.domstd.NodeContainerImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public abstract class W3CMutationEventImpl extends W3CEventImpl implements MutationEvent
{
    protected NodeContainerImpl relatedNode;
    protected NodeContainerImpl refChild;
    protected EventTarget targetNodeInserted;

    /**
     * Creates a new instance of W3CMutationEventImpl
     */
    public W3CMutationEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public void initMutationEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, Node relatedNodeArg, String prevValueArg, String newValueArg, String attrNameArg, short attrChangeArg)
    {
        throw new ItsNatException("Not implemented",this);
    }

    public boolean isCacheIfPossibleTarget()
    {
        // cacheIfPossible es false porque podemos estar cacheando
        // en el servidor (con la respectiva orden enviada al cliente)
        // el nodo que ha sido eliminado en el cliente
        // por ejemplo en DOMNodeRemoved
        return false;
    }

    public void resolveNodePaths()
    {
        super.resolveNodePaths();
        
        getRelatedNode();
        getRefChild();
    }

    public Node getRelatedNode()
    {
        // Notar que cacheIfPossible es true porque relatedNode
        // es el nodo padre del nodo objetivo (insertando, quitando etc)
        // o bien el atributo (Attr) el cual no se cachea.
        if (relatedNode == null)
        {
            if (getType().equals("DOMAttrModified"))
            {
                short attrChange = getAttrChange();
                switch(attrChange)
                {
                    case MutationEvent.ADDITION:
                        // El atributo se ha añadido en el cliente pero no está
                        // en el servidor.
                        this.relatedNode = new NodeContainerImpl(null);
                        break;
                    case MutationEvent.MODIFICATION:
                    case MutationEvent.REMOVAL:
                        // En el caso de modificación obviamente el atributo está también
                        // en el servidor.
                        // En el caso de remoción el evento se procesa antes de que se elimine
                        // efectivamente el atributo pero eso sólo nos preocuparía en el cliente,
                        // en el servidor el evento pudo enviarse después asíncronamente y por tanto
                        // después de la eliminación, pero nos da igual pues el atributo no ha sido
                        // eliminado en el servidor.                        
                        String attrName = getAttrName();
                        Element elem = (Element)getTarget();
                        Attr attr = elem.getAttributeNode(attrName); // NO debería ser nulo.
                        this.relatedNode = new NodeContainerImpl(attr);
                        break;
                }
            }
            else this.relatedNode = new NodeContainerImpl(getParameterNode("relatedNode",true));
        }

        return relatedNode.get();
    }

    public void setRelatedNodeAddedAttr(Attr attr)
    {
        // Así, en el caso de sincronización automática del servidor
        // respecto al cliente, completamos en el evento lo que nos falta,
        // el nuevo atributo en el servidor.
        this.relatedNode = new NodeContainerImpl(attr);
    }
    
    public String getPrevValue()
    {
        return getParameter("prevValue");
    }

    public String getNewValue()
    {
        return getParameter("newValue");
    }

    public String getAttrName()
    {
        return getParameter("attrName");
    }

    public short getAttrChange()
    {
        return getParameterShort("attrChange");
    }

    public Node getRefChild()
    {
        // NO es estándar esta propiedad, sólo se necesita en el evento
        // con type DOMNodeInserted
        if (!getType().equals("DOMNodeInserted"))
            return null;

        // Aquí no es necesario que cacheIfPossible sea false porque este
        // es un evento de inserción.
        if (refChild == null)
            this.refChild = new NodeContainerImpl(getParameterNode("refChild",true));
        return refChild.get();
    }

    public void setTargetNodeInserted(EventTarget target)
    {
        this.targetNodeInserted = target;
    }

    public EventTarget getTarget()
    {
        String type = getType();
        if (type.equals("DOMNodeInserted"))
            return targetNodeInserted;
        else
            return super.getTarget();
    }
}
