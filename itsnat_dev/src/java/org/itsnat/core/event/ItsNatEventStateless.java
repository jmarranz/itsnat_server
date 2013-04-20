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

package org.itsnat.core.event;

import org.w3c.dom.events.Event;

/**
 * This event is created by the client calling the ItsNat JavaScript method
 * <code>createEventStateless</code> and dispatched to the server calling the JavaScript method
 * <code>dispatchEventStateless</code>
 * and is received by the global servlet event listeners and global document even listeners when a document template is specified.
 *
 * <p>Default implementation inherits from <code>java.util.EventObject</code>.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatEventStateless extends ItsNatEvent,Event
{
}
