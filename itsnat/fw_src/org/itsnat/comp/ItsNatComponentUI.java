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

package org.itsnat.comp;

import org.itsnat.comp.ItsNatComponent;

/**
 * Is the base interface of the User Interface utility object associated to
 * the component.
 *
 * <p>This utility object manages only the markup layout of the component.
 * The component calls UI methods typically as response of data model changes
 * to synchronize the markup to match the data model.
 * The UI object does not need the data model (if the component has one), only
 * the tree UI implementation uses the tree data model internally.
 * </p>
 *
 * <p>This architecture, inspired in Swing, separates the layout manager (the UI object) and the
 * component object working as a coordinator of data and selection models, UI
 * and event based actions.</p>
 *
 * <p>User code may use UI methods to "query" the markup of the component.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatComponent#getItsNatComponentUI()
 */
public interface ItsNatComponentUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatComponent getItsNatComponent();
}
