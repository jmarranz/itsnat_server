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

import org.w3c.dom.Element;

/**
 * Is used to render tree node values as markup into DOM elements.
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createDefaultItsNatTreeCellRenderer()
 * @see ItsNatTree#getItsNatTreeCellRenderer()
 * @see ItsNatTree#setItsNatTreeCellRenderer(ItsNatTreeCellRenderer)
 */
public interface ItsNatTreeCellRenderer
{
    /**
     * Renders as markup the specified tree node value into the specified element, usually
     * as a text node.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementRenderer}.</p>
     *
     * <p>Default implementation ignores <code>isSelected</code>, <code>isExpanded</code>, <code>isLeaf</code>
     * and <code>hasFocus</code> (current implementation of ItsNat trees does not handle focus on tree nodes, ever is false).</p>
     *
     * @param tree the tree component, may be used to provide contextual information. Default implementation ignores it.
     * @param row the tree node row (seeing the tree as a list).
     * @param value the value to render.
     * @param isSelected true if the tree node is selected.
     * @param isExpanded true if the tree node is expanded.
     * @param isLeaf true if the tree node is a leaf.
     * @param hasFocus true if the tree node has the focus.
     * @param treeNodeLabelElem the tree node label element to render the value into. Is a hint, if provided should be obtained by calling <code>tree.getItsNatTreeUI().getLabelElementFromRow(row)</code>.
     * @param isNew true if this is the first time the markup is rendered. Default implementation ignores this parameter.
     */
    public void renderTreeCell(
                ItsNatTree tree,
                int row,
                Object value,
                boolean isSelected,
                boolean isExpanded,
                boolean isLeaf,
                boolean hasFocus,
                Element treeNodeLabelElem,
                boolean isNew);

    /**
     * Unrenders the markup of the specified tree node. This method is called <i>before</i> the markup
     * is removed.
     *
     * <p>Default implementation does nothing.</p>
     *
     * @param tree the tree component, may be used to provide contextual information. Default implementation ignores it.
     * @param row the tree node row (seeing the tree as a list).
     * @param treeNodeLabelElem the tree node label element to render the value into. Is a hint, if provided should be obtained by calling <code>tree.getItsNatTreeUI().getLabelElementFromRow(row)</code>.
     */
    public void unrenderTreeCell(
                ItsNatTree tree,
                int row,
                Element treeNodeLabelElem);
}
