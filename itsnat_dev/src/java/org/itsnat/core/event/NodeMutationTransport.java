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
 * Is used to command ItsNat to transport a client node mutation
 * and optionally synchronize this change at the server.
 *
 * <p>Only works with a browser with W3C DOM Mutation Events support like Mozilla/FireFox or Safari
 * and only with mutation events.</p>
 *
 *
 * @see org.itsnat.core.ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,ParamTransport[],String,long)
 * @see org.itsnat.core.ItsNatDocument#addMutationEventListener(org.w3c.dom.events.EventTarget,org.w3c.dom.events.EventListener,boolean,int,String,long)
 */
public class NodeMutationTransport extends ParamTransport
{
    /**
     * Creates a new instance ready to transport the client mutation
     * and synchronize it at the server side.
     */
    public NodeMutationTransport()
    {
        super(true);
    }

    /**
     * Creates a new instance ready to transport the client mutation
     * and optionally synchronize it at the server side.
     *
     * @param sync if true the server is updated.
     */
    public NodeMutationTransport(boolean sync)
    {
        super(sync);
    }
}
