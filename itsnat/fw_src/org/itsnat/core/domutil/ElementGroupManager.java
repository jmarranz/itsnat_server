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

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Element;

/**
 * Utility interface used as factory of {@link ElementGroup} objects
 * and related resources.
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatDocument#getElementGroupManager()
 */
public interface ElementGroupManager
{
    /**
     * Returns the parent document this object belongs to.
     *
     * @return the parent document.
     */
    public ItsNatDocument getItsNatDocument();

    /**
     * Creates a DOM element list manager. Initial DOM list may be not empty
     * and child elements may have different tag names.
     *
     * @param parentElement the parent element of the list.
     * @param master if true only the manager must be used to make structural changes (add, remove or replace elements), avoiding direct DOM manipulation.
     * @return the element list.
     * @see #createElementList(org.w3c.dom.Element,boolean,org.itsnat.core.domutil.ElementListStructure,org.itsnat.core.domutil.ElementListRenderer)
     */
    public ElementListFree createElementListFree(Element parentElement,boolean master);

    /**
     * Creates a DOM element table manager. Initial DOM table may be not empty
     * and row elements and cells may have different tag names.
     *
     * @param parentElement the parent element of the table.
     * @param master if true only the manager must be used to make structural changes (add, remove or replace elements), avoiding direct DOM manipulation.
     * @return the element table.
     * @see #createElementTable(org.w3c.dom.Element,boolean,org.itsnat.core.domutil.ElementTableStructure,org.itsnat.core.domutil.ElementTableRenderer)
     */
    public ElementTableFree createElementTableFree(Element parentElement,boolean master);

    /**
     * Creates a pattern based DOM element label. The content of the element is used as the pattern to render the first
     * value.
     *
     * @param parentElement the parent element of the label.
     * @param removePattern if true the current content is removed, otherwise the label shows the current content.
     * @param renderer the used label renderer. If null the default renderer is used.
     * @return the element label.
     */
    public ElementLabel createElementLabel(Element parentElement,boolean removePattern,ElementLabelRenderer renderer);

    /**
     * Creates a pattern based DOM element list. The first child element is used as the pattern to create new
     * child elements.
     *
     * @param parentElement the parent element of the list.
     * @param removePattern if true the current content (the pattern as minimun) is removed, otherwise the list represents the current content.
     * @param structure the used list structure. If null the default structure is used.
     * @param renderer the used list renderer. If null the default renderer is used.
     * @return the element list.
     * @see #createElementList(org.w3c.dom.Element,boolean)
     * @see #createElementListFree(Element,boolean)
     */
    public ElementList createElementList(Element parentElement,boolean removePattern,ElementListStructure structure,ElementListRenderer renderer);

    /**
     * Creates a pattern based DOM element list. The first child element is used as the pattern to create new
     * child elements. The list uses the default structure and renderer.
     *
     * @param parentElement the parent element of the list.
     * @param removePattern if true the current content (the pattern as minimun) is removed, otherwise the list represents the current content.
     * @return the element list.
     * @see #createElementList(org.w3c.dom.Element,boolean,org.itsnat.core.domutil.ElementListStructure,org.itsnat.core.domutil.ElementListRenderer)
     * @see #createDefaultElementListStructure()
     * @see #createDefaultElementListRenderer()
     */
    public ElementList createElementList(Element parentElement,boolean removePattern);

    /**
     * Creates a pattern based DOM element table. The first child element is used as the row pattern and the first
     * element cell is the cell pattern used to create new cells (columns).
     *
     * @param parentElement the parent element of the table.
     * @param removePattern if true the current content (the first row as minimu) is removed, otherwise the table represents the current content.
     * @param structure the used table structure. If null the default structure is used.
     * @param renderer the used table renderer. If null the default renderer is used.
     * @return the element table.
     * @see #createElementTable(org.w3c.dom.Element,boolean)
     * @see #createElementTableFree(Element,boolean)
     */
    public ElementTable createElementTable(Element parentElement,boolean removePattern,ElementTableStructure structure,ElementTableRenderer renderer);

    /**
     * Creates a pattern based DOM element table. The first child element is used as the row pattern and the first
     * element cell is the cell pattern used to create new cells (columns).
     * The table uses the default structure and renderer.
     *
     * @param parentElement the parent element of the table.
     * @param removePattern if true the current content (the first row as minimu) is removed, otherwise the table represents the current content.
     * @return the element table.
     * @see #createElementTable(org.w3c.dom.Element,boolean,org.itsnat.core.domutil.ElementTableStructure,org.itsnat.core.domutil.ElementTableRenderer)
     * @see #createDefaultElementTableStructure()
     * @see #createDefaultElementTableRenderer()
     */
    public ElementTable createElementTable(Element parentElement,boolean removePattern);

    /**
     * Creates a pattern based DOM element tree node. This node is the fixed root of the tree.
     *
     * @param parentElement the parent element of the node and tree.
     * @param removePattern if true the pattern of child nodes (the first child if any) is removed, otherwise the child node list represents the current child node list.
     * @param structure the used tree node structure. If null the default structure is used.
     * @param renderer the used tree node renderer. If null the default renderer is used.
     * @return the element tree node.
     * @see #createElementTreeNode(org.w3c.dom.Element,boolean)
     * @see #createElementTree(boolean,Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
     * @see #createElementTreeNodeList(boolean,Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
     */
    public ElementTreeNode createElementTreeNode(Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer);

