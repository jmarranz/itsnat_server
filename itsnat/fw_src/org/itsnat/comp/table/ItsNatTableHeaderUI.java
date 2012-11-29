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

package org.itsnat.comp.table;

import org.itsnat.comp.ItsNatElementComponentUI;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Is the base interface of the User Interface of a table header.
 *
 * <p>A table header is managed as a DOM element list, using the current table structure
 * and renderer.</p>
 *
 * <p>Current implementation does not use the data model and relays heavily on
 * {@link org.itsnat.core.domutil.ElementList}.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatTableHeader#getItsNatTableHeaderUI()
 * @see ItsNatTableHeader#getItsNatTableHeaderCellRenderer()
 * @see org.itsnat.comp.table.ItsNatTable#getItsNatTableStructure()
 * @see org.itsnat.core.domutil.ElementList
 */
public interface ItsNatTableHeaderUI extends ItsNatElementComponentUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatTableHeader getItsNatTableHeader();

    /**
     * Returns the number of header columns.
     *
     * @return  the number of header columns.
     * @see org.itsnat.core.domutil.ElementList#getLength()
     */
    public int getLength();

    /**
     * Informs whether the table header is empty (no columns).
     *
     * @return true if the table header is empty.
     * @see org.itsnat.core.domutil.ElementListBase#isEmpty()
     */
    public boolean isEmpty();

    /**
     * Returns the header column element at the specified index.
     *
     * @param index index of the header column to search.
     * @return the element in this position or null if index is out of range.
     * @see org.itsnat.core.domutil.ElementListBase#getElementAt(int)
     */
    public Element getElementAt(int index);

    /**
     * Returns the "content" element, this element is used to render below
     * the associated value of the header column element. This element is obtained
     * using the current structure.
     *
     * @param index index of the element.
     * @return the content element.
     * @see ItsNatTableStructure#getHeaderColumnContentElement(ItsNatTableHeader,int,Element)
     * @see org.itsnat.core.domutil.ElementList#getContentElementAt(int)
     */
    public Element getContentElementAt(int index);

    /**
     * Returns an object info of the header column element at the specified position.
     *
     * @param index index of the element to search for.
     * @return the object info of the matched column element. Null if index is out of range.
     * @see #getItsNatTableHeaderCellUIFromNode(Node)
     * @see org.itsnat.core.domutil.ElementListBase#getListElementInfoAt(int)
     */
    public ItsNatTableHeaderCellUI getItsNatTableHeaderCellUIAt(int index);

    /**
     * Returns an object info of the header column element containing the specified node (or the node
     * is itself an element of the list).
     *
     * @param node the node to search for.
     * @return the object info of the matched column element. Null if this node is not contained by the list.
     * @see #getItsNatTableHeaderCellUIAt(int)
     * @see org.itsnat.core.domutil.ElementListBase#getListElementInfoFromNode(Node)
     */
    public ItsNatTableHeaderCellUI getItsNatTableHeaderCellUIFromNode(Node node);

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
