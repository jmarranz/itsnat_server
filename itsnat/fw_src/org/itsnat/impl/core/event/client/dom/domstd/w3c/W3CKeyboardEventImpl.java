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
 * Navegadores que soportan de alguna forma el KeyboardEvent de DOM-3
 * aunque ha de transformase en el ItsNatKeyEvent basado en FireFox
 *
 * Aunque se hable de Safari (WebKit en general) también es aplicable a BlackBerryOld
 *
 * @author jmarranz
 */
public abstract class W3CKeyboardEventImpl extends W3CUIEventImpl implements ItsNatKeyEvent
{

    /**
     * Creates a new instance of W3CKeyEventImpl
     */
    public W3CKeyboardEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public abstract W3CKeyboardEventSharedImpl getW3CKeyboardEventShared();

    public void initKeyEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, int keyCodeArg, int charCodeArg)
    {
       throw new ItsNatException("Not implemented",this);
    }

    public String getKeyIdentifier()
    {
        return getW3CKeyboardEventShared().getKeyIdentifier(this);
    }

    public int getKeyLocation()
    {
        return getW3CKeyboardEventShared().getKeyLocation(this);
    }

    public boolean getAltKey()
    {
        return getW3CKeyboardEventShared().getAltKey(this);
    }

    public int getCharCode()
    {
        return getW3CKeyboardEventShared().getCharCode(this);
    }

    public boolean getCtrlKey()
    {
        return getW3CKeyboardEventShared().getCtrlKey(this);
    }

    public int getKeyCode()
    {
        return getW3CKeyboardEventShared().getKeyCode(this);
    }

    public boolean getMetaKey()
    {
        return getW3CKeyboardEventShared().getMetaKey(this);
    }

    public boolean getShiftKey()
    {
        return getW3CKeyboardEventShared().getShiftKey(this);
    }

}
