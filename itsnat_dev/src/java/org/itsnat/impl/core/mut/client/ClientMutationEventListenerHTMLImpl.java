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

import java.util.Map;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserBlackBerryOld;
import org.itsnat.impl.core.browser.BrowserGecko;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.browser.webkit.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.SVGWebInfoImpl;
import org.itsnat.impl.core.listener.WaitForEventListenerImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public abstract class ClientMutationEventListenerHTMLImpl extends ClientMutationEventListenerStfulImpl
{
    public ClientMutationEventListenerHTMLImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
    }

    public static ClientMutationEventListenerHTMLImpl createClientMutationEventListenerHTML(ClientDocumentStfulImpl clientDoc)
    {
        Browser browser = clientDoc.getBrowser();
        if (browser instanceof BrowserMSIEOld)
            return new ClientMutationEventListenerHTMLMSIEOldImpl(clientDoc);        
        else if (browser instanceof BrowserBlackBerryOld)
            return new ClientMutationEventListenerHTMLBlackBerryOldImpl(clientDoc);
        else if (browser instanceof BrowserWebKit)
            return ClientMutationEventListenerHTMLWebKitImpl.createClientMutationEventListenerHTMLWebKit(clientDoc);
        else if (browser instanceof BrowserGecko)
            return new ClientMutationEventListenerHTMLDefaultImpl(clientDoc);        
        else
            return new ClientMutationEventListenerHTMLDefaultImpl(clientDoc);
    }

    public Map preRenderAndSendMutationCode(MutationEvent mutEvent)
    {
        String type = mutEvent.getType();

        if (type.equals("DOMNodeRemoved"))
        {
            if (SVGWebInfoImpl.isSVGWebEnabled(clientDoc))
            {
                Node removedNode = (Node)mutEvent.getTarget();
                SVGWebInfoImpl svgWeb = clientDoc.getSVGWebInfo();
                if (!svgWeb.isSVGNodeProcessedBySVGWebFlash(removedNode))
                {
                    // Si el nodo removeNode es un nodo normal
                    // son los hijos los que pueden tener nodos SVG procesados por SVGWeb
                    // en esos nodos hay que hacer un removeChild para cada uno de ellos
                    // (en el propio nodo a eliminar obviamente se hará como parte del proceso normal)
                    fixTreeRemovedSVGRootSVGWeb(removedNode);
                }
            }
        }

        return null;
    }

    public void postRenderAndSendMutationCode(MutationEvent mutEvent,Map context)
    {
        super.postRenderAndSendMutationCode(mutEvent,context);

        String type = mutEvent.getType();

        if (type.equals("DOMNodeRemoved"))
        {
            Node removedNode = (Node)mutEvent.getTarget();
            if (removedNode instanceof Text)
            {
                if (SVGWebInfoImpl.isSVGNodeProcessedBySVGWebFlash(removedNode, clientDoc))
                {
                    // SVGWeb no soporta bien la eliminación de nodos de texto
                    // se elimina del DOM pero no se actualiza visualmente
                    // sin embargo he descubierto que simplemente reinsertando
                    // el nodo padre se actualiza.
                    StringBuffer code = new StringBuffer();

                    Node parentNode = removedNode.getParentNode(); // Será un elemento
                    String jsRef = clientDoc.getNodeReference(parentNode,true,true);

                    code.append("var elem = " + jsRef + ";\n");
                    code.append("var elemClone = elem.cloneNode(false);\n");
                    code.append("elem.parentNode.replaceChild(elemClone,elem);");
                    code.append("elemClone.parentNode.replaceChild(elem,elemClone);");

                    clientDoc.addCodeToSend(code);
                }
            }
        }
        else if (type.equals("DOMNodeInserted"))
        {
            if (SVGWebInfoImpl.isSVGWebEnabled(clientDoc))
            {
                Node newNode = (Node)mutEvent.getTarget();
                processTreeInsertedSVGRootSVGWebWaitForEvent(newNode);
            }
        }
    }

    protected void fixTreeRemovedSVGRootSVGWeb(Node node)
    {
        // Los nodos SVG root procesados por SVGWeb necesitan ser eliminados
        // a través de un método especial de SVGWeb, si eliminamos un nodo
        // normal pero que contiene nodos SVG de SVGWeb dichos nodos (procesados
        // por <objects>/<embeds>) no serán liberados correctamente,
        // por eso antes de hacer el borrado normal del nodo padre buscamos
        // nodos hijos SVG de SVGWeb para eliminarlos antes liberando recursos.

        StringBuffer code = fixTreeRemovedSVGRootSVGWeb(node,null);

        if ((code != null) && (code.length() > 0))
            clientDoc.addCodeToSend(code.toString());
    }

    protected StringBuffer fixTreeRemovedSVGRootSVGWeb(Node node,StringBuffer code)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return code;

        Element elem = (Element)node;
        if (SVGWebInfoImpl.isSVGRootElementProcessedBySVGWebFlash(elem,clientDoc))
        {
            if (code == null) code = new StringBuffer();

            String jsRef = clientDoc.getNodeReference(elem,false,true); // No cacheamos pues lo vamos a eliminar
            code.append("var elem = " + jsRef + ";\n");
            code.append("itsNatDoc.win.svgweb.removeChild(elem,elem.parentNode);\n");

            return code;
        }

        Node child = elem.getFirstChild();
        while (child != null)
        {
            code = fixTreeRemovedSVGRootSVGWeb(child,code);
            child = child.getNextSibling();
        }

        return code;
    }


    protected void processTreeInsertedSVGRootSVGWebWaitForEvent(Node node)
    {
        // Cuando se inserta dinámicamente un nuevo nodo SVG via SVGWeb
        // posteriores cambios en el DOM SVG tras la inserción fallarán,
        // apenas cambios en los atributos del nodo SVG root funcionan.
        // Hay que tener en cuenta que los nodos utilizados en la inserción
        // son provisionales y serán substituidos por otros tras la renderización
        // Por ello SVGWeb recomienda posponer estos accesos/cambios al DOM
        // a después del evento SVGLoad que es emitido artificialmente
        // a los listeners SVGLoad registrados en el nodo SVG tras la inserción.
        // En ItsNat esto obliga al programador a usar la misma técnica, para
        // evitarlo usamos la técnica WaitForEventListener que es una marca que añadimos
        // a la cola de código JS a enviar al cliente tal que se envía el código
        // hasta la marca (podrá añadirse código a la cola después pero no se envía
        // al cliente hasta que no se quite la marca).
        // Cuando el evento SVGLoad se recibe quitamos la marca y ya se puede
        // enviar el código JS que le sigue (hasta otra posible marca de otro
        // nodo root SVGWeb insertado).
        // Esto permite funcionar inserciones dinámicas en SVGWeb en control remoto
        // La pega es si el usuario registra un listener SVGLoad propio (sabe que puede
        // pues SVGWeb lo permite), dicho listener no es enviado al cliente pues está
        // después de la marca y cuando es enviado es cuando se ha recibido ya el evento SVGLoad,
        // lo que hacemos para evitar esto es quitar la marca WaitForEventListener cuando
        // detectemos que el usuario añade un SVGLoad listener, suponemos que
        // el usuario es consciente de lo que hace y no debería cambiar el DOM
        // inmediatamente tras la inserción (para eso está el SVGLoad). Esto se hace en otro lugar.

        if (node.getNodeType() != Node.ELEMENT_NODE) return;

        Element elem = (Element)node;
        if (SVGWebInfoImpl.isSVGRootElementProcessedBySVGWebFlash(elem,clientDoc))
        {
            WaitForEventListenerImpl listener = new WaitForEventListenerImpl(elem,"SVGLoad");
            clientDoc.addEventListener((EventTarget)elem,"SVGLoad",listener,false);
            clientDoc.addCodeToSend(listener); // Añadimos la marca.
            return;
        }

        Node child = elem.getFirstChild();
        while (child != null)
        {
            processTreeInsertedSVGRootSVGWebWaitForEvent(child);
            child = child.getNextSibling();
        }
    }
}
