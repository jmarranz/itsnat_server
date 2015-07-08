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

package org.itsnat.impl.core.event.client.dom.domstd.w3c;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.event.client.dom.domstd.NodeContainerImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

/**
 *
 * @author jmarranz
 */
public class W3CMouseEventImpl extends W3CUIEventImpl implements MouseEvent
{
    protected NodeContainerImpl relatedTarget;

    /**
     * Creates a new instance of W3CMouseEventImpl
     */
    public W3CMouseEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public void initMouseEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, int detailArg, int screenXArg, int screenYArg, int clientXArg, int clientYArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, short buttonArg, EventTarget relatedTargetArg)
    {
       throw new ItsNatException("Not implemented",this);
    }

    public int getScreenX()
    {
        return getParameterInt("screenX");
    }

    public int getScreenY()
    {
        return getParameterInt("screenY");
    }

    public int getClientX()
    {
        return getParameterInt("clientX");
    }

    public int getClientY()
    {
        return getParameterInt("clientY");
    }

    public boolean getCtrlKey()
    {
        return getParameterBoolean("ctrlKey");
    }

    public boolean getShiftKey()
    {
        return getParameterBoolean("shiftKey");
    }

    public boolean getAltKey()
    {
        return getParameterBoolean("altKey");
    }

    public boolean getMetaKey()
    {
        return getParameterBoolean("metaKey");
    }

    public short getButton()
    {
        return getParameterShort("button");
    }

    public EventTarget getRelatedTarget()
    {
        if (relatedTarget == null)
            this.relatedTarget = new NodeContainerImpl(getParameterNode("relatedTarget"));
        return (EventTarget)relatedTarget.get();
    }

    public void resolveNodePaths()
    {
        super.resolveNodePaths();

        getRelatedTarget();
    }
}
