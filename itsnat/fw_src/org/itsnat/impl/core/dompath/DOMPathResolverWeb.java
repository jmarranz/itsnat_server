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

package org.itsnat.impl.core.dompath;

import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.web.ItsNatHTMLDocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class DOMPathResolverWeb extends DOMPathResolver
{
    /** Creates a new instance of DOMPathResolverOtherNSDoc */
    public DOMPathResolverWeb(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        super(clientDoc);
    }

    public static DOMPathResolverWeb createDOMPathResolverWeb(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        if (itsNatDoc instanceof ItsNatHTMLDocumentImpl) // NO debe ser nulo
            return new DOMPathResolverHTMLDoc(clientDoc);
        else
            return new DOMPathResolverOtherNSDoc(clientDoc);
    }    
    
    public boolean isFilteredInClient(Node node)
    {
        return false;
    }
    
    @Override    
    protected Node getChildNodeFromPos(Node parentNode,int pos,boolean isTextNode)
    {
        if (!parentNode.hasChildNodes()) return null;

        int currPos = 0;
        Node currNode = parentNode.getFirstChild();
        while(currNode != null)
        {
            if (!isFilteredInClient(currNode))
            {
                int type = currNode.getNodeType();
                if (currPos == pos)
                {
                    if (isTextNode || (type != Node.TEXT_NODE))
                    {
                        // Los siguientes comentarios son más aplicables al código del cliente que al del servidor:
                        // Si isTextNode es true (buscamos un text node) y currNode no es un nodo de texto es que nos hemos pasado, probablemente el nodo de texto ha sido filtrado
                        // por la normalizacion etc (puede ocurrir en nodos texto con espacios), devolvemos currNode en este caso aunque no sea un text node porque es el más próximo
                        // Esto también es deseable en el cliente pues aunque en el servidor haya un nodo de texto
                        // en el cliente puede que fuera filtrado (ej. BlackBerry) pero si devuelve el próximo probablemente no haya problemas.
                        // Es mejor devolver el siguiente nodo del ausente nodo de texto pues este problema
                        // puede darse en el caso de buscar el elemento de referencia en un insertBefore
                        // en el caso de ausencia de nodo de texto, nos vale el siguiente (el anterior podría dar una inserción errónea),
                        // En el caso de eliminación en otro lugar se detecta que buscamos un nodo de texto pero no lo hemos encontrado (devolviendo el nodo siguiente no texto)
                        // evitando una eliminación errónea por ejemplo del elemento siguiente al nodo de texto filtrado.
                        // Si isTextNode es false y el nodo no es de texto, entonces está correctamente encontrado
                        return currNode;
                    }
                    // Si isTextNode = false (no buscamos un nodo de texto) y es nodo de texto el encontrado,
                    // lo ignoramos y seguimos iterando hasta encontrar el primer nodo que no sea de texto, currPos ya no se aumenta por lo que
                    // seguirá siendo currPos == pos true
                }
                else if(type != Node.TEXT_NODE) // Sólo contamos nodos que no sean de texto pues los de texto están sujetos a filtrado etc normalmente cuando tienen sólo espacios fines de línea etc y segun el navegador (habitual en MSIE por ejemplo en carga)
                        currPos++;
            }

            currNode = currNode.getNextSibling();
        }

        return null;
    }
    
    @Override
    protected Node getChildNodeFromStrPos(Node parentNode,String posStr)
    {
        // Vemos si se especifica un atributo o nodo de texto
        if (posStr.equals("de")) // de = documentElement
        {
            Document doc = getItsNatStfulDocument().getDocument();
            return doc.getDocumentElement();
        }

        int posBracket = posStr.indexOf('[');
        if (posBracket == -1)
        {
            int pos = Integer.parseInt(posStr);
            return getChildNodeFromPos(parentNode,pos,false);
        }
        else
        {
            int pos = Integer.parseInt(posStr.substring(0,posBracket));
            // Se especifica un atributo: num[@attrName]
            // o nodo de texto: num[t]
            if (posStr.charAt(posBracket + 1) == '@') // Atributo
            {
                // En ItsNat no es usado pero el usuario podría usarlo a través de
                // ScriptUtil.getNodeReference()
                String attrName = posStr.substring(posBracket + 2,posStr.length() - 1);
                Node child = getChildNodeFromPos(parentNode,pos,false);
                return ((Element)child).getAttributeNode(attrName); // Se devuelve un Attr
            }
            else
            {
                // Nodo de texto
                return getChildNodeFromPos(parentNode,pos,true);
            }
        }
    }    
}
