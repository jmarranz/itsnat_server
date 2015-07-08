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
 * Used by tree components to locate the required elements of the table layout.
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createDefaultItsNatTreeStructure()
 * @see ItsNatTree#getItsNatTreeStructure()
 * @see org.itsnat.core.domutil.ElementTreeNodeStructure
 */
public interface ItsNatTreeStructure
{
    /**
     * Returns the content element of a tree node.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementTreeNodeStructure}.</p>
     *
     * @param tree the tree component, may be used to provide contextual information. Default implementation ignores it.
     * @param row the tree node row (seeing the tree as a list).
     * @param nodeParent the element containing the tree node markup.
     *          Is a hint, if provided should be obtained by calling <code>tree.getItsNatTreeUI().getParentElementFromRow(row)</code>.
     * @return the content element of the tree node.
     */
    public Element getContentElement(ItsNatTree tree,int row,Element nodeParent);

    /**
     * Returns the handle element of a tree node.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementTreeNodeStructure}.</p>
     *
     * @param tree the tree component, may be used to provide contextual information. Default implementation ignores it.
     * @param row the tree node row (seeing the tree as a list).
     * @param nodeParent the element containing the tree node markup.
     *          Is a hint, if provided should be obtained by calling <code>tree.getItsNatTreeUI().getParentElementFromRow(row)</code>.
     * @return the handle element of the tree node.
     */
    public Element getHandleElement(ItsNatTree tree,int row,Element nodeParent);

    /**
     * Returns the icon element of a tree node.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementTreeNodeStructure}.</p>
     *
     * @param tree the tree component, may be used to provide contextual information. Default implementation ignores it.
     * @param row the tree node row (seeing the tree as a list).
     * @param nodeParent the element containing the tree node markup.
     *          Is a hint, if provided should be obtained by calling <code>tree.getItsNatTreeUI().getParentElementFromRow(row)</code>.
     * @return the icon element of the tree node.
     */
    public Element getIconElement(ItsNatTree tree,int row,Element nodeParent);

    /**
     * Returns the label element of a tree node. This element is used to render and edit
     * the tree node value.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementTreeNodeStructure}.</p>
     *
     * @param tree the tree component, may be used to provide contextual information. Default implementation ignores it.
     * @param row the tree node row (seeing the tree as a list).
     * @param nodeParent the element containing the tree node markup.
     *          Is a hint, if provided should be obtained by calling <code>tree.getItsNatTreeUI().getParentElementFromRow(row)</code>.
     * @return the label element of the tree node.
     * @see ItsNatTreeCellRenderer
     * @see ItsNatTreeCellEditor
     */
    public Element getLabelElement(ItsNatTree tree,int row,Element nodeParent);

    /**
     * Returns the element containing the child tree nodes of the specified tree node.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementTreeNodeStructure}.</p>
     *
     * @param tree the tree component, may be used to provide contextual information. Default implementation ignores it.
     * @param row the tree node row (seeing the tree as a list).
     * @param nodeParent the element containing the tree node markup.
     *          Is a hint, if provided should be obtained by calling <code>tree.getItsNatTreeUI().getParentElementFromRow(row)</code>.
     * @return the element containing the child list.
     */
    public Element getChildListElement(ItsNatTree tree,int row,Element nodeParent);
}
