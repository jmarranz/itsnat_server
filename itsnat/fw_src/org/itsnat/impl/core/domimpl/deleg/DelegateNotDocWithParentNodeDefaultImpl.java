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

package org.itsnat.impl.core.domimpl.deleg;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.domimpl.ItsNatNodeInternal;

/**
 *
 * @author jmarranz
 */
public class DelegateNotDocWithParentNodeDefaultImpl extends DelegateNotDocWithtParentNodeImpl
{
    public DelegateNotDocWithParentNodeDefaultImpl(ItsNatNodeInternal node)
    {
        super(node);
    }

    public boolean isDisconnectedChildNodesFromClient()
    {
        checkHasSenseDisconnectedChildNodesFromClient();

        // Sólo si el propio nodo está desconectado también los hijos lo estarán
        // aunque la verdad es que esto es por poner algo pues salvo los Element
        // no hay más casos DOM con nodos hijos (salvo el Document que se trata aparte)
        return isDisconnectedFromClient();
    }

    public void setDisconnectedChildNodesFromClient(boolean disconnectedChildNodesFromClient)
    {
        checkHasSenseDisconnectedChildNodesFromClient();
        throw new ItsNatException("Only content of elements can be disconnected from client");
    }
}
