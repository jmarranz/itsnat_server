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
 * This event is fired automatically by the client when the time period ends
 * following the rules specified by the timer listener registration.
 *
 * <p>Default implementation inherits from <code>java.util.EventObject</code>.</p>
 *
 * @see org.itsnat.core.ItsNatTimer
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatTimerEvent extends ItsNatDOMExtEvent
{
    /**
     * Returns the timer handle associated to this timer event.
     *
     * @return the timer handle.
     */
    public ItsNatTimerHandle getItsNatTimerHandle();
}