    /**
     * Creates a pattern based DOM element tree node. This node is the fixed (non-removable) root of the tree.
     * The tree node uses the default structure and renderer.
     *
     * @param parentElement the parent element of the node and tree.
     * @param removePattern if true the pattern of child nodes (the first child if any) is removed, otherwise the child node list represents the current child node list.
     * @return the element tree node.
     * @see #createElementTreeNode(org.w3c.dom.Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
     * @see #createDefaultElementTreeNodeStructure()
     * @see #createDefaultElementTreeNodeRenderer()
     */
    public ElementTreeNode createElementTreeNode(Element parentElement,boolean removePattern);

    /**
     * Creates a pattern based DOM element tree. The root node is the only one top level node
     * and is not fixed (removable).
     *
     * @param treeTable if true the tree is a tree-table structurally.
     * @param parentElement the parent element of the tree.
     * @param removePattern if true the first node (the root node used as pattern) is removed, otherwise this node is the root.
     * @param structure the used tree node structure. If null the default structure is used.
     * @param renderer the used tree node renderer. If null the default renderer is used.
     * @return the element tree.
     * @see #createElementTree(boolean,Element,boolean)
     * @see #createElementTreeNode(org.w3c.dom.Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
     * @see #createElementTreeNodeList(boolean,Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
     */
    public ElementTree createElementTree(boolean treeTable,Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer);

    /**
     * Creates a pattern based DOM element tree. The root node is the only one top level node
     * and is not fixed (removable). The tree uses the default structure and renderer.
     *
     * @param treeTable if true the tree is a tree-table structurally.
     * @param parentElement the parent element of the tree.
     * @param removePattern if true the first tree node (used as pattern) is removed, otherwise this node is the root.
     * @return the element tree.
     * @see #createElementTree(boolean,Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
     * @see #createDefaultElementTreeNodeStructure()
     * @see #createDefaultElementTreeNodeRenderer()
     */
    public ElementTree createElementTree(boolean treeTable,Element parentElement,boolean removePattern);

    /**
     * Creates a pattern based rootless DOM element tree.
     *
     * @param treeTable if true the tree is a tree-table structurally.
     * @param parentElement the parent element of the tree.
     * @param removePattern if true the first child node is removed, otherwise the tree represents the current content (current top child nodes).
     * @param structure the used tree node structure. If null the default structure is used.
     * @param renderer the used tree node renderer. If null the default renderer is used.
     * @return the rootless element tree.
     * @see #createElementTreeNodeList(boolean,Element,boolean)
     * @see #createElementTreeNode(org.w3c.dom.Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
     * @see #createElementTree(boolean,Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
     */
    public ElementTreeNodeList createElementTreeNodeList(boolean treeTable,Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer);

    /**
     * Creates a pattern based rootless DOM element tree. The tree uses the default structure and renderer.
     *
     * @param treeTable if true the tree is a tree-table structurally.
     * @param parentElement the parent element of the tree.
     * @param removePattern if true the first child node is removed, otherwise the tree represents the current content (current top child nodes).
     * @return the rootless element tree.
     * @see #createElementTreeNodeList(boolean,Element,boolean,ElementTreeNodeStructure,ElementTreeNodeRenderer)
     * @see #createDefaultElementTreeNodeStructure()
     * @see #createDefaultElementTreeNodeRenderer()
     */
    public ElementTreeNodeList createElementTreeNodeList(boolean treeTable,Element parentElement,boolean removePattern);


    /**
     * Creates the default DOM element renderer. This renderer writes the specified value
     * as a string into the first found text node.
     *
     * @return the default renderer.
     */
    public ElementRenderer createDefaultElementRenderer();

    /**
     * Creates the default DOM element label renderer. This renderer uses
     * the default {@link org.itsnat.core.domutil.ElementRenderer} to
     * render the specified value.
     *
     * @return the default renderer.
     */
    public ElementLabelRenderer createDefaultElementLabelRenderer();

    /**
     * Creates the default DOM element list renderer. This renderer uses
     * the default {@link org.itsnat.core.domutil.ElementRenderer} to
     * render the specified value.
     *
     * @return the default renderer.
     */
    public ElementListRenderer createDefaultElementListRenderer();

    /**
     * Creates the default DOM element table renderer. This renderer uses
     * the default {@link org.itsnat.core.domutil.ElementRenderer} to
     * render the specified value.
     *
     * @return the default renderer.
     */
    public ElementTableRenderer createDefaultElementTableRenderer();

    /**
     * Creates the default DOM element table renderer. This renderer uses
     * the default {@link org.itsnat.core.domutil.ElementRenderer} to
     * render a user value usually into the label element.
     *
     * @return the default renderer.
     */
    public ElementTreeNodeRenderer createDefaultElementTreeNodeRenderer();


    /**
     * Creates the default DOM element list structure. This structure
     * returns as content element the &lt;td&gt; element if the
     * item parent is a &lt;tr&gt; otherwise the item parent element.
     *
     * @return the default structure.
     */
    public ElementListStructure createDefaultElementListStructure();


    /**
     * Creates the default DOM element table structure. This structure
     * returns as row content element the row parent element (itself),
     * and as cell content element the cell parent element (itself).
     *
     * @return the default structure.
     */
    public ElementTableStructure createDefaultElementTableStructure();

    /**
     * Creates the default DOM element tree node structure. This structure
     * is flexible enough to deal with several scenarios like tree-table,
     * no handle and icon etc.
     *
     * <p>The reference manual explains with examples
     * how this default structure works.</p>
     *
     * @return the default structure.
     */
    public ElementTreeNodeStructure createDefaultElementTreeNodeStructure();

}
