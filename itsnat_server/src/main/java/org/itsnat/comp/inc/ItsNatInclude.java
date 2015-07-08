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

package org.itsnat.comp.inc;

import org.itsnat.comp.*;

/**
 * Is the base interface of "include" components. A "include" is used to insert/remove
 * dynamically a markup fragment below the associated DOM element of the component.
 *
 * <p>Only a free version is defined.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatInclude extends ItsNatElementComponent
{
    /**
     * Informs whether a fragment was inserted using this component.
     *
     * @return true if a fragment was inserted.
     * @see #includeFragment(String,boolean)
     */
    public boolean isIncluded();

    /**
     * Returns the name of the current included fragment.
     *
     * @return the name of the current included fragment. Null if no fragment was inserted.
     * @see #includeFragment(String,boolean)
     */
    public String getIncludedFragmentName();

    /**
     * Includes a new markup fragment. If a fragment was included before then is removed first.
     *
     * @param name the name of the markup fragment to insert.
     * @param buildComp if true markup defined components are automatically built.
     * @see #removeFragment()
     * @see ItsNatComponentManager#buildItsNatComponents(Node)
     */
    public void includeFragment(String name,boolean buildComp);

    /**
     * Includes a new markup fragment. If a fragment was included before then is removed first.
     * No markup defined components are automatically built .
     *
     * @param name the name of the markup fragment to insert.
     * @see #includeFragment(String,boolean)
     */
    public void includeFragment(String name);

    /**
     * Removes the current included markup fragment. If no fragment was included nothing is done.
     *
     * @see #includeFragment(String,boolean)
     */
    public void removeFragment();
}
