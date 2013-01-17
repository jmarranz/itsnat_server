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

import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.w3c.dom.html.HTMLTableElement;

/**
 * Is the interface of HTML &lt;table&gt; components.
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatHTMLComponentManager#createItsNatHTMLTable(org.w3c.dom.html.HTMLTableElement,ItsNatTableStructure,org.itsnat.core.NameValue[])
 */
public interface ItsNatHTMLTable extends ItsNatHTMLElementComponent,ItsNatTable
{
    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatHTMLTableUI getItsNatHTMLTableUI();

    /**
     * Returns the table header sub-component.
     *
     * @return the table header. Null if this table does not have a header.
     */
    public ItsNatHTMLTableHeader getItsNatHTMLTableHeader();

    /**
     * Returns the associated DOM element to this component.
     *
     * @return the associated DOM element.
     */
    public HTMLTableElement getHTMLTableElement();
}
