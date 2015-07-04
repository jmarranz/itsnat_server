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
package org.itsnat.impl.core.scriptren.shared.node;

import java.util.LinkedList;
import java.util.List;
import org.itsnat.core.ItsNatException;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class CannotInsertAsMarkupCauseImpl
{
    public static final int CAN_INSERT_VERIFIED = 1;
    public static final int CANNOT_INSERT_VERIFIED = 2;
    public static final int DO_NOT_KNOW = 3;

    protected Node nodeNotForInsertChildrenAsMarkup;
    protected List<Node> childNodeListNotValidInsertedAsMarkup; // No es una lista de hijos directos, sino una rama de nodos

    public CannotInsertAsMarkupCauseImpl(Node parent)
    {
        this(parent,(List<Node>)null);
    }

    public CannotInsertAsMarkupCauseImpl(InsertAsMarkupInfoImpl info)
    {
        this(info.getNodeNotForInsertChildrenAsMarkup(),info.getChildNodeListNotValidInsertedAsMarkup());
    }

    public CannotInsertAsMarkupCauseImpl(Node parent,Node childNotValidInsertedAsMarkup)
    {
        this.nodeNotForInsertChildrenAsMarkup = parent;

        if (childNotValidInsertedAsMarkup != null)
        {
            if (parent == childNotValidInsertedAsMarkup) throw new ItsNatException("INTERNAL ERROR");
            // Insertamos hasta el parent pero no el propio parent, pues son los hijos que han causado
            // problemas los que nos interesan
            this.childNodeListNotValidInsertedAsMarkup = new LinkedList<Node>();
            do
            {
                childNodeListNotValidInsertedAsMarkup.add(0,childNotValidInsertedAsMarkup);
                childNotValidInsertedAsMarkup = childNotValidInsertedAsMarkup.getParentNode();
            }
            while(parent != childNotValidInsertedAsMarkup);
        }
    }

    public CannotInsertAsMarkupCauseImpl(Node nodeNotForInsertChildrenAsMarkup,List<Node> childNodeListNotValidInsertedAsMarkup)
    {
        this.nodeNotForInsertChildrenAsMarkup = nodeNotForInsertChildrenAsMarkup;
        this.childNodeListNotValidInsertedAsMarkup = childNodeListNotValidInsertedAsMarkup;
    }

    public InsertAsMarkupInfoImpl createInsertAsMarkupInfoNextLevel()
    {
        if (childNodeListNotValidInsertedAsMarkup == null || childNodeListNotValidInsertedAsMarkup.isEmpty())
            return null; // El nodo padre mismo no puede utilizar innerHTML (o nuestro innerXML) por alguna razón,
                         // pero los hijos no tienen la culpa
        int size = childNodeListNotValidInsertedAsMarkup.size();
        // size NO es cero
        if (size == 1)
        {
            // Sabemos del único nodo de childNodeListNotValidInsertedAsMarkup que no puede insertarse como
            // markup pero NO sabemos si el contenido (hijos) del mismo puede ser insertado (ej. si admite innerHTML y los hijos son insertables)
            // por lo que NO podemos deducir que sea el nuevo nodeNotForInsertChildrenAsMarkup
            return null;
        }
        else // size es > 1
        {
            // Hay más de un nodo en childNodeListNotValidInsertedAsMarkup, el primero (el de índice 0) es el más alto,
            // significa que en alguno más abajo se detectó que no podía insertarse como markup
            // por lo que implica que los padres no pueden insertar sus hijos como markup
            Node nodeNotForInsertChildrenAsMarkup = childNodeListNotValidInsertedAsMarkup.get(0);
            return new InsertAsMarkupInfoImpl(nodeNotForInsertChildrenAsMarkup,
                        childNodeListNotValidInsertedAsMarkup.subList(1, size));
        }
    }
}
