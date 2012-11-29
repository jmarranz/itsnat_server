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

package org.itsnat.impl.core.path;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.dom.node.html.msie.JSRenderHTMLTextMSIEPocketImpl;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *
 * @author jmarranz
 */
public class DOMPathResolverHTMLDocMSIEPocket extends DOMPathResolverHTMLDoc
{
    /**
     * Creates a new instance of DOMPathResolverHTMLDocMSIEPocket
     */
    public DOMPathResolverHTMLDocMSIEPocket(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
    }

    public boolean isFilteredInClient(Node node)
    {
        // MSIE Pocket no representa en el árbol DOM los comentarios
        // (obviamente hablamos de HTML, no hay otra cosa con AJAX)
        // Los nodos de texto son totalmente filtrados, no podemos acceder a ellos (usando childNodes en el cliente).

        int type = node.getNodeType();
        if (type == Node.COMMENT_NODE)
            return true;
        else if (type == Node.TEXT_NODE)
            return true;

        return false;
    }

    public static boolean isSignificativeNode(Node node)
    {
        // Ignoramos aquellos nodos que son de texto, contienen espacios/tabs/retornos
        // y no tienen influencia visual, dichos nodos no serán enviados al cliente
        // para ahorrar el enorme esfuerzo (inútil por otra parte) de añadirlos (via innerHTML del padre completo normalmente)
        // También evitamos los comentarios pues no se envían al cliente
        if (node instanceof Comment)
            return false;
        if ((node instanceof Text) &&
            JSRenderHTMLTextMSIEPocketImpl.isUnusefulTextNode((Text)node,node.getParentNode()))
            return false;
        return true;
    }

    public static Node getPreviousSignificativeSibling(Node node)
    {
        // Devuelve nodos que son insertados en el cliente
        // aunque no se manifiesten en el DOM
        Node prevSibling = node;
        do
        {
            prevSibling = prevSibling.getPreviousSibling();
        }
        while((prevSibling != null) && !isSignificativeNode(prevSibling));
        return prevSibling; // Será un Element
    }

    public static Node getNextSignificativeSibling(Node node)
    {
        // Leer notas en getPreviousSignificativeSibling
        Node nextSibling = node;
        do
        {
            nextSibling = nextSibling.getNextSibling();
        }
        while((nextSibling != null) && !isSignificativeNode(nextSibling));
        return nextSibling; // Será un Element
    }


}
