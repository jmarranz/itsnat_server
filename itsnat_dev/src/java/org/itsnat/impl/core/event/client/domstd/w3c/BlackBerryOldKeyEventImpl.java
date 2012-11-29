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
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;

/**
 * La BlackBerry JDE 4.6 parece que implementa el estándar DOM 3 del 21 December 2007
 *
 * http://www.w3.org/TR/2007/WD-DOM-Level-3-Events-20071221/events.html#Events-KeyboardEvents-Interfaces
 *
 * @author jmarranz
 */
public class BlackBerryOldKeyEventImpl extends W3CKeyboardEventImpl implements ItsNatKeyEvent
{
    protected static final W3CKeyboardEventSharedImpl delegate = new W3CKeyboardEventSharedImpl();

    // KeyLocationCode
    public static final int DOM_KEY_LOCATION_STANDARD      = 0x00;
    public static final int DOM_KEY_LOCATION_LEFT          = 0x01;
    public static final int DOM_KEY_LOCATION_RIGHT         = 0x02;
    public static final int DOM_KEY_LOCATION_NUMPAD        = 0x03;

    static
    {
        // La correspondencia se ha obtenido por "sentido común" de acuerdo
        // con la lista de identificadores:
        // http://www.w3.org/TR/2007/WD-DOM-Level-3-Events-20071221/keyset.html#KeySet-Set

        // No hay garantía plena pues no tenemos acceso al código
        // fuente de la BlackBerry

        addKeyCodeAndIdentifier("U+0018",ItsNatKeyEvent.DOM_VK_CANCEL);
        addKeyCodeAndIdentifier("Help",ItsNatKeyEvent.DOM_VK_HELP);
        addKeyCodeAndIdentifier("U+0008",ItsNatKeyEvent.DOM_VK_BACK_SPACE);
        addKeyCodeAndIdentifier("U+0009",ItsNatKeyEvent.DOM_VK_TAB);
        addKeyCodeAndIdentifier("Clear",ItsNatKeyEvent.DOM_VK_CLEAR);
        addKeyCodeAndIdentifier("Enter",ItsNatKeyEvent.DOM_VK_RETURN);  // Porque DOM_VK_RETURN=0x0D (el valor típico del Enter/Return) y no hay un "Return"
        // DOM_VK_ENTER=0x0E; Desconocido 0x0E no es el código típico de la tecla Enter.
        addKeyCodeAndIdentifier("Shift",ItsNatKeyEvent.DOM_VK_SHIFT);
        addKeyCodeAndIdentifier("Control",ItsNatKeyEvent.DOM_VK_CONTROL);
        addKeyCodeAndIdentifier("Alt",ItsNatKeyEvent.DOM_VK_ALT);
        addKeyCodeAndIdentifier("Pause",ItsNatKeyEvent.DOM_VK_PAUSE);
        addKeyCodeAndIdentifier("CapsLock",ItsNatKeyEvent.DOM_VK_CAPS_LOCK);
        addKeyCodeAndIdentifier("U+001B",ItsNatKeyEvent.DOM_VK_ESCAPE);
        addKeyCodeAndIdentifier("U+0020",ItsNatKeyEvent.DOM_VK_SPACE);
        addKeyCodeAndIdentifier("PageUp",ItsNatKeyEvent.DOM_VK_PAGE_UP);
        addKeyCodeAndIdentifier("PageDown",ItsNatKeyEvent.DOM_VK_PAGE_DOWN);
        addKeyCodeAndIdentifier("End",ItsNatKeyEvent.DOM_VK_END);
        addKeyCodeAndIdentifier("Home",ItsNatKeyEvent.DOM_VK_HOME);
        addKeyCodeAndIdentifier("Left",ItsNatKeyEvent.DOM_VK_LEFT);
        addKeyCodeAndIdentifier("Up",ItsNatKeyEvent.DOM_VK_UP);
        addKeyCodeAndIdentifier("Right",ItsNatKeyEvent.DOM_VK_RIGHT);
        addKeyCodeAndIdentifier("Down",ItsNatKeyEvent.DOM_VK_DOWN);
        addKeyCodeAndIdentifier("PrintScreen",ItsNatKeyEvent.DOM_VK_PRINTSCREEN);
        addKeyCodeAndIdentifier("Insert",ItsNatKeyEvent.DOM_VK_INSERT);
        addKeyCodeAndIdentifier("U+007F",ItsNatKeyEvent.DOM_VK_DELETE);
        addKeyCodeAndIdentifier("U+0030",ItsNatKeyEvent.DOM_VK_0);
        addKeyCodeAndIdentifier("U+0031",ItsNatKeyEvent.DOM_VK_1);
        addKeyCodeAndIdentifier("U+0032",ItsNatKeyEvent.DOM_VK_2);
        addKeyCodeAndIdentifier("U+0033",ItsNatKeyEvent.DOM_VK_3);
        addKeyCodeAndIdentifier("U+0034",ItsNatKeyEvent.DOM_VK_4);
        addKeyCodeAndIdentifier("U+0035",ItsNatKeyEvent.DOM_VK_5);
        addKeyCodeAndIdentifier("U+0036",ItsNatKeyEvent.DOM_VK_6);
        addKeyCodeAndIdentifier("U+0037",ItsNatKeyEvent.DOM_VK_7);
        addKeyCodeAndIdentifier("U+0038",ItsNatKeyEvent.DOM_VK_8);
        addKeyCodeAndIdentifier("U+0039",ItsNatKeyEvent.DOM_VK_9);
        addKeyCodeAndIdentifier("U+003B",ItsNatKeyEvent.DOM_VK_SEMICOLON);
        addKeyCodeAndIdentifier("U+003D",ItsNatKeyEvent.DOM_VK_EQUALS);
        addKeyCodeAndIdentifier("U+0041",ItsNatKeyEvent.DOM_VK_A);
        addKeyCodeAndIdentifier("U+0042",ItsNatKeyEvent.DOM_VK_B);
        addKeyCodeAndIdentifier("U+0043",ItsNatKeyEvent.DOM_VK_C);
        addKeyCodeAndIdentifier("U+0044",ItsNatKeyEvent.DOM_VK_D);
        addKeyCodeAndIdentifier("U+0045",ItsNatKeyEvent.DOM_VK_E);
        addKeyCodeAndIdentifier("U+0046",ItsNatKeyEvent.DOM_VK_F);
        addKeyCodeAndIdentifier("U+0047",ItsNatKeyEvent.DOM_VK_G);
        addKeyCodeAndIdentifier("U+0048",ItsNatKeyEvent.DOM_VK_H);
        addKeyCodeAndIdentifier("U+0049",ItsNatKeyEvent.DOM_VK_I);
        addKeyCodeAndIdentifier("U+004A",ItsNatKeyEvent.DOM_VK_J);
        addKeyCodeAndIdentifier("U+004B",ItsNatKeyEvent.DOM_VK_K);
        addKeyCodeAndIdentifier("U+004C",ItsNatKeyEvent.DOM_VK_L);
        addKeyCodeAndIdentifier("U+004D",ItsNatKeyEvent.DOM_VK_M);
        addKeyCodeAndIdentifier("U+004E",ItsNatKeyEvent.DOM_VK_N);
        addKeyCodeAndIdentifier("U+004F",ItsNatKeyEvent.DOM_VK_O);
        addKeyCodeAndIdentifier("U+0050",ItsNatKeyEvent.DOM_VK_P);
        addKeyCodeAndIdentifier("U+0051",ItsNatKeyEvent.DOM_VK_Q);
        addKeyCodeAndIdentifier("U+0052",ItsNatKeyEvent.DOM_VK_R);
        addKeyCodeAndIdentifier("U+0053",ItsNatKeyEvent.DOM_VK_S);
        addKeyCodeAndIdentifier("U+0054",ItsNatKeyEvent.DOM_VK_T);
        addKeyCodeAndIdentifier("U+0055",ItsNatKeyEvent.DOM_VK_U);
        addKeyCodeAndIdentifier("U+0056",ItsNatKeyEvent.DOM_VK_V);
        addKeyCodeAndIdentifier("U+0057",ItsNatKeyEvent.DOM_VK_W);
        addKeyCodeAndIdentifier("U+0058",ItsNatKeyEvent.DOM_VK_X);
        addKeyCodeAndIdentifier("U+0059",ItsNatKeyEvent.DOM_VK_Y);
        addKeyCodeAndIdentifier("U+005A",ItsNatKeyEvent.DOM_VK_Z);
        // DOM_VK_CONTEXT_MENU=0x5D; DESCONOCIDO

/*  Los siguientes han de identificarse a través de KeyboardEvent.keyLocation
    según dice el estándar.

        DOM_VK_NUMPAD0=0x60;
        DOM_VK_NUMPAD1=0x61;
        DOM_VK_NUMPAD2=0x62;
        DOM_VK_NUMPAD3=0x63;
        DOM_VK_NUMPAD4=0x64;
        DOM_VK_NUMPAD5=0x65;
        DOM_VK_NUMPAD6=0x66;
        DOM_VK_NUMPAD7=0x67;
        DOM_VK_NUMPAD8=0x68;
        DOM_VK_NUMPAD9=0x69;
        DOM_VK_MULTIPLY=0x6A;
        DOM_VK_ADD=0x6B;
        DOM_VK_SEPARATOR=0x6C;
        DOM_VK_SUBTRACT=0x6D;
        DOM_VK_DECIMAL=0x6E;
        DOM_VK_DIVIDE=0x6F;
 */
        addKeyCodeAndIdentifier("F1", ItsNatKeyEvent.DOM_VK_F1);
        addKeyCodeAndIdentifier("F2", ItsNatKeyEvent.DOM_VK_F2);
        addKeyCodeAndIdentifier("F3", ItsNatKeyEvent.DOM_VK_F3);
        addKeyCodeAndIdentifier("F4", ItsNatKeyEvent.DOM_VK_F4);
        addKeyCodeAndIdentifier("F5", ItsNatKeyEvent.DOM_VK_F5);
        addKeyCodeAndIdentifier("F6", ItsNatKeyEvent.DOM_VK_F6);
        addKeyCodeAndIdentifier("F7", ItsNatKeyEvent.DOM_VK_F7);
        addKeyCodeAndIdentifier("F8", ItsNatKeyEvent.DOM_VK_F8);
        addKeyCodeAndIdentifier("F9", ItsNatKeyEvent.DOM_VK_F9);
        addKeyCodeAndIdentifier("F10",ItsNatKeyEvent.DOM_VK_F10);
        addKeyCodeAndIdentifier("F11",ItsNatKeyEvent.DOM_VK_F11);
        addKeyCodeAndIdentifier("F12",ItsNatKeyEvent.DOM_VK_F12);
        addKeyCodeAndIdentifier("F13",ItsNatKeyEvent.DOM_VK_F13);
        addKeyCodeAndIdentifier("F14",ItsNatKeyEvent.DOM_VK_F14);
        addKeyCodeAndIdentifier("F15",ItsNatKeyEvent.DOM_VK_F15);
        addKeyCodeAndIdentifier("F16",ItsNatKeyEvent.DOM_VK_F16);
        addKeyCodeAndIdentifier("F17",ItsNatKeyEvent.DOM_VK_F17);
        addKeyCodeAndIdentifier("F18",ItsNatKeyEvent.DOM_VK_F18);
        addKeyCodeAndIdentifier("F19",ItsNatKeyEvent.DOM_VK_F19);
        addKeyCodeAndIdentifier("F20",ItsNatKeyEvent.DOM_VK_F20);
        addKeyCodeAndIdentifier("F21",ItsNatKeyEvent.DOM_VK_F21);
        addKeyCodeAndIdentifier("F22",ItsNatKeyEvent.DOM_VK_F22);
        addKeyCodeAndIdentifier("F23",ItsNatKeyEvent.DOM_VK_F23);
        addKeyCodeAndIdentifier("F24",ItsNatKeyEvent.DOM_VK_F24);
        addKeyCodeAndIdentifier("NumLock",ItsNatKeyEvent.DOM_VK_NUM_LOCK);
        addKeyCodeAndIdentifier("Scroll",ItsNatKeyEvent.DOM_VK_SCROLL_LOCK);
        addKeyCodeAndIdentifier("U+002C",ItsNatKeyEvent.DOM_VK_COMMA);
        addKeyCodeAndIdentifier("U+002E",ItsNatKeyEvent.DOM_VK_PERIOD);
        addKeyCodeAndIdentifier("U+002F",ItsNatKeyEvent.DOM_VK_SLASH);
        addKeyCodeAndIdentifier("U+0060",ItsNatKeyEvent.DOM_VK_BACK_QUOTE);
        addKeyCodeAndIdentifier("U+005B",ItsNatKeyEvent.DOM_VK_OPEN_BRACKET);
        addKeyCodeAndIdentifier("U+005C",ItsNatKeyEvent.DOM_VK_BACK_SLASH);
        addKeyCodeAndIdentifier("U+005D",ItsNatKeyEvent.DOM_VK_CLOSE_BRACKET);
        addKeyCodeAndIdentifier("U+0060",ItsNatKeyEvent.DOM_VK_QUOTE);
        addKeyCodeAndIdentifier("Meta",ItsNatKeyEvent.DOM_VK_META);
    }


    /**
     * Creates a new instance of W3CKeyEventImpl
     */
    public BlackBerryOldKeyEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public static void addKeyCodeAndIdentifier(String keyIdentifier,int value)
    {
        delegate.addKeyCodeAndIdentifier(keyIdentifier, value);
    }

    public static String toKeyIdentifier(int keyCode)
    {
        return delegate.toKeyIdentifier(keyCode);
    }

    public static int toKeyCode(String keyIdentifier)
    {
        return delegate.toKeyCode(keyIdentifier);
    }

    public W3CKeyboardEventSharedImpl getW3CKeyboardEventShared()
    {
        return delegate;
    }

}
