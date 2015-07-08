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

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Represents a pattern based DOM tree node. A tree node contains (may contain)
 * a handle, an icon and a label elements (these three are the node "content"),
 * and may have child nodes. The tree node structure is used as pattern to create child tree nodes (by using a deep clone),
 * so all child tree nodes have the same structure (same as the pattern), unless
 * there is one child node in markup.
 *
 * <p>This interface supports "out the box" the typical DOM tree node structure
 * with handle, icon and label elements:</p>
 *
 * <pre>
 *   &lt;parent>
 *     &lt;content>&lt;handle/>&lt;icon/>&lt;label>markup of label&lt;/label>&lt;/content>
 *     &lt;children>
 *      ...child tree nodes go here
 *     &lt;/children>
 *   &lt;/parent>
 * </pre>
 *
 * <p>The label element is normally used to render below the value associated to node.
 * The method {@link #setValue(Object)} is used to render this value, the structure and renderer
 * objects are used to customize how and where this value is saved in the tree node
 * beyond the default cases.</p>
 *
 * <p>A pattern based DOM tree node ever works in "master" mode, see {@link ElementListFree#isMaster()}.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ElementGroupManager#createElementTreeNode(org.w3c.dom.Element,boolean)
 */
public interface ElementTreeNode extends ElementGroup
{
    /**
     * Returns the current structure used by this tree node.
     *
     * @return the current structure.
     */
    public ElementTreeNodeStructure getElementTreeNodeStructure();

    /**
     * Returns the current renderer used by this tree node.
     *
     * @return the current renderer.
     */
    public ElementTreeNodeRenderer getElementTreeNodeRenderer();

    /**
     * Sets the renderer used by this tree node.
     *
     * @param renderer the new renderer.
     */
    public void setElementTreeNodeRenderer(ElementTreeNodeRenderer renderer);

    /**
     * Returns the number of rows below this tree node including itself.
     *
     * @return the number of rows. Min value is 1 (this node).
     */
    public int getRowCount();

    /**
     * Returns the parent tree node if exists.
     *
     * @return the parent tree node, null if this node is root or rootless tree.
     */
    public ElementTreeNode getElementTreeNodeParent();

    /**
     * Returns the content element. This element usually contains the handle, icon and label elements.
     * The current structure is used to obtain this element.
     *
     * @return the content element.
     * @see #getElementTreeNodeStructure()
     * @see ElementTreeNodeStructure#getContentElement(ElementTreeNode,Element)
     */
    public Element getContentElement();

    /**
     * Returns the handle element. The current structure is used to obtain this element.
     *
     * @return the handle element.
     * @see #getElementTreeNodeStructure()
     * @see ElementTreeNodeStructure#getHandleElement(ElementTreeNode,Element)
     */
    public Element getHandleElement();

    /**
     * Returns the icon element. The current structure is used to obtain this element.
     *
     * @return the icon element.
     * @see #getElementTreeNodeStructure()
     * @see ElementTreeNodeStructure#getIconElement(ElementTreeNode,Element)
     */
    public Element getIconElement();

    /**
     * Returns the label element. The current structure is used to obtain this element.
     *
     * @return the label element.
     * @see #getElementTreeNodeStructure()
     * @see ElementTreeNodeStructure#getLabelElement(ElementTreeNode,Element)
     */
    public Element getLabelElement();

    /**
     * Returns the parent element of the child tree nodes. The current structure is used to obtain this element.
     *
     * @return the parent of child nodes.
     * @see #getElementTreeNodeStructure()
     * @see ElementTreeNodeStructure#getChildListElement(ElementTreeNode,Element)
     */
    public Element getChildListElement();

    /**
     * Renders the submitted value into the tree node markup using the current
     * renderer.
     *
     * @param value the value to render.
     * @see #getElementTreeNodeRenderer()
     * @see ElementTreeNodeRenderer#renderTreeNode(ElementTreeNode,Object,Element,boolean)
     */
    public void setValue(Object value);

    /**
     * Returns the index of this node as a child of its node parent.
     *
     * @return the position as child, -1 if root.
     * @see #getElementTreeNodeParent()
     */
    public int getIndex();

    /**
     * Returns the row index of this node seeing the tree as a list.
     *
     * @return the row index, 0 if root.
     */
    public int getRow();

    /**
     * Returns the number of levels (parents) above.
     *
     * @return the number of levels, 0 if root or a child node of the root list in a rootless tree.
     */
    public int getDeepLevel();

    /**
     * Informs whether this node is a leaf, it does not contain child nodes.
     *
     * @return true if this node is a leaf.
     */
    public boolean isLeaf();


    /**
     * Returns this tree node or the tree node containing the specified node.
     *
     * @param node the node to search for.
     * @return the tree node containing the specified node. Null if not contained by this tree node or child tree nodes.
     */
    public ElementTreeNode getElementTreeNodeFromNode(Node node);

    /**
     * Returns the child tree node list of this tree node.
     *
     * @return the child tree node list
     */
    public ElementTreeNodeList getChildTreeNodeList();

    /**
     * Returns the previous tree node in the logical order of the tree
     * (seen as a list).
     *
     * @return the previous tree node.
     */
    public ElementTreeNode getPreviousTreeNode();

    /**
     * Returns the next tree node in the logical order of the tree
     * (seen as a list).
     *
     * @return the next tree node.
     */
    public ElementTreeNode getNextTreeNode();

    /**
     * Returns the tree node immediately preceding this tree node (same level).
     *
     * @return the previous sibling or null if there is no such tree node.
     */
    public ElementTreeNode getPreviousSiblingTreeNode();

    /**
     * Returns the tree node immediately following this tree node (same level).
     *
     * @return the next sibling or null if there is no such tree node.
     */
    public ElementTreeNode getNextSiblingTreeNode();

    /**
     * Informs whether this node is part of a tree table.
     *
     * @return true if this node is part of a tree table.
     */
    public boolean isTreeTable();

    /**
     * Informs whether the original (saved as pattern) markup is used to render.
     *
     * <p>The default value is defined by the parent tree node list if exists or
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

    /**
     * Returns the pattern used to render values if {@link #isUsePatternMarkupToRender()}
     * is true.
     *
     * @return the pattern used to render values.
     */
    public DocumentFragment getLabelContentPatternFragment();
}
