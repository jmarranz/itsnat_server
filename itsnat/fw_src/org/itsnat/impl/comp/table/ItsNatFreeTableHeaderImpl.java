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

package org.itsnat.impl.comp.table;

import org.itsnat.comp.table.ItsNatFreeTable;
import org.itsnat.comp.table.ItsNatFreeTableHeader;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.table.ItsNatTableHeaderUI;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ItsNatFreeTableHeaderImpl extends ItsNatTableHeaderImpl implements ItsNatFreeTableHeader
{
    /**
     * Creates a new instance of ItsNatFreeTableHeaderImpl
     */
    public ItsNatFreeTableHeaderImpl(ItsNatFreeTableImpl tableComp,Element headerElem)
    {
        super(tableComp,headerElem);
    }

    public ItsNatFreeTable getItsNatFreeTable()
    {
        return (ItsNatFreeTable)tableComp;
    }

    public ItsNatTableHeaderUI createDefaultItsNatFreeTableHeaderUI()
    {
        return new ItsNatFreeTableHeaderUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatFreeTableHeaderUI();
    }

    public Node createDefaultNode()
    {
        // Este componente es esclavo del nodo "tabla" padre y si este
        // no tiene header no lo imponemos aquí.
        return null;
    }


}
