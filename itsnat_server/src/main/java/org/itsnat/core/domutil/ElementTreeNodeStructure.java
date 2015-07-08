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

/**
 * Used by {@link ElementTreeNode} objects to obtain the required
 * element of the tree node markup.
 *
 * @see ElementGroupManager#createDefaultElementTreeNodeStructure()
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementTreeNodeStructure
{
    /**
     * Returns the content element of a tree node.
     *
     * <p>The intend of the content element is to contain the markup of handle,
     * icon and label, it does not contain the child nodes if any. Should be return null
     * if these constrains can not be accomplished (take account that handle and icon may be optional).</p>
     *
     * <p>Default implementation returns the first child element of the
     * effective tree node parent element if tree node is not part of a tree-table.
     * If a tree-table returns the first &lt;td&gt; if parent tree node element
     * is an HTML row otherwise returns the parent element (<code>treeNode</code>).</p>
     *
     * <p>The "effective" tree node parent element
     * differs from <code>treeNode</code> when this node is an HTML row, in this
     * case the effective node parent is the first child &lt;td&gt;</p>
     *
     * @param treeNode the target tree node.
     * @param nodeParent the element containing the tree node markup.
     *          Is a hint, if provided should be obtained by calling <code>treeNode.getParentElement()</code>.
     * @return the content element. May be null (rare case).
     */
    public Element getContentElement(ElementTreeNode treeNode,Element nodeParent);

    /**
     * Returns the handle element of a tree node.
     *
     * <p>The handle element is the element containing the markup of the tree node handle,
     * this handle is usually used to control "folders".</p>
     *
     * <p>Default implementation returns the first child element of the content
     * element or null if no handle is detected in the content markup.</p>
     *
     * @param treeNode the target tree node.
     * @param nodeParent the element containing the tree node markup.
     *          Is a hint, if provided should be obtained by calling <code>treeNode.getParentElement()</code>.
     * @return the handle element. May be null (no handle).
     */
    public Element getHandleElement(ElementTreeNode treeNode,Element nodeParent);

    /**
     * Returns the icon element of a tree node.
     *
     * <p>The icon element is the element containing the markup of the tree node icon,
     * this icon is usually used to show the type of node (a folder a final leaf etc).</p>
     *
     * <p>Default implementation returns the next sibling of the handle
     * element or null if no icon is detected in the content markup.</p>
     *
     * @param treeNode the target tree node.
     * @param nodeParent the element containing the tree node markup.
     *          Is a hint, if provided should be obtained by calling <code>treeNode.getParentElement()</code>.
     * @return the icon element. May be null (no icon).
     */
    public Element getIconElement(ElementTreeNode treeNode,Element nodeParent);

    /**
     * Returns the label element of a tree node.
     *
     * <p>The label element is the element containing the markup of the tree node text info.</p>
     *
     * <p>Default implementation returns the next sibling of the icon
     * element, or the content element itself if no icon and handle is detected.</p>
     *
     * @param treeNode the target tree node.
     * @param nodeParent the element containing the tree node markup.
     *          Is a hint, if provided should be obtained by calling <code>treeNode.getParentElement()</code>.
     * @return the label element. May be null (rare case).
     */
    public Element getLabelElement(ElementTreeNode treeNode,Element nodeParent);

    /**
     * Returns the element containing the child tree nodes of the specified tree node.
     *
     * <p>Default implementation returns null if the tree node is part
     * of a tree-table, otherwise returns the next sibling of the content
     * element.</p>
     *
     * <p>If not a tree-table and a null is returned, then
     * the tree node cannot have child nodes.</p>
     *
     * @param treeNode the target tree node.
     * @param nodeParent the element containing the tree node markup.
     *          Is a hint, if provided should be obtained by calling <code>treeNode.getParentElement()</code>.
     * @return the element containing the child list. May be null (is a tree-table or tree node has not child nodes).
     */
    public Element getChildListElement(ElementTreeNode treeNode,Element nodeParent);
}
