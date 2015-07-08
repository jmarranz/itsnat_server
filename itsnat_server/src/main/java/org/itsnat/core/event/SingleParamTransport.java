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
 * Is the base class of "transport" classes which carry one named value, these classes are used to send custom data from client
 * to server using an event fired on the client and listened in the server.
 *
 * <p>Derived classes may do some kind of automatic synchronization in the server, for instance
 * an attribute of a client element can be transported and updated the server counterpart element.</p>
 *
 * <p>With or without synchronization the transported value can be obtained
 * calling {@link org.itsnat.core.event.ItsNatEvent#getExtraParam(String)}</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.core.ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,ParamTransport[],String,long)
 */
public abstract class SingleParamTransport extends ParamTransport
{
    private final String name;

    /**
     * Creates a new instance ready to transport a client value with the specified name
     * with optional server synchronization.
     *
     * @param name the attribute name.
     * @param sync if true the server attribute is updated.
     */
    protected SingleParamTransport(String name,boolean sync)
    {
        super(sync);

        this.name = name;
    }

    /**
     * Returns the item name for instance a property or attribute name.
     *
     * @return the item name.
     */
    public String getName()
    {
        return name;
    }
}
