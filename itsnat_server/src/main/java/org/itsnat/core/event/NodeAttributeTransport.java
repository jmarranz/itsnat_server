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
 * Is used to command ItsNat to transport the specified attribute of a client
 * DOM element and optionally synchronize it with the matched server DOM element.
 *
 * <p>After synchronization the server element has the same attribute value
 * as the client counterpart. If the client element has not the specified attribute
 * the server element attribute will be removed too.</p>
 *
 * <p>With or without synchronization the attribute value can be obtained
 * calling {@link org.itsnat.core.event.ItsNatEvent#getExtraParam(String)}</p>
 *
 * @see org.itsnat.core.ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,ParamTransport[],String,long)
 * @author Jose Maria Arranz Santamaria
 */
public class NodeAttributeTransport extends SingleParamTransport
{

    /**
     * Creates a new instance ready to transport the attribute with the specified name
     * and synchronize it at the server side.
     *
     * @param name the attribute name.
     */
    public NodeAttributeTransport(String name)
    {
        this(name,true);
    }

    /**
     * Creates a new instance ready to transport the attribute with the specified name
     * with optional server synchronization.
     *
     * @param name the attribute name.
     * @param sync if true the server attribute is updated.
     */
    public NodeAttributeTransport(String name,boolean sync)
    {
        super(name,sync);
    }
}
