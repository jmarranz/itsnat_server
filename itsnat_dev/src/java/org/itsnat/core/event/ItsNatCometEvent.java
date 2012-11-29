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

/**
 * This event is fired automatically by the client and sent to the server
 * to wait until the associated {@link org.itsnat.core.CometNotifier} has a pending notification
 * then is dispatched to the registered listeners if any.
 *
 * <p>Default implementation inherits from <code>java.util.EventObject</code>.</p>
 *
 * @see org.itsnat.core.CometNotifier#addEventListener(EventListener)
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatCometEvent extends ItsNatDOMExtEvent
{

}
