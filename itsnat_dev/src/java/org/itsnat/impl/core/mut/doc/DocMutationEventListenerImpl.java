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

package org.itsnat.impl.core.mut.doc;

import java.io.Serializable;
import java.util.LinkedList;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.listener.*;
import org.itsnat.impl.core.util.MapListImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public abstract class DocMutationEventListenerImpl implements EventListener,Serializable
{
    protected ItsNatDocumentImpl itsNatDoc;
    protected MapListImpl beforeAfterListeners;
    protected AutoBuildCompBeforeAfterMutationRenderListener autoBuildCompBeforeAfterListener;
    protected boolean enabled = true;

    /**
     * Creates a new instance of DocMutationEventListenerImpl
     */
    public DocMutationEventListenerImpl(ItsNatDocumentImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public AutoBuildCompBeforeAfterMutationRenderListener getAutoBuildCompBeforeAfterMutationRenderListener()
    {
        return autoBuildCompBeforeAfterListener;
    }

    public void setAutoBuildCompBeforeAfterMutationRenderListener(AutoBuildCompBeforeAfterMutationRenderListener listener)
    {
        this.autoBuildCompBeforeAfterListener = listener;
    }

    protected MapListImpl getBeforeAfterMutationRenderListeners()
    {
        if (beforeAfterListeners == null)
            this.beforeAfterListeners = new MapListImpl();
        return beforeAfterListeners;
    }

    protected LinkedList getBeforeAfterMutationRenderListeners(Node node)
    {
        if (beforeAfterListeners == null)
            return null;
        return beforeAfterListeners.get(node);
    }

    public void addBeforeAfterMutationRenderListener(Node node,BeforeAfterMutationRenderListener listener)
    {
        MapListImpl listeners = getBeforeAfterMutationRenderListeners();
        listeners.add(node,listener);
    }

    public void removeBeforeAfterMutationRenderListener(Node node,BeforeAfterMutationRenderListener listener)
    {
        MapListImpl listeners = getBeforeAfterMutationRenderListeners();
        listeners.remove(node,listener);
    }

    public void handleEvent(Event evt)
    {
        // No hay problemas con los hilos, este método es accedido por un sólo hilo para el documento dado
        // El mutation event es generado en este caso por el DOM de Batik, no es nuestra implementación
        if (!isEnabled()) return;

        MutationEvent mutEvent = (MutationEvent)evt;

        checkOperation(mutEvent);

        handleMutationEvent(mutEvent);
    }

    protected void handleMutationEvent(MutationEvent mutEvent)
    {
        ClientDocumentImpl[] allClients = getAllClientDocumentsCopy();

        beforeAfterRenderAndSendMutationCode(true,mutEvent,allClients);

        renderAndSendMutationCode(mutEvent,allClients);

        beforeAfterRenderAndSendMutationCode(false,mutEvent,allClients);
    }

    protected abstract void checkOperation(MutationEvent mutEvent);

    protected void beforeAfterRenderAndSendMutationCode(boolean before,MutationEvent mutEvent,ClientDocumentImpl[] allClients)
    {
        Node target = (Node)mutEvent.getTarget(); // El target es siempre el nodo que se inserta/elimina/el padre del atributo cambiado/el nodo texto-comentario que cambia.

        // El childrenFirst busca imitar el orden en que son creados/eliminados los nodos
        // tal que sea de la misma forma que hace el render.
        boolean childrenFirst;
        String type = mutEvent.getType();
        if (type.equals("DOMNodeInserted"))
            childrenFirst = false; // Puede ser importante por ejemplo en autobuild de componentes en before=false pues primero se construye el padre y luego los hijos
        else if (type.equals("DOMNodeRemoved"))
            childrenFirst = true; // Puede ser importante por ejemplo en autobuild de componentes en before=true pues primero se desregistran los hijos y luego el padre
        else
            childrenFirst = false; // Indiferente, es el caso de cambio de atributo o cambio de texto en text nodes etc
        beforeAfterRenderAndSendMutationCodeProcessTree(before,childrenFirst,target,mutEvent,allClients);
    }

    protected void beforeAfterRenderAndSendMutationCodeProcessTree(boolean before,boolean childrenFirst,Node node,MutationEvent mutEvent,ClientDocumentImpl[] allClients)
    {
        if (childrenFirst)
        {
            Node child = node.getFirstChild();
            while(child != null)
            {
                beforeAfterRenderAndSendMutationCodeProcessTree(before,childrenFirst,child,mutEvent,allClients);
                child = child.getNextSibling();
            }
        }

        beforeAfterRenderAndSendMutationCode(before,node,mutEvent,allClients);

        if (!childrenFirst)
        {
            Node child = node.getFirstChild();
            while(child != null)
            {
                beforeAfterRenderAndSendMutationCodeProcessTree(before,childrenFirst,child,mutEvent,allClients);
                child = child.getNextSibling();
            }
        }
    }

    protected void beforeAfterRenderAndSendMutationCode(boolean before,Node node,MutationEvent mutEvent,ClientDocumentImpl[] allClients)
    {
        if (autoBuildCompBeforeAfterListener != null)
        {
            if (before) autoBuildCompBeforeAfterListener.beforeRender(node,mutEvent);
            else autoBuildCompBeforeAfterListener.afterRender(node,mutEvent);
        }

        LinkedList beforeAfterListeners = getBeforeAfterMutationRenderListeners(node);
        if ((beforeAfterListeners != null) && !beforeAfterListeners.isEmpty())
        {
            // De esta manera permitimos añadir y eliminar listeners de forma concurrente
            BeforeAfterMutationRenderListener[] listenerArray = (BeforeAfterMutationRenderListener[])beforeAfterListeners.toArray(new BeforeAfterMutationRenderListener[beforeAfterListeners.size()]);
            for(int i = 0; i < listenerArray.length; i++)
            {
                BeforeAfterMutationRenderListener listener = (BeforeAfterMutationRenderListener)listenerArray[i];
                if (before) listener.beforeRender(node,mutEvent);
                else listener.afterRender(node, mutEvent);
            }
        }
    }

    protected abstract void renderAndSendMutationCode(MutationEvent mutEvent,ClientDocumentImpl[] allClients);

    protected abstract ClientDocumentImpl[] getAllClientDocumentsCopy();
}
