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
 *
 * @author jmarranz
 */
public class WebKitKeyEventImpl extends W3CKeyboardEventImpl implements ItsNatKeyEvent
{
    protected static final W3CKeyboardEventSharedImpl delegate = new W3CKeyboardEventSharedImpl();

    // http://doc.ddart.net/msdn/header/include/winuser.h.html
    //  Básicamente coinciden con los de ItsNatKeyEvent

    // begin_r_winuser

    /*
     * Virtual Keys, Standard Set
     */
    public static final int VK_LBUTTON        = 0x01;
    public static final int VK_RBUTTON        = 0x02;
    public static final int VK_CANCEL         = 0x03;
    public static final int VK_MBUTTON        = 0x04;    /* NOT contiguous with L & RBUTTON */

    public static final int VK_BACK           = 0x08;
    public static final int VK_TAB            = 0x09;

    public static final int VK_CLEAR          = 0x0C;
    public static final int VK_RETURN         = 0x0D;

    public static final int VK_SHIFT          = 0x10;
    public static final int VK_CONTROL        = 0x11;
    public static final int VK_MENU           = 0x12;
    public static final int VK_PAUSE          = 0x13;
    public static final int VK_CAPITAL        = 0x14;

    public static final int VK_KANA           = 0x15;
    public static final int VK_HANGEUL        = 0x15;  /* old name - should be here for compatibility */
    public static final int VK_HANGUL         = 0x15;
    public static final int VK_JUNJA          = 0x17;
    public static final int VK_FINAL          = 0x18;
    public static final int VK_HANJA          = 0x19;
    public static final int VK_KANJI          = 0x19;

    public static final int VK_ESCAPE         = 0x1B;

    public static final int VK_CONVERT        = 0x1C;
    public static final int VK_NONCONVERT     = 0x1D;
    public static final int VK_ACCEPT         = 0x1E;
    public static final int VK_MODECHANGE     = 0x1F;

    public static final int VK_SPACE          = 0x20;
    public static final int VK_PRIOR          = 0x21;
    public static final int VK_NEXT           = 0x22;
    public static final int VK_END            = 0x23;
    public static final int VK_HOME           = 0x24;
    public static final int VK_LEFT           = 0x25;
    public static final int VK_UP             = 0x26;
    public static final int VK_RIGHT          = 0x27;
    public static final int VK_DOWN           = 0x28;
    public static final int VK_SELECT         = 0x29;
    public static final int VK_PRINT          = 0x2A;
    public static final int VK_EXECUTE        = 0x2B;
    public static final int VK_SNAPSHOT       = 0x2C;
    public static final int VK_INSERT         = 0x2D;
    public static final int VK_DELETE         = 0x2E;
    public static final int VK_HELP           = 0x2F;

    /* VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39) */
    /* VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A) */

    public static final int VK_LWIN           = 0x5B;
    public static final int VK_RWIN           = 0x5C;
    public static final int VK_APPS           = 0x5D;

    public static final int VK_NUMPAD0        = 0x60;
    public static final int VK_NUMPAD1        = 0x61;
    public static final int VK_NUMPAD2        = 0x62;
    public static final int VK_NUMPAD3        = 0x63;
    public static final int VK_NUMPAD4        = 0x64;
    public static final int VK_NUMPAD5        = 0x65;
    public static final int VK_NUMPAD6        = 0x66;
    public static final int VK_NUMPAD7        = 0x67;
    public static final int VK_NUMPAD8        = 0x68;
    public static final int VK_NUMPAD9        = 0x69;
    public static final int VK_MULTIPLY       = 0x6A;
    public static final int VK_ADD            = 0x6B;
    public static final int VK_SEPARATOR      = 0x6C;
    public static final int VK_SUBTRACT       = 0x6D;
    public static final int VK_DECIMAL        = 0x6E;
    public static final int VK_DIVIDE         = 0x6F;
    public static final int VK_F1             = 0x70;
    public static final int VK_F2             = 0x71;
    public static final int VK_F3             = 0x72;
    public static final int VK_F4             = 0x73;
    public static final int VK_F5             = 0x74;
    public static final int VK_F6             = 0x75;
    public static final int VK_F7             = 0x76;
    public static final int VK_F8             = 0x77;
    public static final int VK_F9             = 0x78;
    public static final int VK_F10            = 0x79;
    public static final int VK_F11            = 0x7A;
    public static final int VK_F12            = 0x7B;
    public static final int VK_F13            = 0x7C;
    public static final int VK_F14            = 0x7D;
    public static final int VK_F15            = 0x7E;
    public static final int VK_F16            = 0x7F;
    public static final int VK_F17            = 0x80;
    public static final int VK_F18            = 0x81;
    public static final int VK_F19            = 0x82;
    public static final int VK_F20            = 0x83;
    public static final int VK_F21            = 0x84;
    public static final int VK_F22            = 0x85;
    public static final int VK_F23            = 0x86;
    public static final int VK_F24            = 0x87;

