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
 * Represents a pattern based DOM tree with a removable root. This structure
 * may be seen as a tree node list with none or only one tree node.
 *
 * <p>The starting point is a root DOM tree node, this node
 * is saved as the pattern (really a deep clone). This pattern is used
 * to recreate the root node (the root may be removed).</p>
 *
 * <p>The initial root tree node may be initially removed
 * or kept as is when this tree is created and attached to the underlying DOM.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ElementGroupManager#createElementTree(boolean,Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
 */
public interface ElementTree extends ElementGroup
{
    /**
     * Returns the current structure defined in this tree.
     *
     * @return the current structure.
     * @see #setElementTreeNodeStructure(ElementTreeNodeStructure)
     */
    public ElementTreeNodeStructure getElementTreeNodeStructure();

    /**
     * Sets the structure defined in this tree.
     *
     * @param structure the new structure.
     * @see #getElementTreeNodeStructure()
     */
    public void setElementTreeNodeStructure(ElementTreeNodeStructure structure);

    /**
     * Returns the current renderer defined in this tree.
     *
     * @return the current renderer.
     * @see #setElementTreeNodeRenderer(ElementTreeNodeRenderer)
     */
    public ElementTreeNodeRenderer getElementTreeNodeRenderer();

    /**
     * Sets the renderer defined in this tree.
     *
     * @param renderer the new renderer.
     * @see #getElementTreeNodeRenderer()
     */
    public void setElementTreeNodeRenderer(ElementTreeNodeRenderer renderer);

    /**
     * Returns the element used as a pattern. This element is a clone of the
     * original root node parent element used as a pattern.
     *
     * @return the pattern element.
     */
    public Element getRootPatternElement();

    /**
     * Returns the number of rows in the tree.
     *
     * @return the number of rows.
     */
    public int getRowCount();

    /**
     * Informs whether the current tree has a root tree node (not empty).
     *
     * @return true if the tree has a root tree node.
     * @see #getRootNode()
     */
    public boolean hasTreeNodeRoot();

    /**
     * Returns the root tree node.
     *
     * @return the root tree node or null if the tree is empty.
     * @see #addRootNode()
     * @see #removeRootNode()
     */
    public ElementTreeNode getRootNode();

    /**
     * Adds a root tree node. If the tree already has a root node an exception is thrown.
     *
     * <p>The new tree node uses the current structure and renderer of the tree.</p>
     *
     * @return the new root node.
     * @see #getRootNode()
     * @see #removeRootNode()
     */
    public ElementTreeNode addRootNode();

    /**
     * Adds a root tree node and renders the value using the
     * current structure and renderer.
     * If the tree already has a root node an exception is thrown.
     *
     * @return the new root node.
     * @see #addRootNode()
     * @see #getElementTreeNodeStructure()
     * @see #getElementTreeNodeRenderer()
     */
    public ElementTreeNode addRootNode(Object value);

    /**
     * Removes the current root tree node. If the tree has no root does nothing.
     *
     * @see #getRootNode()
     * @see #addRootNode()
     */
    public void removeRootNode();


    /**
     * Returns the tree node at the specified row position seeing the tree as a list (root node is 0).
     *
     * @param row the row position.
     * @return the tree node at the specified position or null if the tree is empty or row is out of bounds.
     */
    public ElementTreeNode getElementTreeNodeFromRow(int row);

    /**
     * Returns the tree node containing the specified node.
     *
     * @param node the node to search for.
     * @return the tree node containing the specified node. Null if not contained by this tree.
     */
    public ElementTreeNode getElementTreeNodeFromNode(Node node);


    /**
     * Informs whether the original (saved as pattern) markup is used to render.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatDocument#isUsePatternMarkupToRender()}</p>
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
