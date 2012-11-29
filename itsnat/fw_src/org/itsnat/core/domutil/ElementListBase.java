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
import org.w3c.dom.NodeList;

/**
 * This utility interface represents and manages an integer indexed DOM Element list, a list
 * of consecutive elements with a single parent element.
 *
 * <p>Objects implementing this interface are attached to a real DOM element list
 * with a single parent element, this "initial" list may be not empty,
 * in fact it is automatically synchronized with the "real" DOM element list
 * to show the current state when the utility object is created.</p>
 *
 * <p>This interface only manages DOM Element objects as list elements, other node types like text nodes are ignored (filtered).</p>
 *
 * <p>Indexes are zero-based.</p>
 *
 * <p>When a DOM element is added or removed using this interface, this element
 * is added/removed to/from the DOM list too.</p>
 *
 * <p>The interface inherits from <code>org.w3c.dom.NodeList</code>, the method
 * <code>NodeList.item(int)</code> is equivalent to {@link #getElementAt(int)}
 * and <code>NodeList.getLength()</code> return the number of child DOM Elements
 * in the list.</p>
 *
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementListBase extends ElementGroup,NodeList
{
    /**
     * Informs whether the list is empty (no elements).
     *
     * @return true if the list is empty.
     */
    public boolean isEmpty();

    /**
     * Removes the specified element.
     *
     * @param index index of the element to remove.
     * @return the removed element or null if index is out of bounds.
     */
    public Element removeElementAt(int index);

    /**
     * Removes the elements between the specified indexes.
     *
     * @param fromIndex low index (inclusive).
     * @param toIndex high index (inclusive).
     */
    public void removeElementRange(int fromIndex, int toIndex);

    /**
     * Removes all elements. The list is now empty.
     */
    public void removeAllElements();

    /**
     *  Moves one or more elements from the inclusive range <code>start</code> to
     *  <code>end</code> to the <code>to</code> position in the list.
     *  After the move, the element that was at index <code>start</code>
     *  will be at index <code>to</code>.
     *
     *  <pre>
     *  Examples of moves:
     *  <p>
     *  1. moveRow(1,3,5);
     *          a|B|C|D|e|f|g|h|i|j|k   - before
     *          a|e|f|g|h|B|C|D|i|j|k   - after
     *  <p>
     *  2. moveRow(6,7,1);
     *          a|b|c|d|e|f|G|H|i|j|k   - before
     *          a|G|H|b|c|d|e|f|i|j|k   - after
     *  <p>
     *  </pre>
     *
     * <p>Behavior and documentation is based on <code>DefaultTableModel.moveRow(int,int,int)</code></p>
     *
     * @param   start       the starting element index to be moved
     * @param   end         the ending element index to be moved
     * @param   to          the destination of the elements to be moved
    */
    public void moveElement(int start,int end,int to);

    /**
     * Returns the element list as an array.
     *
     * @return the element array.
     */
    public Element[] getElements();

    /**
     * Returns the element at the specified index.
     *
     * @param index index of the element to search.
     * @return the element in this position or null if index is out of range.
     */
    public Element getElementAt(int index);

    /**
     * Returns the first child element (element at position 0).
     *
     * @return the first child element or null is the list is empty.
     */
    public Element getFirstElement();

    /**
     * Returns the last child element (element at position <code>getLength() - 1</code>).
     *
     * @return the last child element or null is the list is empty.
     */
    public Element getLastElement();

    /**
     * Returns the position of the specified element.
     *
     * @param elem the element to search.
     * @return the position or -1 if the specified element is not in the list.
     */
    public int indexOfElement(Element elem);

    /**
     * Returns the position of the specified element searching backwards.
     *
     * <p>The result must be the same as {@link #indexOfElement(Element)} because
     * there is no "duplicated" elements. Use this method if the specified
     * element is near to the end of the list.</p>
     *
     * @param elem the element to search.
     * @return the element position or -1 if the specified element is not in the list.
     */
    public int lastIndexOfElement(Element elem);

    /**
     * Returns the child element of the list containing the specified node. If the node
     * is itself an element of the list, self is returned.
     *
     * @param node the node to search for.
     * @return the element containing or equal the specified node. Null if this node is not contained by the list.
     * @see #indexOfElement(Element)
     * @see #getListElementInfoFromNode(Node)
     */
    public Element getElementFromNode(Node node);

    /**
     * Returns an object info of the child element at the specified position.
     *
     * @param index index of the element to search for.
     * @return the object info of the matched child element. Null if index is out of range.
     * @see #getElementFromNode(Node)
     */
    public ListElementInfo getListElementInfoAt(int index);

    /**
     * Returns an object info of the child element containing the specified node (or the node
     * is itself an element of the list).
     *
     * @param node the node to search for.
     * @return the object info of the matched child element. Null if this node is not contained by the list.
     * @see #getListElementInfoAt(int)
     */
    public ListElementInfo getListElementInfoFromNode(Node node);
}
