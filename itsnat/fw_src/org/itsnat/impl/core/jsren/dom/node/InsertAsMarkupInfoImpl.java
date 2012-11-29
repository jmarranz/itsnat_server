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
package org.itsnat.impl.core.jsren.dom.node;

import java.util.List;
import org.itsnat.core.ItsNatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class InsertAsMarkupInfoImpl
{
    public static final int CANNOT_INSERT_CHILDREN_VERIFIED = 1;
    public static final int IS_VALID_INSERTED_AS_MARKUP = 2;
    public static final int DO_NOT_KNOW = 3;

    protected List childNodeListNotValidInsertedAsMarkup;
    protected Node nodeNotForInsertChildrenAsMarkup;

    public InsertAsMarkupInfoImpl(Node nodeNotForInsertChildrenAsMarkup,List childNodeListNotValidInsertedAsMarkup)
    {
        this.nodeNotForInsertChildrenAsMarkup = nodeNotForInsertChildrenAsMarkup;
        this.childNodeListNotValidInsertedAsMarkup = childNodeListNotValidInsertedAsMarkup;
    }

    public Node getNodeNotForInsertChildrenAsMarkup()
    {
        return nodeNotForInsertChildrenAsMarkup;
    }
    
    public List getChildNodeListNotValidInsertedAsMarkup()
    {
        return childNodeListNotValidInsertedAsMarkup;
    }

    public int canInsertAllChildrenAsMarkup(Element elem)
    {
        // Se supone que elem y nodeNotForInsertChildrenAsMarkup están en el mismo nivel (mismo padre)
        // Si elem está antes que nodeNotForInsertChildrenAsMarkup entonces sabemos al menos
        // que puede es válido para ser insertado como markup, lo que no significa que sí mismo
        // pueda insertar a sus hijos directamente como markup (es el caso de TABLE en MSIE Old
        // que puede sí mismo insertarse pero no insertar el contenido directamente).

        if (nodeNotForInsertChildrenAsMarkup == elem)
            return CANNOT_INSERT_CHILDREN_VERIFIED;

        Node parentNode = nodeNotForInsertChildrenAsMarkup.getParentNode();
        if (elem.getParentNode() != parentNode) throw new ItsNatException("INTERNAL ERROR"); // Programación defensiva por si acaso

        Node current = parentNode.getFirstChild();

        while(true)
        {
            if (current == elem)
            {
                 // Significa que elem está ANTES que nodeNotForInsertChildrenAsMarkup y por tanto sabemos
                 // que puede insertarse sí mismo como markup pero no sabemos si puede hacerlo él mismo con sus hijos.
                // Devolvemos lo que sabemos
                return IS_VALID_INSERTED_AS_MARKUP;
            }
            else if (current == nodeNotForInsertChildrenAsMarkup)
                return DO_NOT_KNOW; // elem está después
            current = current.getNextSibling();
        }
    }
 
}
