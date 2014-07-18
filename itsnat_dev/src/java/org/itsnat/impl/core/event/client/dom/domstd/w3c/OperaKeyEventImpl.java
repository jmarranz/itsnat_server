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

import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.views.AbstractView;

/**
 *
 * @author jmarranz
 */
public class OperaKeyEventImpl extends W3CUIEventImpl implements ItsNatKeyEvent
{

    /**
     * Creates a new instance of OperaKeyEventImpl
     */
    public OperaKeyEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public void initKeyEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, int keyCodeArg, int charCodeArg)
    {
       throw new ItsNatException("Not implemented",this);
    }

    public boolean getAltKey()
    {
        return getParameterBoolean("altKey");
    }

    public int getCharCode()
    {
        if (getType().equals("keypress"))  // En keypress keyCode es el charCode
            return getParameterInt("keyCode");
        else
            return 0;
    }

    public boolean getCtrlKey()
    {
        return getParameterBoolean("ctrlKey");
    }

    public int getKeyCode()
    {
        if (getType().equals("keypress"))
            return 0;
        else
            return getParameterInt("keyCode");
    }

    public boolean getMetaKey()
    {
        return getParameterBoolean("metaKey");
    }

    public boolean getShiftKey()
    {
        return getParameterBoolean("shiftKey");
    }

}
