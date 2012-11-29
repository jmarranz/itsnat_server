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

package org.itsnat.impl.core.event.client.domstd.msie;

import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.views.AbstractView;

/**
 *
 * @author jmarranz
 */
public class MSIEOldKeyEventImpl extends MSIEOldUIEventImpl implements ItsNatKeyEvent
{
    /**
     * Creates a new instance of MSIEOldKeyEventImpl
     */
    public MSIEOldKeyEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public void initKeyEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, int keyCodeArg, int charCodeArg)
    {
        throw new ItsNatException("Not implemented",this);
    }

    public boolean getAltKey()
    {
        return originalEvt.getAltKey();
    }

    public int getCharCode()
    {
        String type = getType();
        if (type.equals("keypress"))
            return originalEvt.getKeyCode(); // Es la letra generada en Unicode
        else
            return 0;
    }

    public boolean getCtrlKey()
    {
        return originalEvt.getCtrlKey();
    }

    public int getKeyCode()
    {
        String type = getType();
        if (type.equals("keypress"))
            return 0;
        else
            return originalEvt.getKeyCode(); // Es el código de la tecla del teclado (ignorando modificadores)
    }

    public boolean getMetaKey()
    {
        return false; // El MSIE no lo soporta, la metaKey es sólo para Mac (Command Key)
    }

    public boolean getShiftKey()
    {
        return originalEvt.getShiftKey();
    }

}
