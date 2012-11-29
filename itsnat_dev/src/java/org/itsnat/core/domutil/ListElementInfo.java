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

import org.itsnat.core.ItsNatUserData;
import org.w3c.dom.Element;

/**
 * Contains the DOM Element object and index of a child of a DOM element list.
 *
 * <p>If the parent list (which this object belongs to) is master, this object is ever the
 * same instance per list element, supporting index changes (inserting or removing
 * elements to the list) and DOM element changes (calling {@link ElementListFree#setElementAt(int,org.w3c.dom.Element)} ).
 * This feature is very interesting to save contextual
 * data associated to the child element (using {@link ItsNatUserData}).
 * </p>
 *
 * <p>If the parent list is slave every object obtained is a new instance, {@link ItsNatUserData}
 * can not be used.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ElementListBase#getListElementInfoAt(int)
 * @see ElementListBase#getListElementInfoFromNode(Node)
 */
public interface ListElementInfo extends ItsNatUserData
{
    /**
     * Returns the parent list this object belongs to.
     *
     * @return the parent list.
     */
    public ElementListBase getParentList();

    /**
     * Returns the DOM element object parent of this child.
     *
     * @return the DOM element.
     */
    public Element getElement();

    /**
     * Returns the element index.
     *
     * @return the element index.
     */
    public int getIndex();
}
