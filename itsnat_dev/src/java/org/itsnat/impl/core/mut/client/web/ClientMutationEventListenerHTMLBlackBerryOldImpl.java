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

package org.itsnat.impl.core.mut.client.web;

import org.itsnat.impl.core.browser.web.BrowserBlackBerryOld;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class ClientMutationEventListenerHTMLBlackBerryOldImpl extends ClientMutationEventListenerHTMLImpl
{
    public ClientMutationEventListenerHTMLBlackBerryOldImpl(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        super(clientDoc);
    }

    @Override
    public void postRenderAndSendMutationCode(MutationEvent mutEvent)
    {
        super.postRenderAndSendMutationCode(mutEvent);

        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();        
        BrowserBlackBerryOld browser = (BrowserBlackBerryOld)clientDoc.getBrowserWeb();
        String type = mutEvent.getType();

        if (browser.isHTMLInputFileValueBuggy() && type.equals("DOMNodeInserted"))
        {
            Node node = (Node)mutEvent.getTarget(); // node es el nodo nuevo
            fixTreeHTMLInputFileValue(node);
        }

        if (browser.isHTMLSelectMultipleOrWithSizeBuggy() && 
            (type.equals("DOMNodeInserted")||type.equals("DOMNodeRemoved")))
        {
            Node node = (Node)mutEvent.getTarget(); // node es el nodo nuevo o el que se va a eliminar
            if (DOMUtilHTML.isHTMLOptionOfSelectMultipleOrWithSize(node)) // node es un option
            {
                HTMLSelectElement select = (HTMLSelectElement)node.getParentNode();
                fixHTMLSelectMultipleOrWithSize(select);
            }
        }

    }

    private void fixHTMLSelectMultipleOrWithSize(HTMLSelectElement select)
    {
        // Este método soluciona un problema complejo de la BlackBerry JDE 4.6 y 4.7 (Bold y Storm)
        // Consiste en que los <select> con atributos "multiple" o "size" (o ambos)
        // se "corrompen" visualmente cuando se añade o elimina un <option> (no cuando
        // se selecciona o cambia el valor de una opción) aunque el DOM es correcto.
        // El problema ocurre cuando el elemento ya está renderizado y se modifica por la inserción/eliminación de options.
        // He probado mil trucos y ninguno funciona salvo estos trucos:
        // 1) Clonar el elemento y usar el nuevo, a costa de actualizar referencias en el cliente
        //   los option pueden ser los originales, el problema lo tiene el elemento SELECT
        // 2) Cambiar a combo box, tocar con el cursor el componente y volverlo a cambiar
        // a multiple (o poner de nuevo el size). El problema es que ¿como pedimos al
        // usuario "que toque" el componente? es una chapuza.
        // 3) Dejar que se visualice mal y usar un timer que haga display = "none", dejar renderizar
        // y de nuevo lanzar otro timer que lo vuelva a visualizar.

        // La solución 3) se aplicó con éxito relativo, la 1) es la que funciona siempre aunque suponga crear un nuevo elemento.

        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();
        
        StringBuilder code = new StringBuilder();
        String methodName = "blackBerryOldFixHTMLSelect";
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindFixHTMLSelectMethod(methodName));

        code.append("itsNatDoc." + methodName + "(" + clientDoc.getNodeReference(select,true,true) + ");\n");

        clientDoc.addCodeToSend(code.toString());
    }


    private String bindFixHTMLSelectMethod(String methodName)
    {
        StringBuilder code = new StringBuilder();

        code.append( "var func = function (elem)" );
        code.append( "{" );
        code.append( "    var newElem = elem.cloneNode(false);" );
        code.append( "    while(elem.firstChild != null) newElem.appendChild(elem.firstChild);" );
        code.append( "    elem.parentNode.replaceChild(newElem,elem);" );

        code.append( "    function updateListeners(map,elem,newElem)" );
        code.append( "    {" );
        code.append( "      map.doForAll(function (id,listener) {" );
        code.append( "            if (listener.currentTarget != elem) return;" );
        code.append( "            listener.currentTarget = newElem;" );
        code.append( "            if (listener.isUseCapture) " ); // es listener DOM
        code.append( "              newElem.addEventListener(listener.getType(),listener.w3cHander,listener.isUseCapture());" );
        code.append( "          }" );
        code.append( "      );" );
        code.append( "    }" );

        code.append( "    updateListeners(this.domListeners,elem,newElem);" );
        code.append( "    updateListeners(this.timerListeners,elem,newElem);" );
        code.append( "    updateListeners(this.userListenersById,elem,newElem);" );
        code.append( "    if (elem.itsNatCacheId) " ); // esta cacheado
        code.append( "    {" );
        code.append( "        newElem.itsNatCacheId = elem.itsNatCacheId;" );
        code.append( "        this.nodeCacheById.put(newElem.itsNatCacheId,newElem);" );
        code.append( "    }" );
        code.append( "    return newElem;" ); // No se usa este retorno pero por si en un futuro...
        code.append( "};" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();        
        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    private void fixTreeHTMLInputFileValue(Node node)
    {
        StringBuilder code = fixTreeHTMLInputFileValue(node,null);

        if ((code != null) && (code.length() > 0))
            clientDoc.addCodeToSend(code.toString());
    }

    private StringBuilder fixTreeHTMLInputFileValue(Node node,StringBuilder code)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return code;

        Element elem = (Element)node;

        if (DOMUtilHTML.isHTMLInputFileWithValueAttr(elem))
        {
            return fixHTMLInputFileValue((HTMLInputElement)elem,code);
        }
        else
        {
            Node child = elem.getFirstChild();
            while (child != null)
            {
                code = fixTreeHTMLInputFileValue(child,code);
                child = child.getNextSibling();
            }
        }

        return code;
    }

    private StringBuilder fixHTMLInputFileValue(HTMLInputElement elem,StringBuilder code)
    {
        // Solucionamos el problema de que la sola presencia del atributo
        // "value" en un <input type="file"> provoca una excepción.
        // Dicha excepción ocurre cuando BlackBerry *renderiza* el DOM
        // modificado, por lo que tras la renderización de la inserción (que pudo ser via innerHTML o DOM)
        // tenemos la oportunidad de eliminar el atributo value antes de que
        // BlackBerry muestre el control.

        if (code == null) code = new StringBuilder();

        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();        
        String jsRef = clientDoc.getNodeReference(elem,true,true);

        code.append(jsRef + ".removeAttribute(\"value\");\n");

        return code;
    }

}
