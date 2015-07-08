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
 * This event type is the case of event stateless when dispatched to a global document listener
 * because a document template was specified in request.
 *
 * <p>Default implementation inherits from <code>java.util.EventObject</code>.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatEventDOMStateless extends ItsNatEventStateless,ItsNatDOMExtEvent
{
}
