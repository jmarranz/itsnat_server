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

package org.itsnat.impl.core.domimpl.deleg;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.core.ItsNatUserDataImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domimpl.ItsNatNodeInternal;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public abstract class DelegateNodeImpl extends ItsNatUserDataImpl
{
    protected ItsNatNodeInternal node;
    protected Boolean internalMode;

    public DelegateNodeImpl(ItsNatNodeInternal node)
    {
        super(false); // En el caso del documento del template no usaremos los métodos de ItsNatUserData por lo que no hay problemas de hilos
        this.node = node;
    }

    public abstract ItsNatDocumentImpl getItsNatDocument();

    public abstract DelegateDocumentImpl getDelegateDocument();

    public abstract boolean isInternalMode();

    public void setInternalMode(boolean mode)
    {
        if (mode)
            this.internalMode = Boolean.TRUE;
        else
        {
            ItsNatDocumentImpl itsNatDoc = getItsNatDocument();
            // Este más bien es un error interno, pues el DOM del documento patrón del template (que no tiene ItsNatDocumentImpl)
            // a día de hoy no es expuesto al usuario.
            if (itsNatDoc == null) throw new DOMException(DOMException.INVALID_STATE_ERR,"Non-local event mode cannot be set in a non-remote DOM document");
            this.internalMode = Boolean.FALSE;
        }
    }

    public boolean isDispatchEventInternal(Event evt)
    {
        // Dependiendo del tipo de evento irá a un sitio u otro
        // independientemente del isInternalMode()
        return !(evt instanceof ItsNatEvent);
    }

    public boolean dispatchEventRemote(Event evt) throws EventException
    {
        // evt DEBE ser de tipo ItsNatEvent
        return getItsNatDocument().dispatchEvent(node,evt);
    }

    public boolean isAddRemoveEventListenerInternal()
    {
        return isInternalMode();
    }

    public void addEventListenerRemote(String type, EventListener listener, boolean useCapture)
    {
        ItsNatDocumentImpl itsNatDoc = getItsNatDocument();
        itsNatDoc.addEventListener(node,type,listener,useCapture);
    }

    public void removeEventListenerRemote(String type, EventListener listener, boolean useCapture)
    {
        ItsNatDocumentImpl itsNatDoc = getItsNatDocument();
        itsNatDoc.removeEventListener(node,type,listener,useCapture);
    }

    public void checkHasSenseDisconnectedFromClient()
    {
        ItsNatDocumentImpl itsNatDoc = getItsNatDocument();
        if (itsNatDoc == null) throw new ItsNatException("INTERNAL ERROR"); // No tiene sentido usar esto por ejemplo en el Document patrón de un template
    }

    public void checkHasSenseDisconnectedChildNodesFromClient()
    {
        checkHasSenseDisconnectedFromClient();
    }

    public static boolean isNodeBoundToDocumentTree(Node node)
    {
        if (node == null) return false;

        Node parent = node;
        do
        {
            node = parent;
            parent = node.getParentNode();
        }
        while(parent != null);

        return (node.getNodeType() == Node.DOCUMENT_NODE);
    }
    
    public abstract boolean isMutationEventInternal();
    public abstract void setMutationEventInternal(boolean value);
    public abstract boolean isDisconnectedFromClient();
    public abstract boolean isDisconnectedChildNodesFromClient();
    public abstract void setDisconnectedChildNodesFromClient(boolean disconnectedChildNodesFromClient);
}
