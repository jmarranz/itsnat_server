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

import javax.swing.tree.TreePath;
import org.itsnat.core.ItsNatUserData;
import org.w3c.dom.Element;

/**
 * Contains visual information of a tree node.
 *
 * <p>This interface is the "componentized" version of
 * {@link org.itsnat.core.domutil.ElementTreeNode} and
 * follows a similar philosophy. The objective is similar to {@link org.itsnat.comp.list.ItsNatListCellUI}
 * applied to tree nodes. Child index, row, tree path, deep level etc are tolerant
 * to tree changes (are automatically updated).</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatTreeUI#getItsNatTreeCellUIFromRow(int)
 * @see ItsNatTreeUI#getItsNatTreeCellUIFromNode(org.w3c.dom.Node)
 */
public interface ItsNatTreeCellUI extends ItsNatUserData
{
    /**
     * Returns the parent tree UI this object belongs to.
     *
     * @return the parent tree UI.
     */
    public ItsNatTreeUI getItsNatTreeUI();

    /**
     * Returns the parent tree node UI of this node.
     *
     * @return the parent tree node UI.
     */
    public ItsNatTreeCellUI getTreeNodeUIParent();

    /**
     * Returns an array with tree node UI objects from this node to the top most (usually the root).
     *
     * <p>Element at 0 is the top most node; if this tree is not rootless 0 element
     * is the root, otherwise is the top most parent node containing this node.</p>
     *
     * @return an array with this object and any parent tree node UI.
     */
    public ItsNatTreeCellUI[] getTreeNodeUIPath();

    /**
     * Returns the parent element containing this tree node.
     *
     * @return the parent element of this node.
     * @see ItsNatTreeUI#getParentElementFromRow(int)
     */
    public Element getParentElement();

    /**
     * Returns the content element of this tree node.
     *
     * @return the content element of this node.
     * @see ItsNatTreeUI#getContentElementFromRow(int)
     */
    public Element getContentElement();

    /**
     * Returns the handle element of this tree node.
     *
     * @return the handle element of this node.
     * @see ItsNatTreeUI#getHandleElementFromRow(int)
     */
    public Element getHandleElement();

    /**
     * Returns the icon element of this tree node.
     *
     * @return the icon element of this node.
     * @see ItsNatTreeUI#getIconElementFromRow(int)
     */
    public Element getIconElement();

    /**
     * Returns the label element of this tree node.
     *
     * @return the label element of this node.
     * @see ItsNatTreeUI#getLabelElementFromRow(int)
     */
    public Element getLabelElement();

    /**
     * Returns the zero based index of this node as a child relative to the parent.
     *
     * @return the index of this node relative to the parent. -1 if this node is a root or a top most node (rootless tree).
     */
    public int getIndex();

    /**
     * Returns the number of direct child nodes of this node.
     *
     * @return the number of child nodes.
     */
    public int getChildCount();

    /**
     * Returns the direct child tree node UI at the specified position.
     *
     * @param index zero based index of the child node position.
     * @return the child tree node.
     */
    public ItsNatTreeCellUI getChildItsNatTreeCellUIAt(int index);

    /**
     * Returns the row index of this node seeing the tree as a list. 0 if this node
     * is the root regardless this tree is rootless or not (if rootless never returns 0
     * because root node has not markup/tree node UI).
     *
     * @return the row index of this node seeing the tree as a list.
     * @see ItsNatTreeUI#getItsNatTreeCellUIFromRow(int)
     */
    public int getRow();

    /**
     * Sets the new expand state.
     *
     * <p>Current implementation does not perform DOM changes and only serves
     * to save expand state. Future versions might use this method to render
     * visually the markup effect of the expansion/collapse.</p>
     *
     * <p>Expand state of child or parent nodes is not affected.</p>
     *
     * @param expandState the new state.
     * @see #isExpanded()
     * @see ItsNatTree#expandNode(javax.swing.tree.TreePath)
     * @see ItsNatTree#collapseNode(javax.swing.tree.TreePath)
     */
    public void expand(boolean expandState);

    /**
     * Returns the current expand state.
     *
     * @return the current expand state, true by default.
     * @see #expand(boolean)
     * @see ItsNatTree#isExpandedNode(javax.swing.tree.TreePath)
     */
    public boolean isExpanded();

    /**
     * Returns the deep (number of parents) of this node. If this node is root
     * or a top most (rootless case) returns 0.
     *
     * @return the deep level of this node.
     */
    public int getDeepLevel();

    /**
     * Returns the tree path of this node.
     *
     * @return the tree path.
     */
    public TreePath getTreePath();
}
