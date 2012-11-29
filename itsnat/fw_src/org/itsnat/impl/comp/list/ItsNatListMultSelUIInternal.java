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

import org.itsnat.comp.list.ItsNatListMultSel;

/**
 * ESTA INTERFASE FUE PUBLICA ANTES
 * 
 * Is the base interface of the User Interface of a list based component
 * using multiple selection.
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatListMultSelUIInternal extends ItsNatListUIInternal
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatListMultSel getItsNatListMultSel();

    /**
     * Sets the selection state of the specified list item on markup.
     *
     * <p>If this component is a &lt;select&gt; the attribute "selected"
     * of the involved &lt;option&gt; is defined accordingly, otherwise (free lists) it does nothing, use a list selection model listener to decorate
     * the markup when selection state changes.</p>
     *
     * @param index index of the list item to change selection state.
     * @param selected if true the markup is set as selected.
     */
    public void setSelectedIndex(int index,boolean selected);
}
