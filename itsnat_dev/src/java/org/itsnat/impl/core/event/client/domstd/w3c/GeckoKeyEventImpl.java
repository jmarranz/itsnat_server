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

package org.itsnat.impl.core.event.client.domstd.w3c;

import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.views.AbstractView;

/**
    Como lo define Firefox difiere un poco del draft del DOM-3 KeyboardEvent

    http://lxr.mozilla.org/seamonkey/source/dom/public/idl/events/nsIDOMKeyEvent.idl
    http://developer.mozilla.org/en/docs/DOM:event
    http://developer.mozilla.org/en/docs/DOM:event.initKeyEvent
    Tuvo un intento de estandarizarse casi igual en el W3C DOM 3
    http://www.w3.org/TR/2001/WD-DOM-Level-3-Events-20010410/events.html#Events-ItsNatKeyEvent

 * @author jmarranz
 */
public class GeckoKeyEventImpl extends W3CUIEventImpl implements ItsNatKeyEvent
{

    /**
     * Creates a new instance of GeckoKeyEventImpl
     */
    public GeckoKeyEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
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
        return getParameterInt("charCode");
    }

    public boolean getCtrlKey()
    {
        return getParameterBoolean("ctrlKey");
    }

    public int getKeyCode()
    {
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
