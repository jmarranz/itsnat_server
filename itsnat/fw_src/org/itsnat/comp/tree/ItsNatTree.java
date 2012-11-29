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
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.RowMapper;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.itsnat.comp.label.ItsNatLabel;

/**
 * Is the base interface of tree based components.
 *
 * <p>A tree component manages a <code>javax.swing.tree.TreeModel</code>
 * data model, tree node values are rendered as markup using a special object, the renderer,
 * and may be optionally edited "in place" using a user defined editor.</p>
 *
 * <p>Any change to the data model is notified to the component and the markup
 * is rendered again. The data model ever mandates over the markup,
 * any initial markup content (initial root node) is removed.</p>
 *
 * <p>Almost a root node must be present in the markup, this element is used as a pattern
 * to create new tree nodes, and is removed because by default the data model is empty.</p>
 *
 * <p>This component family uses a <code>javax.swing.tree.TreeSelectionModel</code> to keep
 * track of selection states. When a tree node is selected (usually by clicking it) the selection
 * state is updated accordingly using the selection model (this one fires any listener registered).</p>
 *
 * <p>There is no default decoration of tree node selection,
 * selection model listeners may be used to decorate the tree node markup when its
 * selection state changes.</p>
 *
 * <p>The component internally manages the expand/collapse behavior, when
 * a mouse event is received (by default one click on handler, double click
 * if icon or label) usually changes the expand/collapse state, this change
 * is notified sending a <code>javax.swing.event.TreeExpansionEvent</code>
 * to the registered listeners, these listeners must be used to manage how the expansion/collapsing
 * is rendered visually (by default the component does not modify the view,
 * does not force a default view behavior).</p>
 *
 * <p>By default this component uses the default renderer and editor and
 * a <code>javax.swing.tree.DefaultTreeModel</code> data model.</p>
 *
 * <p>Row indexes start in 0.</p>
 *
 * <p>This component family is the "componentized" version of {@link org.itsnat.core.domutil.ElementTree} and
 * follows a similar philosophy. </p>
 *
 * <p>ItsNat only provides "free trees" because
 * no &lt;tree&gt; tag is present in HTML.</p>
*
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatTree extends ItsNatElementComponent
{
    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatTreeUI getItsNatTreeUI();

    /**
     * Returns the current data model of this component.
     *
     * @return the current data model
     * @see #setTreeModel(javax.swing.tree.TreeModel)
     */
    public TreeModel getTreeModel();

    /**
     * Changes the data model of this component.
     *
     * <p>Current data model is disconnected from this component, and the new
     * data model is bound to this component, every change is tracked and
     * updates the user interfaces accordingly.</p>
     *
     * <p>If the specified data model is the same instance as the current data model,
     * then is reset, component listener is removed and added again. Use this technique if
     * you want to add a data model listener to be executed <i>before</i> the default component listener.
     *
     * @param dataModel the new data model.
     * @see #getTreeModel()
     */
    public void setTreeModel(TreeModel dataModel);

    /**
     * Informs whether this tree is a tree-table.
     *
     * <p>The tree-table mode only affects to the layout of tree nodes. If not
     * tree-table (normal) child nodes are contained by the parent tree node markup,
     * in a tree-table layout is as a list, all tree nodes are "top" level nodes
     * as whether they all were roots (only from a layout point of view).</p>
     *
     * <p>This mode is explicitly defined when the component is created
     * calling {@link org.itsnat.comp.ItsNatComponentManager#createItsNatFreeTree(org.w3c.dom.Element element,boolean,boolean,ItsNatTreeStructure,org.itsnat.core.NameValue[] artifacts)}
     * otherwise is defined by the artifact name "treeTable"
     * if defined or "treeTable" attribute (ItsNat namespace) if defined
     * else is false by default.</p>
     *
     * @return true if this tree has a tree-table layout.
     */
    public boolean isTreeTable();

    /**
     * Informs whether this tree has a visible root.
     *
     * <p>In a root-less tree the root node has no markup, direct child nodes
     * are top level nodes. This feature may be used to build trees with "multiple
     * roots" visually, this is very useful in vertical tree-based menus where
     * a root node is disturbing.</p>
     *
     * <p>This mode is explicitly defined when the component is created
     * calling {@link org.itsnat.comp.ItsNatComponentManager#createItsNatFreeTree(org.w3c.dom.Element element,boolean,boolean,ItsNatTreeStructure,org.itsnat.core.NameValue[] artifacts)}
     * otherwise is defined by the artifact name "rootless"
     * if defined or "rootless" attribute (ItsNat namespace) if defined
     * else is false by default.</p>
     *
     * @return true if root node has no markup.
     */
    public boolean isRootless();

    /**
     * Creates a data model instance appropriated to this component. This instance
     * is not bound to the component.
     *
     * @return a new data model instance.
     */
    public TreeModel createDefaultTreeModel();

    /**
     * Returns the number of mouse clicks needed to expand or close a node.
     *
     * <p>This number only counts for icon and label parts, the handle ever
     * expands/collapses the node with every click.</p>
     *
     * <p>Valid numbers: 0,1 and 2. If 0 icon and label do not expand/collapse the node when clicked.</p>
     *
     * @return number of mouse clicks to expand/collapse a node. By default is 2.
     * @see #setToggleClickCount(int)
     */
    public int getToggleClickCount();

    /**
     * Sets the number of mouse clicks needed to expand or close a node.
     *
     * @param toggleClickCount number of mouse clicks to expand/collapse a node. Valid numbers: 0,1 and 2.
     * @see #getToggleClickCount()
     */
    public void setToggleClickCount(int toggleClickCount);

    /**
     * Registers a new "tree will expand" listener. This listener is called <i>before</i>
     * a tree node is going to expand/collapse and can veto this action.
     *
     * @param tel the listener to register.
     * @see #removeTreeWillExpandListener(javax.swing.event.TreeWillExpandListener)
     */
    public void addTreeWillExpandListener(TreeWillExpandListener tel);

    /**
     * Unregisters the specified "tree will expand" listener.
     *
     * @param tel the listener to unregister.
     * @see #addTreeWillExpandListener(javax.swing.event.TreeWillExpandListener)
     */
    public void removeTreeWillExpandListener(TreeWillExpandListener tel);

    /**
     * Returns all registered tree "tree will expand" listeners
     *
     * @return an array with all registered "tree will expand" listeners
     */
    public TreeWillExpandListener[] getTreeWillExpandListeners();

    /**
     * Registers a new tree expansion listener. This listener is called <i>after</i>
     * a tree node is expanded/collapsed.
     *
     * @param tel the listener to register.
     * @see #removeTreeExpansionListener(javax.swing.event.TreeExpansionListener)
     */
    public void addTreeExpansionListener(TreeExpansionListener tel);

    /**
     * Unregisters the specified tree expansion listener.
     *
     * @param tel the listener to unregister.
     * @see #addTreeExpansionListener(javax.swing.event.TreeExpansionListener)
     */
    public void removeTreeExpansionListener(TreeExpansionListener tel);

    /**
     * Returns all registered "tree expansion" listeners
     *
     * @return an array with all registered "tree expansion" listeners
     */
    public TreeExpansionListener[] getTreeExpansionListeners();

    /**
     * Returns the component structure.
     *
     * @return the component structure.
     */
    public ItsNatTreeStructure getItsNatTreeStructure();

    /**
     * Returns the current component renderer. This renderer converts a tree node value to markup.
     *
     * @return the current renderer. By default uses the default renderer ({@link ItsNatComponentManager#createDefaultItsNatTreeCellRenderer()})
     * @see #setItsNatTreeCellRenderer(ItsNatTreeCellRenderer)
     */
    public ItsNatTreeCellRenderer getItsNatTreeCellRenderer();

    /**
     * Sets the component renderer.
     *
     * @param renderer the new renderer.
     * @see #getItsNatTreeCellRenderer()
     */
    public void setItsNatTreeCellRenderer(ItsNatTreeCellRenderer renderer);

    /**
     * Returns the current tree node label editor. This object is used to edit in place
     * a tree node value.
     *
     * @return the current editor. By default uses the default editor
     *      calling ({@link ItsNatComponentManager#createDefaultItsNatTreeCellEditor(ItsNatComponent)}) with a null parameter.
     * @see #setItsNatTreeCellEditor(ItsNatTreeCellEditor)
     */
    public ItsNatTreeCellEditor getItsNatTreeCellEditor();

    /**
     * Sets the tree node label editor.
     *
     * <p>Tree node edition works very much the same as label edition
     * (see {@link ItsNatLabel#setItsNatLabelEditor(ItsNatLabelEditor)}).</p>
     *
     * <p>Some differences:</p>
     *
     * <p>The edition process starts programmatically by calling {@link #startEditingAtPath(javax.swing.tree.TreePath)}
     * or {@link #startEditingAtRow(int)}.</p>
     *
     * <p>The edition takes place inside the tree node <i>label</i> element
     * as returned by {@link ItsNatTreeStructure#getLabelElement(ItsNatTree,int,org.w3c.dom.Element)}.</p>
     *
     * <p>The new tree node value is set to the data model
     * calling <code>javax.swing.tree.TreeModel.valueForPathChanged(javax.swing.tree.TreePath,Object)</code>.
     * </p>
     *
     * @param editor the new editor. May be null (edition disabled).
     * @see #getItsNatTreeCellEditor()
     */
    public void setItsNatTreeCellEditor(ItsNatTreeCellEditor editor);

    /**
     * Returns the current selection model.
     *
     * @return the current selection model. By default a <code>javax.swing.tree.DefaultTreeSelectionModel</code> instance.
     * @see #setTreeSelectionModel(javax.swing.tree.TreeSelectionModel)
     */
    public TreeSelectionModel getTreeSelectionModel();

    /**
     * Sets the new selection model.
     *
     * <p>If the new selection model is the current defined then is "reset",
     * component listener is removed and added again. Use this technique if
     * you want to add a listener to be executed <i>before</i> the default component listener.</p>
     *
     * <p>The component automatically replaces the current selection model <code>javax.swing.tree.RowMapper</code>
     * with the internal row mapper ({@link #getRowMapper()}) calling
     * <code>TreeSelectionModel.setRowMapper(RowMapper)</code>.</p>
     *
     * @param selectionModel the new selection model.
     * @see #getTreeSelectionModel()
     */
    public void setTreeSelectionModel(TreeSelectionModel selectionModel);

    /**
     * Returns the previous path seeing the data model as a list (tree order).
     *
     * <p>First tries to return the previous sibling, if no sibling then returns the parent.</p>
     *
     * @return the previous path or null if specified path is the root node.
     * @see #getNextPath(javax.swing.tree.TreePath)
     * @see #getPreviousSiblingPath(javax.swing.tree.TreePath)
     */
    public TreePath getPreviousPath(TreePath path);

    /**
     * Returns the path of the next node seeing the data model as a list (tree order).
     *
     * <p>First tries to return the first child, if no child returns the next sibling,
     * if no next sibling returns the next sibling of the parent node, if none
     * then the next sibling of the parent of the parent and so on.</p>
     *
     * @return the next path or null if this node is the last.
     * @see #getPreviousPath(javax.swing.tree.TreePath)
     * @see #getNextSiblingPath(javax.swing.tree.TreePath)
     */
    public TreePath getNextPath(TreePath path);

    /**
     * Returns the path of the previous sibling node.
     *
     * @return the previous sibling path or null if specified path is the first child node or is the root.
     * @see #getNextSiblingPath(javax.swing.tree.TreePath)
     */
    public TreePath getPreviousSiblingPath(TreePath path);

    /**
     * Returns the path of the next sibling node.
     *
     * @return the next sibling path or null if specified path is the last child node or is the root.
     * @see #getPreviousSiblingPath(javax.swing.tree.TreePath)
     */
    public TreePath getNextSiblingPath(TreePath path);

    /**
     * Returns the last node (tree order)
     *
     * @return the last node, null if the tree is empty.
     */
    public TreePath getLastPath();

    /**
     * Returns the number of nodes. This value is got traversing the data model.
     *
     * @return the number of nodes.
     */
    public int getTreeNodeCount();

    /**
     * Returns the <code>expandsSelectedPaths</code> property.
     * @return true if selection changes result in the parent path being expanded
     * @see #setExpandsSelectedPaths(boolean)
     */
    public boolean isExpandsSelectedPaths();

    /**
     * Sets the <code>expandsSelectedPaths</code> property. If
     * true, any time the selection is changed, either via the
     * <code>TreeSelectionModel</code>, or the cover methods provided by
     * this component like {@link #expandNode(javax.swing.tree.TreePath)},
     * the <code>TreePath</code>s parents will be
     * expanded too to make them visible (visible meaning the parent path is
     * expanded, not necessarily in the visible rectangle of the tree).
     * If false, when the selection changes the node parents are not expanded.
     * This is useful if you wish to have your selection model maintain selected paths
     * that are not always expanded (all parents expanded).
     *
     * @param newValue the new value for <code>expandsSelectedPaths</code>
     * @see #isExpandsSelectedPaths()
     */
    public void setExpandsSelectedPaths(boolean newValue);

    /**
     * Used to start programmatically a tree node edition process "in place".
     *
     * @param path the tree node path to edit.
     * @see #startEditingAtRow(int)
     * @see #isEditing()
     */
    public void startEditingAtPath(TreePath path);

    /**
     * Used to start programmatically a tree node edition process "in place".
     *
     * @param row the tree node row to edit.
     * @see #startEditingAtPath(javax.swing.tree.TreePath)
     * @see #isEditing()
     */
    public void startEditingAtRow(int row);

    /**
     * Informs whether a tree node value is being edited.
     *
     * @return true if a tree node item value is being edited.
     *
     * @see #startEditingAtPath(javax.swing.tree.TreePath)
     * @see #startEditingAtRow(int)
     */
    public boolean isEditing();

    /**
     * Returns the path of the tree node being edited.
     *
     * @return the path of the tree node item being edited. Null if none is being edited.
     */
    public TreePath getEditingPath();

    /**
     * Returns the row index of the tree node being edited.
     *
     * @return the row index of the tree node item being edited. -1 if none is being edited.
     */
    public int getEditingRow();

    /**
     * Returns the event type used to activate the tree node edition process by the user.
     *
     * <p>If returns null edition activated by events is disabled .</p>
     *
     * @return the event type used to activate the edition. By default is "dblclick".
     * @see #setEditorActivatorEvent(String)
     */
    public String getEditorActivatorEvent();

    /**
     * Sets the event type used to activate the tree node edition process by the user.
     *
     * @param eventType the event type used to activate the edition.
     * @see #getEditorActivatorEvent()
     */
    public void setEditorActivatorEvent(String eventType);

    /**
     * Informs whether the in place edition is enabled.
     *
     * @return false if the editing in place is temporally disabled. True by default.
     * @see #setEditingEnabled(boolean)
     */
    public boolean isEditingEnabled();

    /**
     * Enables or disables temporally the in place edition.
     *
     * @param value true to enable in place edition.
     * @see #isEditingEnabled()
     */
    public void setEditingEnabled(boolean value);

    /**
     * Informs whether the specified path is expanded.
     *
     * @param treePath the specified path.
     * @return true if the specified path is expanded, true by default.
     * @see org.itsnat.comp.tree.ItsNatTreeCellUI#isExpanded()
     */
    public boolean isExpandedNode(TreePath treePath);

    /**
     * Marks the specified node as expanded.
     *
     * <p>First of all a {@link javax.swing.event.TreeExpansionEvent} event
     * is sent to the registered {@link javax.swing.event.TreeWillExpandListener} listeners,
     * if expansion request is not vetoed then the node is marked as expanded and previous event is sent again
     * to the registered {@link javax.swing.event.TreeExpansionListener} listeners.</p>
     *
     * @param treePath the node path to expand.
     * @see #collapseNode(javax.swing.tree.TreePath)
     * @see org.itsnat.comp.tree.ItsNatTreeCellUI#expand(boolean)
     */
    public void expandNode(TreePath treePath);

    /**
     * Marks the specified node as collapsed.
     *
     * <p>Symmetric behavior is applied to collapse request than
     * {@link #expandNode(javax.swing.tree.TreePath)}.</p>
     *
     * @param treePath the node path to collapse.
     * @see #expandNode(javax.swing.tree.TreePath)
     * @see org.itsnat.comp.tree.ItsNatTreeCellUI#expand(boolean)
     */
    public void collapseNode(TreePath treePath);

    /**
     * Changes the expansion state of the specified node to the contrary.
     *
     * <p>Same behavior as {@link #expandNode(javax.swing.tree.TreePath)}
     * and {@link #collapseNode(javax.swing.tree.TreePath)}.</p>
     *
     * @param treePath the node path to change expansion state.
     */
    public void toggleExpansionStateNode(TreePath treePath);

    /**
     * Expands the specified node and node parents.
     *
     * @param path the path to expand.
     * @see #collapsePath(javax.swing.tree.TreePath)
     */
    public void expandPath(TreePath path);

    /**
     * Collapses the specified node and node parents.
     *
     * @param path the path to expand.
     * @see #expandPath(javax.swing.tree.TreePath)
     */
    public void collapsePath(TreePath path);

    /**
     * Returns the built-in row mapper.
     *
     * <p>This row mapper can not be replaced and is set automatically
     * to the registered selection model ({@link #setTreeSelectionModel(javax.swing.tree.TreeSelectionModel)}).</p>
     *
     * <p>Current implementation converts a <code>TreePath</code> to the matched row seeing
     * the tree as a list (root node is 0), where every node is "visible" (in a server point of view,
     * the same node in the client may be hidden).</p>
     *
     * @return the built-in row mapper.
     */
    public RowMapper getRowMapper();

    /**
     * Returns the row position of the specified path. This position is obtained
     * calling the built-in row mapper.
     *
     * @return the row position of the path.
     * @return #getRowMapper()
     */
    public int getRowForPath(TreePath path);

    /**
     * Returns the path of the node at the specified row position.
     *
     * <p>Current implementation specifies every node is visible (server point of view)
     * row-path conversion is straightforward seeing the tree as a list.</p>
     *
     * @param row the specified row position.
     * @return the path of the node at this row position.
     * @see #getRowMapper()
     */
    public TreePath getTreePathForRow(int row);

    /**
     * Returns if the keyboard is used to select items.
     *
     * <p>If false ItsNat simulates the CTRL key is ever pressed
     * when an item is selected.
     * </p>
     * <p>This feature is useful for mobile devices without keyboard and tactile
     * screens to select multiple items (if set to false).
     * </p>
     * <p>Default value is defined by {@link org.itsnat.comp.ItsNatComponentManager#isSelectionOnComponentsUsesKeyboard()}
     * </p>
     *
     * @return the current state.
     * @see #setSelectionUsesKeyboard(boolean)
     */
    public boolean isSelectionUsesKeyboard();

    /**
     * Sets if the keyboard is used to select items.
     *
     * @param value the new state.
     * @see #isSelectionUsesKeyboard()
     */
    public void setSelectionUsesKeyboard(boolean value);

    /**
     * Informs whether a joystick is enough to control the component
     * (some kind of mouse, pointer or stylus not present or not necessary).
     *
     * <p>If the component is in joystick mode, the handle, icon and label elements
     * of every tree node have an event listener associated. By this way tree nodes
     * are "live" elements and can traversed using a joystick in mobile devices
     * without a mouse, pointer or stylus.
     * </p>
     *
     * <p>Default value is defined by {@link org.itsnat.core.ItsNatDocument#isJoystickMode()}
     * </p>
     *
     * @return true if joystick mode is on.
     * @see #setJoystickMode(boolean)
     * @see ItsNatTreeStructure#getHandleElement(ItsNatTree, int, org.w3c.dom.Element)
     * @see ItsNatTreeStructure#getIconElement(ItsNatTree, int, org.w3c.dom.Element)
     * @see ItsNatTreeStructure#getLabelElement(ItsNatTree, int, org.w3c.dom.Element)
     */
    public boolean isJoystickMode();

    /**
     * Informs whether a joystick is enough to control the component
     * (some kind of mouse, pointer or stylus not present or not necessary).
     *
     * @param value true to enable joystick mode.
     * @see #isJoystickMode()
     */
    public void setJoystickMode(boolean value);
}
