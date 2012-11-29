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

package org.itsnat.comp.list;

import org.itsnat.core.ItsNatUserData;
import org.w3c.dom.Element;

/**
 * Contains visual information of a list child.
 *
 * <p>This interface is the "componentized" version of a master
 * {@link org.itsnat.core.domutil.ListElementInfo} and
 * follows a similar philosophy.</p>
 *
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatListUI#getItsNatListCellUIAt(int)
 * @see ItsNatListUI#getItsNatListCellUIFromNode(org.w3c.dom.Node)
 */
public interface ItsNatListCellUI extends ItsNatUserData
{
    /**
     * Returns the parent list UI this object belongs to.
     *
     * @return the parent list UI.
     */
    public ItsNatListUI getItsNatListUI();

    /**
     * Returns the element index.
     *
     * @return the element index.
     */
    public int getIndex();

    /**
     * Returns the DOM element object parent of this cell.
     *
     * @return the DOM element.
     */
    public Element getElement();

    /**
     * Returns the content element of this list element.
     *
     * <p>Current implementation delegates to {@link ItsNatListUI#getContentElementAt(int)}.</p>
     *
     *
     * @return the content element of this list element.
     */
    public Element getContentElement();
}
