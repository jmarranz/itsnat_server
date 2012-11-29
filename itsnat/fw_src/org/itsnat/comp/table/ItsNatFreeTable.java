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

package org.itsnat.comp.table;

import org.itsnat.comp.*;

/**
 * Is the interface of the free table components.
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createItsNatFreeTable(org.w3c.dom.Element element,ItsNatTableStructure,org.itsnat.core.NameValue[] artifacts)
 */
public interface ItsNatFreeTable extends ItsNatTable,ItsNatFreeComponent
{
    /**
     * Returns the table header sub-component.
     *
     * @return the table header. Null if this table does not have a header.
     */
    public ItsNatFreeTableHeader getItsNatFreeTableHeader();
}
