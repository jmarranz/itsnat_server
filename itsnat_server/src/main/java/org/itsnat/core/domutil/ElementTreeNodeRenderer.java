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
 * Used by {@link ElementTreeNode} objects to render the value
 * associated to the tree node.
 *
 *
 * @author Jose Maria Arranz Santamaria
 * @see ElementTreeNode#setElementTreeNodeRenderer(ElementTreeNodeRenderer)
 * @see ElementGroupManager#createDefaultElementTreeNodeRenderer()
 */
public interface ElementTreeNodeRenderer
{
    /**
     * Renders as markup the specified value into the specified tree node.
     *
     * <p>Default implementation renders the specified value inside the first text node found below the
     * label element.</p>
     *
     * @param treeNode the target tree node.
     * @param value the value to render.
     * @param labelElem the label element to render the value into. Is a hint, if provided should be obtained by calling {@link ElementTreeNode#getLabelElement()}.
     * @param isNew true if this is the first time the markup is rendered. Default implementation ignores this parameter.
     */
    public void renderTreeNode(ElementTreeNode treeNode,Object value,Element labelElem,boolean isNew);

    /**
     * Unrenders the markup of the specified tree node. This method is called <i>before</i> the markup
     * is removed.
     *
     * <p>Default implementation does nothing.</p>
     *
     * @param treeNode the target tree node.
     * @param labelElem the label element to render the value into. Is a hint, if provided should be obtained by calling {@link ElementTreeNode#getLabelElement()}.
     */
    public void unrenderTreeNode(ElementTreeNode treeNode,Element labelElem);
}
