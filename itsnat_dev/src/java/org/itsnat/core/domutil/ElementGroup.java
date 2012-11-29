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
import org.itsnat.core.ItsNatUserData;
import org.w3c.dom.Element;

/**
 * This interface is the base of several interface oriented to manage groups of DOM elements such as
 * element lists, tables and trees. Any element group has a single parent DOM element.
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementGroup extends ItsNatUserData
{

    /**
     * Returns the parent element of this element group.
     *
     * @return the parent element.
     */
    public Element getParentElement();

    /**
     * Returns the ItsNat document used to create this element group.
     *
     * @return the ItsNat document.
     */
    public ItsNatDocument getItsNatDocument();
}
