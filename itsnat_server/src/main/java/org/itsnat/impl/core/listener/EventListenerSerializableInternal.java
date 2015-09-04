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

import java.io.Serializable;

/**
 * Interface de utilidad para añadir el Serializable en los EventListeners (prácticamente todos) útil para crear como anonymous inner classes serializables
 * internas (new EventListenerSerializableInternal() { ...};) e indirectamente para identificar los EventListener Serializables internos de ItsNat respecto a los del usuario, puesto que EventListenerSerializableInternal NO es público
 * 
 * @author jmarranz
 */
public interface EventListenerSerializableInternal extends EventListenerInternal,Serializable
{

}
