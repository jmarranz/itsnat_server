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

package org.itsnat.impl.comp.list;

import org.itsnat.comp.list.ItsNatListUI;
import org.w3c.dom.Element;

/**
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatListUIInternal extends ItsNatListUI
{
    public ItsNatListInternal getItsNatListInternal();

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Renders the specified value into the child element with the given position.
     *
     * @param index index of the element.
     * @param value the value to render.
     * @param isSelected if this child element is selected.
     * @param hasFocus if this child element has the focus. Current ItsNat implementation ever passes false.
     * @see org.itsnat.core.domutil.ElementList#setElementValueAt(int,Object)
     */
    public void setElementValueAt(int index,Object value,boolean isSelected,boolean hasFocus);

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Removes all child elements. The list is now empty.
     * @see org.itsnat.core.domutil.ElementListBase#removeAllElements()
     */
    public void removeAllElements();

    /**
     * ESTE METODO FUE PUBLICO ANTES
     * 
     * Inserts a new child element at the specified position rendering
     * the specified item value.
     *
     * @param index index of the new child element.
     * @param value the item value to render as markup.
     * @return the new element.
     * @see org.itsnat.core.domutil.ElementList#insertElementAt(int,Object)
     */
    public Element insertElementAt(int index,Object value);
}
