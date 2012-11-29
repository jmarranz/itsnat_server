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

package org.itsnat.impl.core.event.client.domstd;

import org.w3c.dom.Node;

/**
 * Esta clase es debido a que la resolución de un path
 * es costoso pues hay que recorrer el árbol, sirve para
 * que se haga sólo una vez (se supone que el nodo no cambia
 * de sitio en el árbol).
 *
 * @author jmarranz
 */
public class NodeContainerImpl
{
    protected Node node;

    /**
     * Creates a new instance of NodeContainerImpl
     */
    public NodeContainerImpl(Node node)
    {
        this.node = node; // puede ser null
    }

    public Node get()
    {
        return node;
    }
}
