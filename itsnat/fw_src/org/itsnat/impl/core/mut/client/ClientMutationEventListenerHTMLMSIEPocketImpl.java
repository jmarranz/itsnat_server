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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.itsnat.impl.core.browser.BrowserMSIEPocket;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.dom.node.html.msie.JSRenderHTMLAttributeMSIEPocketImpl;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerStfulImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableElement;

/**
 *
 * @author jmarranz
 */
public class ClientMutationEventListenerHTMLMSIEPocketImpl extends ClientMutationEventListenerHTMLImpl
{
    public ClientMutationEventListenerHTMLMSIEPocketImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
    }

    public Map preRenderAndSendMutationCode(MutationEvent mutEvent)
    {
        Map ctx = super.preRenderAndSendMutationCode(mutEvent);

        // Pocket IE (WM 6 y 6.1) tiene un error muy tonto y es que en modo
        // "desktop" no permite insertar/eliminar TRs y TDs (y supongo que TH) en un
        // TABLE estando ya renderizada (aunque estuviera vacía), Pocket IE se vuelve loco
        // (no es una simple excepción). El error es debido al intento de renderizar
        // durante el proceso de inserción.
        // Esto también ocurre si insertados un TBODY (o THEAD
        // o TFOOT) si dicho TBODY tiene ya un TR, el TBODY por sí mismo
        // no provoca el error, el error es causado por el TR cuando se intenta
        // renderizar. Si el propio TABLE se insertó antes en el mismo script no hay tal problema
        // porque todavía no está renderizada.
        // Este problema no ocurre si por ejemplo se cambia el contenido de un TD.
        // La solución es cambiar el CSS display de la TABLE a cualquier otro valor soportado
        // que no sea "table" (el de por defecto, se puede ver con currentStyle)
        // antes de la inserción del TR, TD, TH o TBODY/THEAD/TFOOT con algo y después
        // volver a restaurar el estilo anterior, al insertarse con un estilo diferente a table
        // el renderizado como table (el "peligroso") lo hace después de la inserción sin problema.
        // Un valor de display como "block" o similares muestran una imagen errónea de la tabla
        // y queda feo aunque luego se restaure el valor viejo, por lo que lo mejor
        // es usar "none".
        // Esta técnica ya se utiliza en el archivo JS en la inserción/eliminación/reemplazo,
        // el problema es que no podemos detectar ahí cuando estamos por ejemplo insertando una fila (TR) con
        // varias celdas contenidas. El algoritmo de generación de código primero inserta
        // el TR lo cual provoca un display none y posterior vuelta a visible y luego
        // inserta cada celda, en el caso de un TR con tres celdas supone 4 ocultaciones/visualizaciones
        // de la tabla lo cual supone parpadeos inútiles.

        // Si hacemos aquí ya el display="none" reducimos los parpadeos sólo a 1 pues
        // el código JavaScript de cada inserción/borrado pondrá un display "none" a una tabla que
        // está ya como display "none" y al restaurar el valor antiguo volverá a poner un "none".


        String type = mutEvent.getType();
        if (type.equals("DOMNodeInserted"))
        {
            Node node = (Node)mutEvent.getTarget(); // node es el nuevo o a eliminar
            if (node.getNodeType() != Node.ELEMENT_NODE)
                return ctx;
            Element elem = (Element)node;
            if (elem.getParentNode().getNodeType() != Node.ELEMENT_NODE)
                return ctx; // Es rarisimo que insertemos un <body> o un <head> pero por si acaso
            Element parentNode = (Element)elem.getParentNode();
            if (parentNode instanceof HTMLElement)
            {
                String localName = parentNode.getLocalName();
                if (!localName.equals("table") && !localName.equals("tr") &&
                    !localName.equals("thead") && !localName.equals("tbody") && !localName.equals("tfoot"))
                    return ctx;
            }

            Element table = parentNode;
            while(!(table instanceof HTMLTableElement))
                table = (Element)table.getParentNode();

            String tableRef = clientDoc.getNodeReference(table,true,true);
            StringBuffer code = new StringBuffer();
            // El nombre "table" no se usa (y no se debe usar) en la generación de código de la inserción/borrado
            code.append("var table = " + tableRef + ";");
            code.append("var oldTableDisplay = itsNatDoc.hideTable(table);");

            clientDoc.addCodeToSend(code.toString());

            if (ctx == null) ctx = new HashMap();
            ctx.put("msiepocket_table_display", Boolean.TRUE);
            return ctx;
        }

        return ctx;
    }

    public void postRenderAndSendMutationCode(MutationEvent mutEvent,Map context)
    {
        super.postRenderAndSendMutationCode(mutEvent,context);

        if (context == null) return;
        if (!context.containsKey("msiepocket_table_display")) // Si está es true
            return;

        StringBuffer code = new StringBuffer();
        // El nombre "table" no se usa (y no se debe usar) en la generación de código de la inserción/borrado
        code.append("itsNatDoc.showTable(table,oldTableDisplay);");

        clientDoc.addCodeToSend(code.toString());
    }

    public void renderTreeDOMNodeInserted(Node newNode)
    {
        DocMutationEventListenerStfulImpl docMut = getDocMutationEventListenerStful();
        boolean oldMode = docMut.isEnabled();
        docMut.setEnabled(false); // Pues vamos a cambiar temporalmente atributos que luego restituiremos

        LinkedList attributes = insertTreeNodePreRender(newNode);

        super.renderTreeDOMNodeInserted(newNode);  // Esta llamada no genera eventos DOM mutation por lo que nos da igual si está enabled o no

        if (attributes != null)
            insertTreeNodePostRender(attributes);

        docMut.setEnabled(oldMode);
    }

    private LinkedList insertTreeNodePreRender(Node newNode)
    {
        LinkedList attributes = new LinkedList();
        insertTreeNodePreRender(newNode,attributes);
        return attributes;
    }

    private void insertTreeNodePreRender(Node node,LinkedList attributes)
    {
        insertNodePreRenderProcessNode(node,attributes);

        Node child = node.getFirstChild();
        while(child != null)
        {
            insertTreeNodePreRender(child,attributes);
            child = child.getNextSibling();
        }
    }

    private void insertNodePreRenderProcessNode(Node node,LinkedList attributes)
    {
        if (!(node instanceof HTMLElement)) return;

        HTMLElement elem = (HTMLElement)node;
        String localName = elem.getLocalName();
        if (BrowserMSIEPocket.needAttributeHandler(localName))
        {
            // Para acelerar el registro de listeners y evitar llamar al setAttribute emulado posteriormente en el IE Mobile
            LinkedList types = (LinkedList)BrowserMSIEPocket.getEventTypesByTagName().get(localName);
            Document doc = getClientDocumentStful().getItsNatStfulDocument().getDocument();
            JSRenderHTMLAttributeMSIEPocketImpl.addEventListenerAttrs(elem, types, attributes, doc);
        }
    }

    private void insertTreeNodePostRender(LinkedList attributes)
    {
        // Restauramos
        JSRenderHTMLAttributeMSIEPocketImpl.removeAttributes(attributes);
    }
}