    public static final int VK_NUMLOCK        = 0x90;
    public static final int VK_SCROLL         = 0x91;

    /*
     * VK_L* & VK_R* - left and right Alt, Ctrl and Shift virtual keys.
     * Used only as parameters to GetAsyncKeyState() and GetKeyState().
     * No other API or message will distinguish left and right keys in this way.
     */
    public static final int VK_LSHIFT         = 0xA0;
    public static final int VK_RSHIFT         = 0xA1;
    public static final int VK_LCONTROL       = 0xA2;
    public static final int VK_RCONTROL       = 0xA3;
    public static final int VK_LMENU          = 0xA4;
    public static final int VK_RMENU          = 0xA5;

    public static final int VK_PROCESSKEY     = 0xE5;


    public static final int VK_ATTN           = 0xF6;
    public static final int VK_CRSEL          = 0xF7;
    public static final int VK_EXSEL          = 0xF8;
    public static final int VK_EREOF          = 0xF9;
    public static final int VK_PLAY           = 0xFA;
    public static final int VK_ZOOM           = 0xFB;
    public static final int VK_NONAME         = 0xFC;
    public static final int VK_PA1            = 0xFD;
    public static final int VK_OEM_CLEAR      = 0xFE;

    // end_r_winuser

    static
    {
        // http://www.koders.com/cpp/fidCA6C5BC570CBFE6A97F78D2A65217CBECDB4ED88.aspx?s=PlatformKeyboardEvent#L27

        addKeyCodeAndIdentifier("Alt",VK_MENU);
        addKeyCodeAndIdentifier("Clear",VK_CLEAR);
        addKeyCodeAndIdentifier("Down",VK_DOWN);
        addKeyCodeAndIdentifier("End",VK_END);
        addKeyCodeAndIdentifier("Enter",VK_RETURN);
        addKeyCodeAndIdentifier("Execute",VK_EXECUTE);
        addKeyCodeAndIdentifier("F1",VK_F1);
        addKeyCodeAndIdentifier("F2",VK_F2);
        addKeyCodeAndIdentifier("F3",VK_F3);
        addKeyCodeAndIdentifier("F4",VK_F4);
        addKeyCodeAndIdentifier("F5",VK_F5);
        addKeyCodeAndIdentifier("F6",VK_F6);
        addKeyCodeAndIdentifier("F7",VK_F7);
        addKeyCodeAndIdentifier("F8",VK_F8);
        addKeyCodeAndIdentifier("F9",VK_F9);
        addKeyCodeAndIdentifier("F10",VK_F10);
        addKeyCodeAndIdentifier("F11",VK_F11);
        addKeyCodeAndIdentifier("F12",VK_F12);
        addKeyCodeAndIdentifier("F13",VK_F13);
        addKeyCodeAndIdentifier("F14",VK_F14);
        addKeyCodeAndIdentifier("F15",VK_F15);
        addKeyCodeAndIdentifier("F16",VK_F16);
        addKeyCodeAndIdentifier("F17",VK_F17);
        addKeyCodeAndIdentifier("F18",VK_F18);
        addKeyCodeAndIdentifier("F19",VK_F19);
        addKeyCodeAndIdentifier("F20",VK_F20);
        addKeyCodeAndIdentifier("F21",VK_F21);
        addKeyCodeAndIdentifier("F22",VK_F22);
        addKeyCodeAndIdentifier("F23",VK_F23);
        addKeyCodeAndIdentifier("F24",VK_F24);
        addKeyCodeAndIdentifier("Help",VK_HELP);
        addKeyCodeAndIdentifier("Home",VK_HOME);
        addKeyCodeAndIdentifier("Insert",VK_INSERT);
        addKeyCodeAndIdentifier("Left",VK_LEFT);
        addKeyCodeAndIdentifier("PageDown",VK_NEXT);
        addKeyCodeAndIdentifier("PageUp",VK_PRIOR);
        addKeyCodeAndIdentifier("Pause",VK_PAUSE);
        addKeyCodeAndIdentifier("PrintScreen",VK_SNAPSHOT);
        addKeyCodeAndIdentifier("Right",VK_RIGHT);
        addKeyCodeAndIdentifier("Scroll",VK_SCROLL);
        addKeyCodeAndIdentifier("Select",VK_SELECT);
        addKeyCodeAndIdentifier("Up",VK_UP);
        addKeyCodeAndIdentifier("U+007F",VK_DELETE);
    }


    /**
     * Creates a new instance of W3CKeyEventImpl
     */
    public WebKitKeyEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
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


    public W3CKeyboardEventSharedImpl getW3CKeyboardEventShared()
    {
        return delegate;
    }
}
