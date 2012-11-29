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

import org.itsnat.comp.*;
import javax.swing.CellEditor;
import org.w3c.dom.Element;

/**
 * Used to specify how a list item value is edited in place.
 *
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatComponentManager#createDefaultItsNatListCellEditor(ItsNatComponent)
 * @see ItsNatList#getItsNatListCellEditor()
 */
public interface ItsNatListCellEditor extends CellEditor
{
    /**
     * Returns the component used to edit in place the list item value.
     *
     * <p>Default implementation uses a {@link org.itsnat.comp.text.ItsNatHTMLInputText} (text not formatted version) to edit
     * the list item value.</p>
     *
     * <p>Default implementation ignores <code>isSelected</code> parameter.</p>
     *
     *
     * @param list the list component, may be used to provide contextual information. Default implementation ignores it.
     * @param index the list item index.
     * @param value the value to edit (initial value).
     * @param isSelected true if the cell is selected.
     * @param cellContentElem the list item content element to render the value into. Is a hint, if provided should be obtained by calling <code>list.getItsNatListUI().getContentElementAt(index)</code>.
     * @return the component used to edit in place the list item value. Current implementation of lists does nothing with this component and may be null (is not mandatory to use a single component as an editor).
     * @see ItsNatList#setItsNatListCellEditor(ItsNatListCellEditor)
     */
    public ItsNatComponent getListCellEditorComponent(
                ItsNatList list,
                int index,
                Object value,
                boolean isSelected,
                Element cellContentElem);
}
