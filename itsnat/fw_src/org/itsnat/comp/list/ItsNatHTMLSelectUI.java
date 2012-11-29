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

package org.itsnat.comp.list;

import org.itsnat.comp.ItsNatHTMLElementComponentUI;
import org.w3c.dom.html.HTMLOptionElement;

/**
 * Is the base interface of the User Interface of an HTML &lt;select&gt; based component.
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHTMLSelectUI extends ItsNatHTMLElementComponentUI,ItsNatListUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatHTMLSelect getItsNatHTMLSelect();

    /**
     * Returns the child option element at the specified index.
     *
     * <p>This method calls {@link ItsNatListUI#getElementAt(int)}.</p>
     *
     * @param index index of the option element to search.
     * @return the option element in this position or null if index is out of range.
     * @see ItsNatListUI#getElementAt(int)
     */
    public HTMLOptionElement getHTMLOptionElementAt(int index);
}
