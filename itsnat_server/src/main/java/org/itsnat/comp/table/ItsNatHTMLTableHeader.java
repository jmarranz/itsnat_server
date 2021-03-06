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
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 * Is the interface of the HTML table header component.
 *
 * <p>This component is associated to the &lt;thead&gt; element of the HTML table if present.</p>
 *
 * <p>ItsNat provides a default implementation of this interface but is ever used (instanced)
 * alongside a table component as the parent of this component.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHTMLTableHeader extends ItsNatHTMLElementComponent,ItsNatTableHeader
{
    /**
     * Returns the table component this header belongs to.
     *
     * @return the parent table component. Never is null.
     */
    public ItsNatHTMLTable getItsNatHTMLTable();

    /**
     * Returns the table section element (the &lt;thead&gt;) of this header.
     *
     * @return the table section element of this header.
     */
    public HTMLTableSectionElement getHTMLTableSectionElement();

    /**
     * Returns the row parent element (the &lt;row&gt;) of the header cells.
     *
     * @return the row parent element of cells.
     */
    public HTMLTableRowElement getHTMLTableRowElement();

    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatHTMLTableHeaderUI getItsNatHTMLTableHeaderUI();
}
