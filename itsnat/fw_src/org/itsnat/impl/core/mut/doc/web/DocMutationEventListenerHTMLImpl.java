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

package org.itsnat.impl.core.mut.doc.web;

import org.itsnat.core.ItsNatDOMException;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.web.ItsNatHTMLDocumentImpl;
import org.itsnat.impl.core.domimpl.html.HTMLTextAreaElementImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLStyleElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public class DocMutationEventListenerHTMLImpl extends DocMutationEventListenerStfulWebImpl
{
    public DocMutationEventListenerHTMLImpl(ItsNatHTMLDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    protected void checkOperation(MutationEvent mutEvent)
    {
        if (itsNatDoc.isDebugMode()) // En producción se supone que las operaciones que se realizan son válidas
        {
            String type = mutEvent.getType();
            if (type.equals("DOMNodeInserted"))
            {
                // Hay que tener en cuenta que el nodo YA está insertado en el DOM servidor
                Node newNode = (Node)mutEvent.getTarget();
                checkInsertedNodeTree(newNode);
            }
        }
    }

    private void checkInsertedNodeTree(Node node)
    {
        checkInsertedNode(node);

        Node child = node.getFirstChild();
        while(child != null)
        {
            checkInsertedNodeTree(child);
            child = child.getNextSibling();
        }
    }

    private void checkInsertedNode(Node node)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return;

        // Revisamos que no se inserte un TR directamente bajo TABLE
        // pues por ejemplo el parser del FireFox añade automáticamente el TBODY,
        // aunque via DOM FireFox lo admite lo evitamos por coherencia del parser,
        // además los componentes de ItsNat esperan la presencia de un BODY
        // En el caso de STYLE en el BODY es similar, el parser de FireFox y Safari lo mueven al <HEAD>
        // via DOM vale pero por coherencia no lo admitimos además Safari ignora los estilos introducidos de esta manera.

        if (node instanceof HTMLTableRowElement)
        {
            if (node.getParentNode() instanceof HTMLTableRowElement)
                throw new ItsNatDOMException("A TR element must be inserted below a TBODY, THEAD or TFOOT",node);
        }
        else if (node instanceof HTMLStyleElement)
        {
             if (!(node.getParentNode() instanceof HTMLHeadElement))
                throw new ItsNatDOMException("A STYLE element must be inserted below HEAD",node);
        }
    }

    protected void beforeAfterRenderAndSendMutationCode(boolean before,MutationEvent mutEvent,ClientDocumentImpl[] allClients)
    {
        super.beforeAfterRenderAndSendMutationCode(before,mutEvent,allClients);

        Node target = (Node)mutEvent.getTarget();
        if (before && (itsNatDoc.getDocument() instanceof HTMLDocument) &&
            (target instanceof HTMLTextAreaElement) &&
             mutEvent.getType().equals("DOMAttrModified"))
        {
            /* En HTML y XHTML en tiempo de carga el valor inicial de un control <textarea> viene dado por 
             * el nodo de texto independientemente del valor del atributo value.
             * El problema es que en modo fastMode los cambios al atributo value no generan
             * JavaScript, a esto le añadimos que no podemos distinguir el valor del atributo
             * value impuesto por el programador del valor inicial en el markup.
             * Por otra parte en control remoto no sabemos si manda el nodo de texto o el value.
             * Por ello usamos la propiedad especial contenida en la implementación de <textarea>
             * Siempre un cambio a value explícito debería mandar sobre el nodo de texto en tiempo
             * de carga.
             *
             * Esto NO es necesario en documentos XUL ni en SVG (comprobado en FireFox 3.5, Opera 9.x, Chrome 2.0 y Safari 3.1)
             * pues fuera de X/HTML el nodo de texto del textarea ES IGNORADO, sólo cuenta el atributo/propiedad "value"
               OJO este comportamiento "moderno" de textarea NO es aplicable a documentos con MIME XHTML en donde
               funciona como siempre.
             */
            HTMLTextAreaElementImpl elem = (HTMLTextAreaElementImpl)target;
            Attr attr = (Attr)mutEvent.getRelatedNode();
            if (attr.getName().equals("value"))
            {
                int changeType = mutEvent.getAttrChange();
                switch(changeType)
                {
                    case MutationEvent.ADDITION:
                    case MutationEvent.MODIFICATION:
                        String value = attr.getValue();
                        elem.setValueProperty(value);
                        break;
                    case MutationEvent.REMOVAL:
                        elem.setValueProperty(null); // En este caso el nodo de texto hijo decide el valor inicial
                        break;
                }
            }
        }
    }

}
