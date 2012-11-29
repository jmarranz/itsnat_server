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

package org.itsnat.core;

/**
 * This basic interface provides a user data registry. This registry
 * is in no way used by the framework.
 *
 * <p>Most of ItsNat objects implement this interface to help the user to reduce
 * the work of wiring user data with ItsNat objects.</p>
 *
 * <p>User data registry methods are synchronized if this object is a
 * {@link ItsNatServletContext}, {@link ItsNatServlet}, {@link ItsNatServletConfig},
 * {@link ItsNatSession}, {@link org.itsnat.core.tmpl.MarkupTemplate} or {@link CometNotifier}
 * otherwise are unsynchronized.</p>
 *
 */
public interface ItsNatUserData
{
    /**
     * Informs whether the registry contains one pair name/value with the specified name.
     *
     * @param name the name to look for.
     * @return true if there is a name/value pair with this name.
     */
    public boolean containsUserValueName(String name);

    /**
     * Returns the value associated to the specified name.
     *
     * @param name the name to look for.
     * @return the value associated or null if not found.
     */
    public Object getUserValue(String name);

    /**
     * Sets a new value associated to the specified name.
     *
     * @param name the name used to register.
     * @param value the value with the specified name.
     * @return the old value associated to this name or null if none.
     */
    public Object setUserValue(String name,Object value);

    /**
     * Removes the name/value registry with the specified name.
     *
     * @param name the name to look for.
     * @return the associated value or null if no registry has this name.
     */
    public Object removeUserValue(String name);

    /**
     * Returns all registered names.
     *
     * @return an array with all registered names.
     */
    public String[] getUserValueNames();
}
