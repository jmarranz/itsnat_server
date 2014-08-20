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

import java.util.LinkedList;
import java.util.Map;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.doc.*;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.domimpl.ElementDocContainer;
import org.itsnat.impl.core.domimpl.ItsNatNodeInternal;
import org.itsnat.impl.core.domimpl.deleg.DelegateNodeImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.mut.client.ClientMutationEventListenerStfulImpl;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalLoadStfulDocImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public abstract class DocMutationEventListenerStfulImpl extends DocMutationEventListenerImpl
{

    public DocMutationEventListenerStfulImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return (ItsNatStfulDocumentImpl)itsNatDoc;
    }

    protected ClientDocumentImpl[] getAllClientDocumentsCopy()
    {
        return getItsNatStfulDocument().getAllClientDocumentsCopy();
    }

    protected void renderAndSendMutationCode(final MutationEvent mutEvent,ClientDocumentImpl[] allClients)
    {
        for(int i = 0; i < allClients.length; i++)
        {
            ClientDocumentStfulImpl clientDoc = (ClientDocumentStfulImpl)allClients[i];
            //if (!(clientDoc.getClientDocumentStfulDelegate() instanceof ClientDocumentStfulDelegateWebImpl)) continue;
            ClientDocumentStfulDelegateImpl clientDocDeleg = clientDoc.getClientDocumentStfulDelegate();
            ClientMutationEventListenerStfulImpl mutListener = clientDocDeleg.getClientMutationEventListenerStful();

            mutListener.beforeRenderAndSendMutationCode(mutEvent);
            if (mutListener.canRenderAndSendMutationCode()) // Si es false es que seguramente estamos en fase carga y fast load
            {
                // Cuidado: pre and post métodos sólo se llaman si se genera código de la mutación
                mutListener.preRenderAndSendMutationCode(mutEvent);
                mutListener.renderAndSendMutationCode(mutEvent);
                mutListener.postRenderAndSendMutationCode(mutEvent);
            }
            mutListener.afterRenderAndSendMutationCode(mutEvent);
        }
    }

    @Override
    protected void beforeAfterRenderAndSendMutationCode(boolean before,MutationEvent mutEvent,ClientDocumentImpl[] allClients)
    {
        super.beforeAfterRenderAndSendMutationCode(before, mutEvent, allClients);

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();

        String type = mutEvent.getType();

        if (before && itsNatDoc.isAutoCleanEventListeners() &&
            ( !itsNatDoc.isLoadingPhaseAndFastLoadMode() || itsNatDoc.isDebugMode()) &&
            type.equals("DOMNodeRemoved") )
        {
            // En carga y fast load en teoría no es necesario porque si es así es que estamos programando mal
            // pues si estamos eliminando un nodo que tiene un listener como no se genera código JavaScript
            // de las mutaciones significa que en el cliente cuando se vaya a cachear el nodo al crear el listener,
            // dicho nodo NO estará en el árbol. El añadir un listener supone normalmente cacheado, la simple inserción
            // no cachea. Por eso nos interesa hacerlo en este caso cuando tenemos el modo debug con la única
            // finalidad de detectar errores en tiempo de desarrollo no por otra rzón.

            Node target = (Node)mutEvent.getTarget(); // El target es siempre el nodo que se inserta/elimina/el padre del atributo cambiado/el nodo texto-comentario que cambia.
            processTreeCleanEventListeners(target,allClients);
        }

        if (!itsNatDoc.isLoadingPhaseAndFastLoadMode())
        {
            // En el caso de isLoading y FastLoadMode no es necesario
            // porque lo que interesa es el markup finalmente enviado al cliente
            // pues es el cliente el que va a cargar el iframe/object/embed/applet, los
            // estados intermedios del DOM no nos interesan. En este caso
            // el registro ya se hace al final, al serializar.

            // No bajamos este código a la clase derivada HTML porque supone que el documento
            // es HTML/XHTML, y podría ser que el <object>,<iframe> etc estuvieran en un fragmento
            // XHTML de un SVG por ejemplo.

            if (before)
            {
                if (type.equals("DOMNodeInserted"))
                {
                    Node insertedNode = (Node)mutEvent.getTarget();
                    processTreeInsertedElementDocContainer(insertedNode);
                }
                else if (type.equals("DOMAttrModified"))
                {
                    Attr attr = (Attr)mutEvent.getRelatedNode();
                    Element elem = (Element)mutEvent.getTarget();
                    ElementDocContainer elemDocCont = ElementDocContainerWrapperImpl.getElementDocContainerIfURLAttr(attr, elem);
                    if (elemDocCont != null)
                    {
                        BoundElementDocContainerImpl bindInfo = elemDocCont.getElementDocContainerWrapper().getBoundElementDocContainer();
                        if (bindInfo != null)
                        {
                            // Ya existe, tenemos que desregistrarlo pues cambia el URL
                            bindInfo.unRegister();
                        }

                        int changeType = mutEvent.getAttrChange();
                        switch(changeType)
                        {
                            case MutationEvent.ADDITION:
                            case MutationEvent.MODIFICATION:
                                bindInfo = BoundElementDocContainerImpl.register(elemDocCont, itsNatDoc);
                                // Si bindInfo es null es que el nuevo URL no cumple el formato esperado
                                break;
                            case MutationEvent.REMOVAL: // Nada que hacer, ya lo hemos desregistrado
                                break;
                            // No hay más casos
                        }
                    }
                }
            }
            else // after
            {
                if (type.equals("DOMNodeRemoved"))
                {
                    Node removeNode = (Node)mutEvent.getTarget();
                    processTreeRemovedElementDocContainer(removeNode);
                }
            }
        }
    }

    protected void processTreeCleanEventListeners(Node node,ClientDocumentImpl[] allClients)
    {
        // En el caso de listeners da igual si se procesa primero el padre
        // y luego los hijos o al revés, son únicamente "desregistros".
        cleanEventListeners(node,allClients);

        Node child = node.getFirstChild();
        while (child != null)
        {
            processTreeCleanEventListeners(child,allClients);
            child = child.getNextSibling();
        }
    }

    private void cleanEventListeners(Node node,ClientDocumentImpl[] allClients)
    {
        if (node.getNodeType() == Node.TEXT_NODE)
            return; // No es estrictamente necesario detectarlo aquí pero así nos ahorramos algunas llamadas

        EventTarget eventTarget = (EventTarget)node;

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        int count = 0;
        count += itsNatDoc.removeAllPlatformEventListeners(eventTarget,true);
        count += itsNatDoc.removeAllUserEventListeners(eventTarget,true);

        for(int i = 0; i < allClients.length; i++)
        {
            ClientDocumentStfulImpl clientDoc = (ClientDocumentStfulImpl)allClients[i];
            count += clientDoc.getClientDocumentStfulDelegate().removeAllPlatformEventListeners(eventTarget, true);
            count += clientDoc.removeAllUserEventListeners(eventTarget, true);
            count += clientDoc.removeAllTimerEventListeners(eventTarget, true);
            count += clientDoc.removeAllContinueEventListeners(eventTarget, true);
        }

        if ((count > 0) && itsNatDoc.isLoadingPhaseAndFastLoadMode())
            throw new ItsNatException("An event listener was registered in a node removed in the document load phase and in fast load mode",node);
    }

    protected void processTreeInsertedElementDocContainer(Node node)
    {
        if (node instanceof ElementDocContainer)
        {
            ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
            ElementDocContainer elem = (ElementDocContainer)node;
            BoundElementDocContainerImpl.register(elem,itsNatDoc);
        }

        Node child = node.getFirstChild();
        while (child != null)
        {
            processTreeInsertedElementDocContainer(child);
            child = child.getNextSibling();
        }
    }


    protected void processTreeRemovedElementDocContainer(Node node)
    {
        if (node instanceof ElementDocContainer)
        {
            ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
            ElementDocContainer elem = (ElementDocContainer)node;
            BoundElementDocContainerImpl.unRegister(elem,itsNatDoc);
        }

        Node child = node.getFirstChild();
        while (child != null)
        {
            processTreeRemovedElementDocContainer(child);
            child = child.getNextSibling();
        }
    }

    @Override
    protected void handleMutationEvent(MutationEvent mutEvent)
    {
        String type = mutEvent.getType();
        if (type.equals("DOMNodeInserted"))
        {
            // Hay que tener en cuenta que el nodo YA está insertado en el DOM servidor
            Element parentNode = (Element)mutEvent.getRelatedNode();
            Node newNode = (Node)mutEvent.getTarget();
            //Node parentNode = newNode.getParentNode();
            if (parentNode != null && // Yo creo que es imposible que sea nulo pero por si acaso
                parentNode.getFirstChild() == newNode &&
                parentNode.getLastChild() == newNode) // Es el primero y el último => el único hijo
            {
                // Es el primer nodo hijo, antes de hacer otra cosa reconectamos el nodo padre,
                // el que el usuario inserte un primer nodo dentro de un nodo desconectado muestra
                // la voluntad de que vuelva a reconectarse. Esto es más cómodo que llamar a reconnectChildNodesToClient
                // explícitamente por parte del usuario.
                // Si no estuviera desconectado no hace nada
                ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
                itsNatDoc.reconnectChildNodesToClient(parentNode);
            }
        }

        super.handleMutationEvent(mutEvent);
    }

    public boolean isDisconnectedChildNodesFromClient(Node node)
    {
        return ((ItsNatNodeInternal)node).getDelegateNode().isDisconnectedChildNodesFromClient();
    }

    public Node disconnectChildNodesFromClient(Node node)
    {
        DelegateNodeImpl delegNode = ((ItsNatNodeInternal)node).getDelegateNode();
        if (delegNode.isDisconnectedChildNodesFromClient())
            throw new ItsNatDOMException("This node is already disconnected from client",node);
            // Provocamos un error porque de otra manera tendríamos que devolver algo y un null haría
            // pensar al programador que el nodo no contenía nada y 
            // evitamos también un intento de desconectar los hijos de un nodo que a su vez ya está
            // desconectado a través de un nodo padre cuyos hijos se desconectaron, pues si un nodo
            // está desconectado sus hijos también lo serán, aunque este caso ya no se da nunca
            // pues los hijos al eliminarse ya no pertenecen al documento y el hecho de hacer la pregunta
            // isDisconnectedChildNodesFromClient() ya dará error.

        delegNode.setDisconnectedChildNodesFromClient(true);

        // Ahora eliminamos los nodos, de esta manera hacemos la limpieza
        // de caches, registros de listeners etc pero el código efectivo JavaScript de eliminación (y registro)
        // no se enviará al cliente pues ahora los hijos en el cliente están "desconectados"
        Node disconnectedFragment = DOMUtilInternal.extractChildren(node,true); // Es importante eliminar del último al primero para que el cálculo de paths de nodos no cacheados NO falle pues los nodos no son eliminados en el cliente
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        if (itsNatDoc.isLoadingPhaseAndFastLoadMode() && disconnectedFragment != null) // Fase load y modo fastLoad = true
        {
            // El fragmento removido lo guardamos para renderizarlo como markup cuando
            // se haga la serialización.
            ResponseNormalLoadStfulDocImpl loadResponse = getResponseNormalLoadStfulDoc();
            Map<Node,Object> disconnectedNodesFastLoadMode = loadResponse.getDisconnectedNodesFastLoadMode();
            // disconnectedFragment o es un DocumentFragment o es un nodo concreto, si es un
            // DocumentFragment debemos copiar su contenido pues luego necesitamos reinsertar
            // los nodos en el documento y al reinsertarse se quitan del DocumentFragment automáticamente
            // por parte del motor DOM, pues el problema es que el DocumentFragment se devuelve al usuario
            // el cual no espera cambios

            if (disconnectedFragment instanceof DocumentFragment)
            {
                LinkedList<Node> nodeList = new LinkedList<Node>();
                nodeList.add(disconnectedFragment); // Lo necesitamos para reconstruirlo

                if (disconnectedFragment.hasChildNodes())
                {
                    Node child = disconnectedFragment.getFirstChild();
                    while(child != null)
                    {
                        nodeList.add(child);
                        child = child.getNextSibling();
                    }
                }

                disconnectedNodesFastLoadMode.put(node,nodeList);
            }
            else // Un nodo concreto
            {
                disconnectedNodesFastLoadMode.put(node,disconnectedFragment);
            }
        }
        return disconnectedFragment;

        // OTRA ESTRATEGIA ES COPIAR CON JAVASCRIPT LOS NODOS HIJOS, ELIMINARLOS
        // DE VERDAD EN EL CLIENTE Y LUEGO REINSERTARLOS DE NUEVO VIA JAVASCRIPT
        // La pega sería un cierto parpadeo de la zona eliminada/reinsertada y posiblemente fastidiaría
        // alguna librería JavaScript de utilidad que hiciera algún uso de la zona
    }

    public void reconnectChildNodesToClient(Node node)
    {
        DelegateNodeImpl delegNode = ((ItsNatNodeInternal)node).getDelegateNode();
        if (!delegNode.isDisconnectedChildNodesFromClient())
            return; // No cambia nada, no hacemos nada (el modo por defecto)

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        if (itsNatDoc.isLoadingPhaseAndFastLoadMode())
        {
            // Nos hemos rajado y reconectado de nuevo en tiempo de carga y fastLoad = true
            // deshacemos la tarea "pendiente" de renderizar el fragmento removido
            ResponseNormalLoadStfulDocImpl loadResponse = getResponseNormalLoadStfulDoc();
            if (loadResponse.hasDisconnectedNodesFastLoadMode())
            {
                Map<Node,Object> disconnectedNodesFastLoadMode = loadResponse.getDisconnectedNodesFastLoadMode();
                disconnectedNodesFastLoadMode.remove(node); // En el caso de contenido nulo no llegamos a registrarlo por lo que es posible que no lo encuentre (es normal)
            }
        }

        ClientDocumentImpl[] allClients = getAllClientDocumentsCopy();
        for(int i = 0; i < allClients.length; i++)
        {
            ClientDocumentStfulImpl clientDoc = (ClientDocumentStfulImpl)allClients[i];
            ClientDocumentStfulDelegateImpl clientDocDeleg = clientDoc.getClientDocumentStfulDelegate();
            ClientMutationEventListenerStfulImpl mutListener = clientDocDeleg.getClientMutationEventListenerStful();            
            
            mutListener.removeAllChildNodes(node); // Se eliminan en el cliente
        }

        // Se supone que el nodo está vacío en el servidor pues no dejamos insertar hasta que haya reconexión
        delegNode.setDisconnectedChildNodesFromClient(false);
    }

    public ResponseNormalLoadStfulDocImpl getResponseNormalLoadStfulDoc()
    {
        // En tiempo de carga del documento estos tipos de datos son válidos y esperados
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        ItsNatServletRequestImpl loadINSRequest = itsNatDoc.getCurrentItsNatServletRequest();
        RequestNormalLoadDocImpl loadRequest = (RequestNormalLoadDocImpl)loadINSRequest.getRequest();
        return (ResponseNormalLoadStfulDocImpl)loadRequest.getResponseNormalLoadDoc();
    }

}
