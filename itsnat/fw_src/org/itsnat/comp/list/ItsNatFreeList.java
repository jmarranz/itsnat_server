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

import org.itsnat.comp.*;

/**
 * Is the base interface of the free multiple selection lists and combo box components.
 *
 * <p>Almost a child DOM element must be present in the markup, this element is used as a pattern
 * to create new list items and is removed because by default the data model is empty.</p>
 *
 * <p>By default this component uses the default renderer and editor.</p>
 *
 * <p>There is no default decoration of list item selection,
 * selection model listeners may be used to decorate the list markup when its
 * selection state changes.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatFreeList extends ItsNatList,ItsNatFreeComponent
{
    /**
     * Informs whether a joystick is enough to control the component
     * (some kind of mouse, pointer or stylus not present or not necessary).
     *
     * <p>If the component is in joystick mode, the "content element" of every
     * list item has an event listener associated. By this way list items
     * are "live" elements and can traversed using a joystick in mobile devices
     * without a mouse, pointer or stylus.
     * </p>
     *
     * <p>Default value is defined by {@link org.itsnat.core.ItsNatDocument#isJoystickMode()}
     * </p>
     *
     * @return true if joystick mode is on.
     * @see #setJoystickMode(boolean)
     * @see ItsNatListStructure#getContentElement(ItsNatList, int, org.w3c.dom.Element)
     */
    public boolean isJoystickMode();

    /**
     * Informs whether a joystick is enough to control the component
     * (some kind of mouse, pointer or stylus not present or not necessary).
     *
     * @param value true to enable joystick mode.
     * @see #isJoystickMode()
     */
    public void setJoystickMode(boolean value);
}
