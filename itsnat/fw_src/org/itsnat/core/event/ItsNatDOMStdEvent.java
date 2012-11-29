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
 * Every standard DOM event implements this interface. This interface is defined for
 * identification purposes.
 *
 * <p>This event type is received by <code>org.w3c.dom.events.EventListener</code> listeners
 * as <code>org.w3c.dom.events.Event</code> objects.</p>
 *
 * <p>Default implementation inherits from <code>java.util.EventObject</code>.</p>
 *
 * @see org.itsnat.core.ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,ParamTransport[],String,long)
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatDOMStdEvent extends ItsNatNormalEvent,Event
{

}
