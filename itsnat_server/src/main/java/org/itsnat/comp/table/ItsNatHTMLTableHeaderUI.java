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

import org.itsnat.comp.ItsNatHTMLElementComponentUI;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 * Is the base interface of the User Interface of an HTML table header component.
 *
 * <p>This interface basically provides casts of the base methods.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHTMLTableHeaderUI extends ItsNatHTMLElementComponentUI,ItsNatTableHeaderUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatHTMLTableHeader getItsNatHTMLTableHeader();

    /**
     * Returns the &lt;thead&gt; element.
     *
     * @return the &lt;thead&gt; element.
     */
    public HTMLTableSectionElement getHTMLTableSectionElement();

    /**
     * Returns the &lt;row&gt; element.
     *
     * @return the &lt;row&gt; element.
     */
    public HTMLTableRowElement getHTMLTableRowElement();


    /**
     * Returns the &lt;td&gt; or &lt;th&gt; element at the specified column.
     *
     * @param column the column index of the cell element to search.
     * @return the element in this position or null if some index is out of range.
     * @see ItsNatTableHeaderUI#getElementAt(int)
     */
    public HTMLTableCellElement getHTMLTableCellElementAt(int column);
}
