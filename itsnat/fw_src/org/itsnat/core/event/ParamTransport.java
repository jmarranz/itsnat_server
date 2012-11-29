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

import java.io.Serializable;

/**
 * Is the base class of "transport" classes, these classes are used to send custom data from client
 * to server using an event fired on the client and listened in the server.
 *
 * <p>Derived classes may do some kind of automatic synchronization in the server, for instance
 * an attribute of a client element can be transported and updated the server counterpart element.</p>

 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.core.ItsNatDocument#addEventListener(EventTarget,String,EventListener,boolean,ParamTransport)
 */
public abstract class ParamTransport implements Serializable
{
    private boolean sync;

    /**
     * Creates a new instance with optional synchronization.
     *
     * @param sync if true the server is synchronized in some way.
     */
    protected ParamTransport(boolean sync)
    {
        this.sync = sync;
    }

    /**
     * If the server state is synchronized in some way.
     *
     * @return true if the server is synchronized.
     */
    public boolean isSync()
    {
        return sync;
    }
}
