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

package org.itsnat.comp.tree;

import org.itsnat.comp.*;
import javax.swing.CellEditor;
import org.w3c.dom.Element;

/**
 * Used to specify how a tree node value is edited in place.
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatComponentManager#createDefaultItsNatTreeCellEditor(ItsNatComponent)
 * @see ItsNatTree#getItsNatTreeCellEditor()
 */
public interface ItsNatTreeCellEditor extends CellEditor
{
    /**
     * Returns the component used to edit in place the tree node value.
     *
     * <p>Default implementation uses a {@link org.itsnat.comp.text.ItsNatHTMLInputText} (text not formatted version) to edit
     * the tree node value.</p>
     *
     * <p>Default implementation ignores <code>isSelected</code> parameter.</p>
     *
     * @param tree the tree component, may be used to provide contextual information. Default implementation ignores it.
     * @param row the tree node row (seeing the tree as a list).
     * @param value the value to edit (initial value).
     * @param isSelected true if the tree node is selected.
     * @param isExpanded true if the tree node is expanded.
     * @param isLeaf true if the tree node is a leaf.
     * @param labelElem the tree node label element to render the value into. Is a hint, if provided should be obtained by calling <code>tree.getItsNatTreeUI().getLabelElementFromRow(row)</code>.
     * @return the component used to edit in place the tree node value. Current implementation of trees does nothing with this component and may be null (is not mandatory to use a single component as an editor).
     * @see ItsNatTree#setItsNatTreeCellEditor(ItsNatTreeCellEditor)
     */
    public ItsNatComponent getTreeCellEditorComponent(
                ItsNatTree tree,
                int row,
                Object value,
                boolean isSelected,
                boolean isExpanded,
                boolean isLeaf,
                Element labelElem);
}
