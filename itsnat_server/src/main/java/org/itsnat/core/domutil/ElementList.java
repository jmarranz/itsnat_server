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

/**
 * Manages a pattern based DOM Element list. The pattern is used to create new elements (by using a deep clone),
 * so all elements have the same tag name (same as the pattern).
 *
 * <p>The starting point is a DOM element list with almost a child element, this first child element
 * is saved as the pattern (really a deep clone), and new elements are created using this element as the pattern.
 * The initial element list (including the pattern) may be initially cleared
 * or kept as is when this object is created and attached to the underlying DOM list.</p>
 *
 * <p>This type of list helps to render a list of values into the DOM element list,
 * for instance, this interface support "out the box" the typical DOM element list where
 * every child element contains some value usually as the data of a text node.
 * Methods to add new elements include optionally a <code>value</code> parameter.
 * The structure and renderer objects are used to customize how and where this value is saved in the list
 * beyond the default cases.</p>
 *
 * <p>A pattern based DOM Element list ever works in "master" mode, see {@link ElementListFree#isMaster()}.</p>
 *
 * @see ElementGroupManager#createElementList(org.w3c.dom.Element,boolean)
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementList extends ElementListBase
{
    /**
     * Returns the current structure used by this list.
     *
     * @return the current structure.
     * @see #setElementListStructure(ElementListStructure)
     */
    public ElementListStructure getElementListStructure();

    /**
     * Sets the structure used by this list.
     *
     * @param structure the new structure.
     * @see #getElementListStructure()
     */
    public void setElementListStructure(ElementListStructure structure);

    /**
     * Returns the current renderer used by this list.
     *
     * @return the current renderer.
     * @see #setElementListRenderer(ElementListRenderer)
     */
    public ElementListRenderer getElementListRenderer();

    /**
     * Sets the renderer used by this list.
     *
     * @param renderer the new renderer.
     * @see #getElementListRenderer()
     */
    public void setElementListRenderer(ElementListRenderer renderer);

    /**
     * Returns the element used as a pattern. This element is a clone of the
     * original first child element used as a pattern.
     *
     * @return the pattern element.
     */
    public Element getChildPatternElement();


    /**
     * Increases or shrinks the list to fit the new size.
     *
     * <p>If the new size is bigger new elements are added at the end, if the size
     * is lower tail elements are removed.</p>
     *
     * <p>Note: <code>getLength()</code> returns the current length, this method
     * is defined in <code>org.w3c.dom.NodeList.getLength()</code>.</p>
     *
     * @param len the new length.
     * @see #addElement()
     * @see #removeElementAt(int)
     */
    public void setLength(int len);

    /**
     * Adds a new child element at the end of the list using the pattern (the new element is a clone).
     *
     * @return the new element.
     * @see #addElement(Object)
     */
    public Element addElement();

    /**
     * Adds a new child element at the end of the list and renders the specified value using
     * the current structure and renderer.
     *
     * @param value the value to render.
     * @return the new element.
     * @see #addElement()
     * @see #getElementListStructure()
     * @see #getElementListRenderer()
     * @see ElementListStructure#getContentElement(ElementList,int,Element)
     * @see ElementListRenderer#renderList(ElementList,int,Object,Element,boolean)
     */
    public Element addElement(Object value);

    /**
     * Inserts a new child element at the specified position using the pattern (the new element is a clone).
     *
     * @param index index of the new element.
     * @return the new element.
     * @see #insertElementAt(int,Object)
     */
    public Element insertElementAt(int index);

    /**
     * Inserts a new child element at the specified position and renders the specified value using
     * the current structure and renderer.
     *
     * @param index index of the element.
     * @param value the value to render.
     * @return the new element.
     * @see #insertElementAt(int)
     * @see #getElementListStructure()
     * @see #getElementListRenderer()
     * @see ElementListStructure#getContentElement(ElementList,int,Element)
     * @see ElementListRenderer#renderList(ElementList,int,Object,Element,boolean)
     */
    public Element insertElementAt(int index,Object value);

    /**
     * Renders the specified value into the element with the given position
     * using the current structure and renderer.
     *
     * @param index index of the element.
     * @param value the value to render.
     * @see #insertElementAt(int,Object)
     * @see #getElementListStructure()
     * @see #getElementListRenderer()
     * @see ElementListStructure#getContentElement(ElementList,int,Element)
     * @see ElementListRenderer#renderList(ElementList,int,Object,Element,boolean)
     */
    public void setElementValueAt(int index,Object value);

    /**
     * Returns the "content" element, this element is used to render below
     * the associated value of the child element. This element is obtained
     * using the current structure.
     *
     * @param index index of the element.
     * @return the content element.
     * @see #getElementListStructure()
     * @see ElementListStructure#getContentElement(ElementList,int,Element)
     * @see ElementListRenderer#renderList(ElementList,int,Object,Element,boolean)
     */
    public Element getContentElementAt(int index);

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

    /**
     * Returns the pattern used to render values if {@link #isUsePatternMarkupToRender()}
     * is true.
     *
     * @return the pattern used to render values.
     */
    public DocumentFragment getChildContentPatternFragment();
}
