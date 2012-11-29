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
 * This event is fired when new code is being added or was added to the document or client.
 *
 * <p>Default implementation inherits from <code>java.util.EventObject</code>,
 * the <code>source</code> attribute is the {@link org.itsnat.core.ItsNatDocument}
 * or {@link org.itsnat.core.ClientDocument} target.</p>
 *
 * @see CodeToSendListener
 * @author Jose Maria Arranz Santamaria
 */
public interface CodeToSendEvent
{
    /**
     * Returns the code being added or was added.
     *
     * @return the new code.
     */
    public Object getCode();
}
