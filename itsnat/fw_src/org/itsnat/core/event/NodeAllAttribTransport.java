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
 * Is used to command ItsNat to transport all current attributes of a client
 * element and optionally synchronize them with the matched server element.
 *
 * <p>After synchronization the server DOM element has the same attributes and values
 * as the client counterpart.</p>
 *
 * <p>With or without synchronization the transported attributes can be obtained
 * calling {@link org.itsnat.core.event.ItsNatEvent#getExtraParam(String)}</p>
 *
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.core.ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,ParamTransport[],String,long)
 */
public class NodeAllAttribTransport extends ParamTransport
{

    /**
     * Creates a new instance with automatic synchronization enabled.
     */
    public NodeAllAttribTransport()
    {
        super(true);
    }

    /**
     * Creates a new instance with optional synchronization.
     *
     * @param sync if true the server element is synchronized
     */
    public NodeAllAttribTransport(boolean sync)
    {
        super(sync);
    }

}
