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

package org.itsnat.impl.core.mut.client;

import java.io.Serializable;
import java.util.LinkedList;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulOwnerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.BoundElementDocContainerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domimpl.ElementDocContainer;
import org.itsnat.impl.core.domimpl.ItsNatNodeInternal;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerStfulImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public abstract class ClientMutationEventListenerStfulImpl implements Serializable
{
    protected ClientDocumentStfulDelegateImpl clientDoc;

    public ClientMutationEventListenerStfulImpl(ClientDocumentStfulDelegateImpl clientDoc)
    {
        this.clientDoc = clientDoc;
    }

    public static ClientMutationEventListenerStfulImpl createClientMutationEventListenerStful(ClientDocumentStfulDelegateImpl clientDoc)
    {
        Browser browser = clientDoc.getClientDocumentStful().getBrowser();
        if (browser instanceof BrowserWeb)
            return ClientMutationEventListenerStfulWebImpl.createClientMutationEventListenerStfulWeb((ClientDocumentStfulDelegateWebImpl)clientDoc);
        else
            return new ClientMutationEventListenerStfulDroidImpl((ClientDocumentStfulDelegateDroidImpl)clientDoc);
    }

    public ClientDocumentStfulDelegateImpl getClientDocumentStfulDelegate()
    {
        return clientDoc;
    }

    public boolean canRenderAndSendMutationCode()
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        return (itsNatDoc.canRenderAndSendMutationCode() &&
                clientDoc.getClientDocumentStful().isSendCodeEnabled());
    }

    public DocMutationEventListenerStfulImpl getDocMutationEventListenerStful()
    {
        return clientDoc.getItsNatStfulDocument().getDocMutationListenerEventStful();
    }

    public boolean isDisconnectedFromClient(Node node)
    {
        return ((ItsNatNodeInternal)node).getDelegateNode().isDisconnectedFromClient();
    }

    
    protected void removeTreeFromNodeCache(Node node,LinkedList<String> idList)
    {
        String id = clientDoc.removeNodeFromCache(node);
        if ((id != null)&&(idList != null))
            idList.add(id);

        Node child = node.getFirstChild();
        while (child != null)
        {
            removeTreeFromNodeCache(child,idList);
            child = child.getNextSibling();
        }
    }        
    
    public void beforeRenderAndSendMutationCode(MutationEvent mutEvent)
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        String type = mutEvent.getType();

        if (type.equals("DOMNodeInserted") &&
            !itsNatDoc.isLoadingPhaseAndFastLoadMode())
        {
            Node insertedNode = (Node)mutEvent.getTarget();
            processTreeInsertedElementDocContainer(true,insertedNode);
        }
    }

    public void afterRenderAndSendMutationCode(MutationEvent mutEvent)
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        String type = mutEvent.getType();

        
        if (clientDoc.isNodeCacheEnabled() &&
            ( !itsNatDoc.isLoadingPhaseAndFastLoadMode() || itsNatDoc.isDebugMode()) &&
            type.equals("DOMNodeRemoved"))
        {
            // Esto lo hacemos sí o sí pues tenemos que eliminar del caché
            // en el servidor los nodos removidos, aunque el cliente no se entere (send code disabled)
            // al menos evitamos memory leaks en el servidor (y evitamos fallos al clonar
            // la caché inicialmente para un cliente control remoto)
            // Ha de llamarse lo último porque para eliminar el nodo en el cliente es más rápido
            // usar la caché y aunque el nodo haya sido eliminado ya en el cliente
            // (porque ya se ha enviado el código de eliminación) el registro de la caché
            // permanece, pues aunque el elemento cliente eliminado y el id no vuelven a reutilizarse
            // evitamos que la memoria crezca indefinidamente en cliente y servidor.

            // En carga y fast load en teoría no es necesario porque si es así es que estamos programando mal
            // pues si estamos eliminando un nodo que fue cacheado como no se genera código JavaScript
            // de las mutaciones significa que en el cliente cuando se vaya a cachear el nodo dicho nodo NO estará
            // en el árbol. Si fue cacheado es que se uso para algo o se cambió algo, porque la simple inserción
            // no cachea. Por eso nos interesa hacerlo en este caso cuando tenemos el modo debug con la única
            // finalidad de detectar errores en tiempo de desarrollo no por otra rzón.

            Node removedNode = (Node)mutEvent.getTarget();
            removeTreeFromNodeCache(removedNode);
        }

        if (type.equals("DOMNodeInserted") &&
            !itsNatDoc.isLoadingPhaseAndFastLoadMode())
        {
            Node insertedNode = (Node)mutEvent.getTarget();
            processTreeInsertedElementDocContainer(false,insertedNode);
        }
    }    





    protected void processTreeInsertedElementDocContainer(boolean beforeRender,Node node)
    {
        if (node instanceof ElementDocContainer)
        {
            // En el caso de isLoading y FastLoadMode no es necesario
            // porque lo que interesa es el markup finalmente enviado al cliente
            // pues es el cliente el que va a cargar el iframe/object/embed, los
            // estados intermedios del DOM no nos interesan. En este caso
            // el proceso ya se hace al final, al serializar.
            ElementDocContainer elem = (ElementDocContainer)node;
            BoundElementDocContainerImpl bindInfo = elem.getElementDocContainerWrapper().getBoundElementDocContainer();

            if (bindInfo != null)
            {
                DocMutationEventListenerStfulImpl docMut = getDocMutationEventListenerStful();
                boolean modeOld = docMut.isEnabled();
                docMut.setEnabled(false);
                if (beforeRender)
                {
                    // Un iframe/object/embed/applet binding ha sido registrado, modificamos el atributo que define el URL
                    // para que al renderizar se envíe el nuevo
                    ClientDocumentStfulImpl clientDocParent = clientDoc.getClientDocumentStful();
                    if (clientDocParent instanceof ClientDocumentStfulOwnerImpl)
                        bindInfo.setURLForClientOwner((ClientDocumentStfulOwnerImpl)clientDocParent);
                    else // attached
                        bindInfo.setURLForClientAttached((ClientDocumentAttachedClientImpl)clientDocParent);
                }
                else // after
                {
                    // Como fue cambiado el URL, hay que restaurar
                    ClientDocumentStfulImpl clientDocParent = clientDoc.getClientDocumentStful();
                    bindInfo.restoreOriginalURL(clientDocParent);
                }
                docMut.setEnabled(modeOld);
            }
        }

        Node child = node.getFirstChild();
        while (child != null)
        {
            processTreeInsertedElementDocContainer(beforeRender,child);
            child = child.getNextSibling();
        }
    }
    
    public void renderAndSendMutationCode(MutationEvent mutEvent)
    {
        String type = mutEvent.getType();
        if (type.equals("DOMNodeInserted"))
        {
            // Hay que tener en cuenta que el nodo YA está insertado en el DOM servidor
            //Element parent = (Element)mutEvent.getRelatedNode();
            Node newNode = (Node)mutEvent.getTarget();

            Object code = getTreeDOMNodeInsertedCode(newNode);
            clientDoc.addCodeToSend(code);
        }
        else if (type.equals("DOMNodeRemoved"))
        {
            // El nodo todavía no ha sido removido del árbol DOM servidor

            //Element parent = (Element)mutEvent.getRelatedNode();
            Node removedNode = (Node)mutEvent.getTarget();

            // No damos error en el caso de desconectado pero no generamos el código porque necesitamos
            // eliminar los nodos hijo sin que el cliente se entere, esto sólo es en fase de
            // desconexión pues estando desconectado un nodo no podrán insertarse hijos nuevos (ni por tanto eliminarse)
            if (!isDisconnectedFromClient(removedNode))
            {
                Object code = getTreeDOMNodeRemovedCode(removedNode);
                clientDoc.addCodeToSend(code);
            }
        }
        else if (type.equals("DOMAttrModified"))
        {
            Attr attr = (Attr)mutEvent.getRelatedNode();
            Element elem = (Element)mutEvent.getTarget();
            int changeType = mutEvent.getAttrChange();            
            String code = getDOMAttrModifiedCode(attr,elem,changeType);

            clientDoc.addCodeToSend(code);
        }
        else if (type.equals("DOMCharacterDataModified"))
        {
            CharacterData charDataNode = (CharacterData)mutEvent.getTarget();
            String code = getCharacterDataModifiedCode(charDataNode);
            clientDoc.addCodeToSend(code);
        }
    }    
    
    protected void removeTreeFromNodeCache(Node node)
    {
        // Quitamos los nodos del trozo de árbol del caché (si hay) porque cuando insertamos
        // un nodo al documento en el cliente enviamos siempre la "regeneración"
        // via DOM del nodo porque no detectamos cambios en nodos que no están
        // vinculados al Document, sin embargo el nodo en el servidor que se inserta puede ser
        // un nodo que previamente se quitara del árbol (y conservamos) y que si no quitamos
        // de la caché aquí estaría en la cache posiblemente, pero en el cliente el nodo será uno nuevo,
        // así desde el punto de vista  de la caché el nodo insertado es nuevo.

        LinkedList<String> idList = null;
        if (clientDoc.getClientDocumentStful().isSendCodeEnabled())
            idList = new LinkedList<String>();

        removeTreeFromNodeCache(node,idList);

        if ((idList != null)&& !idList.isEmpty())
        {
            String code = getRemoveNodeFromCacheCode(idList); 
            clientDoc.addCodeToSend(code);   // El código generado es compatible con todos los navegadores

            /*
             Un nodo se quita de la caché cuando se elimina del documento DOM
             (salvo algún en un caso especial de "preventive caching"),
             si esto se ejecuta en tiempo de carga y fastLoad = true significa
             que el nodo que se cacheará (y descacheará) en el cliente no
             estará presente en el DOM de carga.
             Esto es debido a que el programador ha utilizado el nodo de alguna
             manera (por ejemplo usando ScriptUtil.getNodeReference()) pues
             en inserción no hay cacheado.
             En el caso de error NO evitamos que se envíe el código JavaScript al cliente
             pues nos interesa que de error (aunque en este caso no ocurre) pues se da el problema
             de la captura de excepciones en el proceso de los mutation events del Batik DOM (afortunadamente el error se ve en la consola)
             */
            ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
            if (itsNatDoc.isLoadingPhaseAndFastLoadMode())
                throw new ItsNatDOMException("A cached DOM node is being removed on load time in fast load mode. Avoid any access to this node or avoid removing in load time (use a load event instead) or disable fast load mode.",node);
        }
    }    
    
    public void removeAllChild(Node node)
    {
        if (canRenderAndSendMutationCode()) // Por si acaso está desactivado el enviar código en este cliente
        {
            String code = getRemoveAllChildCode(node);
            clientDoc.addCodeToSend(code);
        }
    }
    

    public abstract String getRemoveAllChildCode(Node node);    
    public abstract void preRenderAndSendMutationCode(MutationEvent mutEvent);  
    public abstract Object getTreeDOMNodeInsertedCode(Node newNode);
    public abstract Object getTreeDOMNodeRemovedCode(Node removedNode);
    public abstract String getRemoveNodeFromCacheCode(LinkedList<String> idList);
    protected abstract String getDOMAttrModifiedCode(Attr attr,Element elem,int changeType);
    public abstract void postRenderAndSendMutationCode(MutationEvent mutEvent);    
    protected abstract String getCharacterDataModifiedCode(CharacterData charDataNode);  

}
