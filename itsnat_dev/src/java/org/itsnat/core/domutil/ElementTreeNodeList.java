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

package org.itsnat.core.domutil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Represents a pattern based DOM tree node list.
 *
 * <p>The starting point may be a tree node list with almost a child tree node,
 * this first child element is saved as the pattern (really a deep clone).
 * If the list is initially empty the pattern used is the pattern of the parent tree node.
 * New child tree nodes in the list are created using this pattern.
 * The initial tree node list (including the pattern) may be initially cleared
 * or kept as is when this object is created and attached to the underlying DOM list.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ElementGroupManager#createElementTreeNodeList(boolean,Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
 * @see ElementTreeNode#getChildTreeNodeList()
 */
public interface ElementTreeNodeList extends ElementGroup
{
    /**
     * Returns the current structure defined in this tree node list.
     *
     * @return the current structure.
     * @see #setElementTreeNodeStructure(ElementTreeNodeStructure)
     */
    public ElementTreeNodeStructure getElementTreeNodeStructure();

    /**
     * Sets the structure defined in this tree node list.
     *
     * @param structure the new structure.
     * @see #getElementTreeNodeStructure()
     */
    public void setElementTreeNodeStructure(ElementTreeNodeStructure structure);

    /**
     * Returns the current renderer defined in this tree node list.
     *
     * @return the current renderer.
     * @see #setElementTreeNodeRenderer(ElementTreeNodeRenderer)
     */
    public ElementTreeNodeRenderer getElementTreeNodeRenderer();

    /**
     * Sets the renderer defined in this tree node list.
     *
     * @param renderer the new renderer.
     * @see #getElementTreeNodeRenderer()
     */
    public void setElementTreeNodeRenderer(ElementTreeNodeRenderer renderer);

    /**
     * Returns the element used as a pattern. This element is a clone of the
     * original first child tree node (if the pattern was obtained with).
     *
     * @return the pattern element.
     */
    public Element getChildPatternElement();

    /**
     * Informs whether the tree node list is empty.
     *
     * @return true if the tree node list is empty.
     */
    public boolean isEmpty();

    /**
     * Removes the specified tree node.
     *
     * @param index index of the child tree node to remove.
     * @return the removed tree node or null if index is out of bounds.
     */
    public ElementTreeNode removeTreeNodeAt(int index);

    /**
     * Removes the tree nodes between the specified indexes.
     *
     * @param fromIndex low index (inclusive).
     * @param toIndex high index (inclusive).
     */
    public void removeTreeNodeRange(int fromIndex, int toIndex);

    /**
     * Removes all tree nodes. The tree node list is now empty.
     */
    public void removeAllTreeNodes();

    /**
     * Returns the tree node list as an array.
     *
     * @return the tree node array.
     */
    public ElementTreeNode[] getTreeNodes();

    /**
     * Returns the tree node at the specified index.
     *
     * @param index index of the tree node to search.
     * @return the tree node in this position or null if index is out of range.
     */
    public ElementTreeNode getTreeNodeAt(int index);

    /**
     * Returns the first child tree node (position 0).
     *
     * @return the first child tree node or null is the list is empty.
     */
    public ElementTreeNode getFirstTreeNode();

    /**
     * Returns the last child tree node (position <code>getLength() - 1</code>).
     *
     * @return the last child tree node or null is the list is empty.
     */
    public ElementTreeNode getLastTreeNode();

    /**
     * Returns the tree node containing the specified node. The search is performed
     * in the full subtree, so the returned tree node
     * may not be a direct child node (may be a tree node child of a tree node list child and so on).
     *
     * @param node the node to search for.
     * @return the tree node containing the specified node. Null if this node is not contained by the list.
     */
    public ElementTreeNode getElementTreeNodeFromNode(Node node);

    /**
     * Returns the number of child tree nodes in the list.
     *
     * @return the list size.
     * @see #setLength(int)
     */
    public int getLength();

    /**
     * Returns the number of rows in the list.
     *
     * @return the number of rows.
     */
    public int getRowCount();

    /**
     * Increases or shrinks the list to fit the new size.
     *
     * <p>If the new size is bigger new tree nodes are added at the end, if the size
     * is lower tail tree nodes are removed.</p>
     *
     * @param len the new length.
     * @see #getLength()
     * @see #addTreeNode()
     * @see #removeTreeNodeAt(int)
     */
    public void setLength(int len);

    /**
     * Adds a new child tree node at the end of the list using the pattern (the new tree node markup is a clone).
     *
     * <p>The new tree node uses the current structure and renderer of the list.</p>
     *
     * @return the new tree node.
     * @see #addTreeNode(Object)
     */
    public ElementTreeNode addTreeNode();

    /**
     * Adds a new child tree node at the end of the list and renders the specified value using
     * the current structure and renderer.
     *
     * @param value the value to render.
     * @return the new tree node.
     * @see #addTreeNode()
     * @see #getElementTreeNodeStructure()
     * @see #getElementTreeNodeRenderer()
     */
    public ElementTreeNode addTreeNode(Object value);

    /**
     * Inserts a new child tree node at the specified position using the pattern (the new tree node markup is a clone).
     *
     * <p>The new tree node uses the current structure and renderer of the list.</p>
     *
     * @param index index of the new tree node.
     * @return the new tree node.
     * @see #insertTreeNodeAt(int,Object)
     */
    public ElementTreeNode insertTreeNodeAt(int index);

    /**
     * Inserts a new child tree node at the specified position and renders the specified value using
     * the current structure and renderer.
     *
     * @param index index of the tree node.
     * @param value the value to render.
     * @return the new tree node.
     * @see #insertTreeNodeAt(int)
     * @see #getElementTreeNodeStructure()
     * @see #getElementTreeNodeRenderer()
     */
    public ElementTreeNode insertTreeNodeAt(int index,Object value);


    /**
     * Informs whether the original (saved as pattern) markup is used to render.
     *
     * <p>The default value is defined by the parent tree node if exists or
     * by {@link org.itsnat.core.ItsNatDocument#isUsePatternMarkupToRender()}</p>
     *
     * @return true if the original markup is used.
     * @see #setUsePatternMarkupToRender(boolean)
     */
    public boolean isUsePatternMarkupToRender();

    /**
     * Sets whether the original (saved as pattern) markup is used to render.
     *
     * @param value true to enable the use of original markup to render.
     * @see #isUsePatternMarkupToRender()
     */
    public void setUsePatternMarkupToRender(boolean value);

}
