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

import org.itsnat.impl.core.listener.trans.NodeInnerTransportUtil;

/**
 * Is used to command ItsNat to transport the current content (inner) of the specified client
 * node and optionally synchronize it with the matched server DOM node.
 *
 * <p>After synchronization the server node has the same content
 * as the client counterpart.</p>
 *
 * <p>With or without synchronization the client node content can be obtained in
 * markup form calling {@link org.itsnat.core.event.ItsNatEvent#getExtraParam(String)}
 * with the name returned by {@link NodeInnerTransport#getName()}</p>
 *
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.core.ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,ParamTransport[],String,long)
 */
public class NodeInnerTransport extends SingleParamTransport
{

    /**
     * Creates a new instance ready to transport the current content of the specified
     * client node and synchronizing the server node.
     */
    public NodeInnerTransport()
    {
        this(true);
    }

    /**
     * Creates a new instance ready to transport the current content of the specified
     * client node with optional server synchronization.
     *
     * @param sync if true the server node content is updated.
     */
    public NodeInnerTransport(boolean sync)
    {
        super(NodeInnerTransportUtil.getName(),sync);
    }
}
