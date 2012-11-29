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

import org.itsnat.comp.ItsNatElementComponentUI;
import javax.swing.tree.TreePath;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Is the base interface of the User Interface of a tree component.
 *
 * <p>Tree nodes are managed using the current tree structure
 * and renderer.</p>
 *
 * <p>Current implementation uses the tree data model when necessary and relays heavily on
 * {@link org.itsnat.core.domutil.ElementTree} and related interfaces/objects.</p>
 *
 * <p>If the tree is visually "rootless" there is no markup to the root but the root
 * is ever counted for indexing as row 0 (including when there is no root in data model).
 * Method calls like <code>getParentElementFromRow(0)</code> returns null.
 * This guaranties same row values for trees visually "rootless" and
 * with visible root.</p>
 *
 * <p>Current implementation does not use the data model on methods not containing
 * <code>TreePath</code> parameters.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatTree#getItsNatTreeUI()
 * @see ItsNatTree#getItsNatTreeCellRenderer()
 * @see ItsNatTree#getItsNatTreeStructure()
 */
public interface ItsNatTreeUI extends ItsNatElementComponentUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatTree getItsNatTree();

    /**
     * Returns the number of rows in the tree. If the tree is rootless
     * the root node is ever counted (including when there is no root
     * in data model).
     *
     * @return the number of rows. If rootless min value returned is 1.
     */
    public int getRowCount();

    /**
     * Returns the row index of the specified tree node.
     *
     * @param path the path of the tree node.
     * @return the row index of the specified tree node.
     */
    public int getRow(TreePath path);

    /**
     * Returns the number of rows contained below the specified node including itself.
     *
     * <p>This method uses the data model (to locate the specified tree node) but can be called including when
     * the child nodes are missing in the data model (for instance were removed before).
     * </p>
     *
     * @param path the path of the tree node.
     * @return the number of rows.
     */
    public int getRowCount(TreePath path);

    /**
     * Returns the row index of the child tree node at the specified position below the parent tree node
     * specified by its path.
     *
     * <p>This method uses the data model (to locate the parent) but can be called including when
     * the specified child node is missing in the data model (for instance was removed before).
     * </p>
     *
     * @param index 0 based index of the child tree node relative to the parent.
     * @param parentPath the path of the parent tree node.
     * @return the row index of the specified child node.
     */
    public int getRow(int index,TreePath parentPath);

    /**
     * Returns the number of rows contained in the child tree node at the specified position below the parent tree node
     * specified by its path. The child node is counted too.
     *
     * <p>This method uses the data model (to locate the parent) but can be called including when
     * the specified child node is missing in the data model (for instance was removed before).
     * </p>
     *
     * @param index 0 based index of the child tree node relative to the parent.
     * @param parentPath the path of the parent tree node.
     * @return the row index of the specified child node.
     */
    public int getRowCount(int index,TreePath parentPath);

    /**
     * Returns the parent element of the tree node at the specified row position seeing the tree as a list (root node is 0).
     *
     * @param row the row position.
     * @return the parent element of the tree node at the specified position or null if the tree is empty or row is out of bounds.
     * @see ItsNatTreeCellUI#getParentElement()
     * @see org.itsnat.core.domutil.ElementGroup#getParentElement()
     */
    public Element getParentElementFromRow(int row);

    /**
     * Returns the content element of the tree node at the specified row position seeing the tree as a list (root node is 0).
     *
     * @param row the row position.
     * @return the content element of the tree node at the specified position or null if the tree is empty or row is out of bounds.
     * @see ItsNatTreeCellUI#getContentElement()
     * @see ItsNatTreeStructure#getContentElement(ItsNatTree,int,Element)
     * @see org.itsnat.core.domutil.ElementTreeNode#getContentElement()
     */
    public Element getContentElementFromRow(int row);

    /**
     * Returns the handle element of the tree node at the specified row position seeing the tree as a list (root node is 0).
     *
     * @param row the row position.
     * @return the handle element of the tree node at the specified position or null if the tree is empty or row is out of bounds.
     * @see ItsNatTreeCellUI#getHandleElement()
     * @see ItsNatTreeStructure#getHandleElement(ItsNatTree,int,Element)
     * @see org.itsnat.core.domutil.ElementTreeNode#getHandleElement()
     */
    public Element getHandleElementFromRow(int row);

    /**
     * Returns the icon element of the tree node at the specified row position seeing the tree as a list (root node is 0).
     *
     * @param row the row position.
     * @return the icon element of the tree node at the specified position or null if the tree is empty or row is out of bounds.
     * @see ItsNatTreeCellUI#getIconElement()
     * @see ItsNatTreeStructure#getIconElement(ItsNatTree,int,Element)
     * @see org.itsnat.core.domutil.ElementTreeNode#getIconElement()
     */
    public Element getIconElementFromRow(int row);

    /**
     * Returns the label element of the tree node at the specified row position seeing the tree as a list (root node is 0).
     *
     * @param row the row position.
     * @return the label element of the tree node at the specified position or null if the tree is empty or row is out of bounds.
     * @see ItsNatTreeCellUI#getLabelElement()
     * @see ItsNatTreeStructure#getLabelElement(ItsNatTree,int,Element)
     * @see org.itsnat.core.domutil.ElementTreeNode#getLabelElement()
     */
    public Element getLabelElementFromRow(int row);

    /**
     * Returns the parent element of the tree node specified by the tree path.
     *
     * @param path the tree node path.
     * @return the parent element of the tree node.
     * @see #getParentElementFromRow(int)
     */
    public Element getParentElementFromTreePath(TreePath path);

    /**
     * Returns the content element of the tree node specified by the tree path.
     *
     * @param path the tree node path.
     * @return the content element of the tree node.
     * @see #getContentElementFromRow(int)
     */
    public Element getContentElementFromTreePath(TreePath path);

    /**
     * Returns the handle element of the tree node specified by the tree path.
     *
     * @param path the tree node path.
     * @return the handle element of the tree node.
     * @see #getHandleElementFromRow(int)
     */
    public Element getHandleElementFromTreePath(TreePath path);

    /**
     * Returns the icon element of the tree node specified by the tree path.
     *
     * @param path the tree node path.
     * @return the icon element of the tree node.
     * @see #getIconElementFromRow(int)
     */
    public Element getIconElementFromTreePath(TreePath path);

    /**
     * Returns the label element of the tree node specified by the tree path.
     *
     * @param path the tree node path.
     * @return the label element of the tree node.
     * @see #getLabelElementFromRow(int)
     */
    public Element getLabelElementFromTreePath(TreePath path);

    /**
     * Returns the object info of the tree node at the specified row position seeing the tree as a list (root node is 0).
     *
     * @param row the row position.
     * @return the object info of the tree node at the specified position or null
     *          if the tree is empty or row is out of bounds.
     *          This object is ever the same per tree node and may be used to save any context data.
     * @see org.itsnat.core.domutil.ElementTree#getElementTreeNodeFromRow(int)
     */
    public ItsNatTreeCellUI getItsNatTreeCellUIFromRow(int row);

    /**
     * Returns the object info of the tree node specified by the tree path.
     *
     * @param path the tree node path.
     * @return the object info of the tree node.
     *          This object is ever the same per tree node and may be used to save any context data.
     * @see #getItsNatTreeCellUIFromRow(int)
     */
    public ItsNatTreeCellUI getItsNatTreeCellUIFromTreePath(TreePath path);

    /**
     * Returns the object info of the tree node containing the specified node.
     *
     * @param node the node to search for.
     * @return the tree node containing the specified node. Null if not contained by this tree.
     *          This object is ever the same per tree node and may be used to save any context data.
     * @see org.itsnat.core.domutil.ElementTree#getElementTreeNodeFromNode(Node)
     */
    public ItsNatTreeCellUI getItsNatTreeCellUIFromNode(Node node);

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
