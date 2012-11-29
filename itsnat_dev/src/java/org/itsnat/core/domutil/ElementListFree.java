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

import java.util.List;
import org.w3c.dom.Element;

/**
 * Represents an integer indexed DOM Element list, child elements can have different
 * tag names (the meaning of "free").
 *
 * <p>The interface inherits from <code>java.util.List</code> and supports
 * both types of iterators. <code>List</code> and <code>Iterator</code> methods
 * accept and return DOM Element objects.</p>
 *
 * @see ElementGroupManager#createElementListFree(Element,boolean)
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementListFree extends ElementListBase,List
{
    /**
     * Informs whether the list works in master mode.
     *
     * <p>In master mode DOM elements must be added, removed or replaced
     * using this interface avoiding direct DOM operations (<code>Node.appendChild</code>,
     * <code>Node.removeChild</code> etc), this is the fastest mode.
     * In slave mode (master is false) direct DOM operations
     * can be used and element list changes are automatically reflected by this
     * object, nevertheless method calls are slower in this mode.</p>
     *
     * @return true if the list is in master mode.
     */
    public boolean isMaster();

    /**
     * Adds a new child element at the end of the list.
     *
     * <p>If the new element is already in the list a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is inserted. This avoids
     * an indirect delete by DOM.</p>
     *
     * @param elem the new element.
     * @see #insertElementAt(int,org.w3c.dom.Element)
     */
    public void addElement(Element elem);

    /**
     * Inserts a new child element at the specified position.
     *
     * <p>If the new element is already in the list a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is inserted. This avoids
     * an indirect delete by DOM.</p>
     *
     * @param index index of the element.
     * @param elem the new element.
     * @see #addElement(Element)
     */
    public void insertElementAt(int index,Element elem);

    /**
     * Replaces the specified element with a new one.
     *
     * <p>If the new element is already in the list a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is used to replac the element in that
     * position. This avoids an indirect delete by DOM.</p>
     *
     * @param index index of the element.
     * @param elem the new element.
     * @return the element replaced.
     * @see #getElementAt(int)
     * @see #insertElementAt(int,Element)
     */
    public Element setElementAt(int index,Element elem);

    /**
     * Clears the current list and fills again with new elements. The length may change.
     *
     * <p>If the new element is already in the list a deep clone
     * (calling <code>Node.cloneNode(boolean deep)</code>) is used to replace the element in that
     * position. This avoids an indirect delete by DOM.</p>
     *
     * @param newElems new elements.
     * @return the elements replaced.
     * @see #getElements()
     */
    public Element[] setElements(Element[] newElems);
}
