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

package org.itsnat.impl.core.listener;

import org.w3c.dom.events.EventListener;

/**
 * Interface de utilidad para identificar los EventListener internos de ItsNat (es necesario un instanceof EventListenerInternal para JProxy) respecto a los del usuario, puesto que EventListenerInternal NO es público
 * 
 * @author jmarranz
 */
public interface EventListenerInternal extends EventListener
{

}
