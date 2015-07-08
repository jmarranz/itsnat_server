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

import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

/**
 * This interface is a Java DOM version of <a href="http://lxr.mozilla.org/seamonkey/source/dom/public/idl/events/nsIDOMKeyEvent.idl">Mozilla/FireFox's ItsNatKeyEvent</a>
 * interface because there is no W3C DOM Level 2 key event interface.
 *
 * <p>FireFox's key event implementation is considered de facto standard.
 * Is based on an early version of
 * <a href="http://www.w3.org/TR/1999/WD-DOM-Level-2-19990923/events.html#Events-KeyEvent">DOM Level 2</a> (key event specification was removed later)
 * as states <a href="http://developer.mozilla.org/en/docs/DOM:event.initKeyEvent">Mozilla developer center</a>
 * and is similar to <a href="http://www.w3.org/TR/DOM-Level-3-Events/events.html#Events-KeyboardEvent">W3C DOM Level 3 KeyboardEvent</a>.</p>
 *
 * <p>MSIE 6, Safari and Opera key events are converted to this event type.</p>
 *
 * <p>To create programatically a key event use <code>DocumentEvent.createEvent(String)}</code>
 * ({@link org.itsnat.core.ItsNatNode#isInternalMode()} must return false)
 * or {@link org.itsnat.core.ItsNatDocument#createEvent(String)}
 * with "ItsNatKeyEvent" or "KeyEvents" as parameter. "KeyboardEvent", "KeyboardEvents" names
 * are reserved for future, final, DOM 3 implementation (DOM 3 is still a draft).
 * </p>
 *
 * <p>Default implementation inherits from <code>java.util.EventObject</code>.</p>
 *
 * <p>More info <a href="http://developer.mozilla.org/en/docs/DOM:event">here</a>
 * and <a href="http://developer.mozilla.org/en/DOM/Event/UIEvent/KeyEvent">here</a>.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatKeyEvent extends UIEvent,ItsNatDOMStdEvent
{
    public int DOM_VK_CANCEL    =0x03;
    public int DOM_VK_HELP      =0x06;
    public int DOM_VK_BACK_SPACE=0x08;
    public int DOM_VK_TAB       =0x09;
    public int DOM_VK_CLEAR     =0x0C;
    public int DOM_VK_RETURN    =0x0D;
    public int DOM_VK_ENTER     =0x0E;
    public int DOM_VK_SHIFT     =0x10;
    public int DOM_VK_CONTROL   =0x11;
    public int DOM_VK_ALT       =0x12;
    public int DOM_VK_PAUSE     =0x13;
    public int DOM_VK_CAPS_LOCK =0x14;
    public int DOM_VK_ESCAPE    =0x1B;
    public int DOM_VK_SPACE     =0x20;
    public int DOM_VK_PAGE_UP   =0x21;
    public int DOM_VK_PAGE_DOWN =0x22;
    public int DOM_VK_END       =0x23;
    public int DOM_VK_HOME      =0x24;
    public int DOM_VK_LEFT      =0x25;
    public int DOM_VK_UP        =0x26;
    public int DOM_VK_RIGHT     =0x27;
    public int DOM_VK_DOWN      =0x28;
    public int DOM_VK_PRINTSCREEN=0x2C;
    public int DOM_VK_INSERT    =0x2D;
    public int DOM_VK_DELETE    =0x2E;
    public int DOM_VK_0=0x30;
    public int DOM_VK_1=0x31;
    public int DOM_VK_2=0x32;
    public int DOM_VK_3=0x33;
    public int DOM_VK_4=0x34;
    public int DOM_VK_5=0x35;
    public int DOM_VK_6=0x36;
    public int DOM_VK_7=0x37;
    public int DOM_VK_8=0x38;
    public int DOM_VK_9=0x39;
    public int DOM_VK_SEMICOLON =0x3B;
    public int DOM_VK_EQUALS    =0x3D;
    public int DOM_VK_A=0x41;
    public int DOM_VK_B=0x42;
    public int DOM_VK_C=0x43;
    public int DOM_VK_D=0x44;
    public int DOM_VK_E=0x45;
    public int DOM_VK_F=0x46;
    public int DOM_VK_G=0x47;
    public int DOM_VK_H=0x48;
    public int DOM_VK_I=0x49;
    public int DOM_VK_J=0x4A;
    public int DOM_VK_K=0x4B;
    public int DOM_VK_L=0x4C;
    public int DOM_VK_M=0x4D;
    public int DOM_VK_N=0x4E;
    public int DOM_VK_O=0x4F;
    public int DOM_VK_P=0x50;
    public int DOM_VK_Q=0x51;
    public int DOM_VK_R=0x52;
    public int DOM_VK_S=0x53;
    public int DOM_VK_T=0x54;
    public int DOM_VK_U=0x55;
    public int DOM_VK_V=0x56;
    public int DOM_VK_W=0x57;
    public int DOM_VK_X=0x58;
    public int DOM_VK_Y=0x59;
    public int DOM_VK_Z=0x5A;
    public int DOM_VK_CONTEXT_MENU=0x5D;
    public int DOM_VK_NUMPAD0=0x60;
    public int DOM_VK_NUMPAD1=0x61;
    public int DOM_VK_NUMPAD2=0x62;
    public int DOM_VK_NUMPAD3=0x63;
    public int DOM_VK_NUMPAD4=0x64;
    public int DOM_VK_NUMPAD5=0x65;
    public int DOM_VK_NUMPAD6=0x66;
    public int DOM_VK_NUMPAD7=0x67;
    public int DOM_VK_NUMPAD8=0x68;
    public int DOM_VK_NUMPAD9=0x69;
    public int DOM_VK_MULTIPLY  =0x6A;
    public int DOM_VK_ADD       =0x6B;
    public int DOM_VK_SEPARATOR =0x6C;
    public int DOM_VK_SUBTRACT  =0x6D;
    public int DOM_VK_DECIMAL   =0x6E;
    public int DOM_VK_DIVIDE    =0x6F;
    public int DOM_VK_F1 =0x70;
    public int DOM_VK_F2 =0x71;
    public int DOM_VK_F3 =0x72;
    public int DOM_VK_F4 =0x73;
    public int DOM_VK_F5 =0x74;
    public int DOM_VK_F6 =0x75;
    public int DOM_VK_F7 =0x76;
    public int DOM_VK_F8 =0x77;
    public int DOM_VK_F9 =0x78;
    public int DOM_VK_F10=0x79;
    public int DOM_VK_F11=0x7A;
    public int DOM_VK_F12=0x7B;
    public int DOM_VK_F13=0x7C;
    public int DOM_VK_F14=0x7D;
    public int DOM_VK_F15=0x7E;
    public int DOM_VK_F16=0x7F;
    public int DOM_VK_F17=0x80;
    public int DOM_VK_F18=0x81;
    public int DOM_VK_F19=0x82;
    public int DOM_VK_F20=0x83;
    public int DOM_VK_F21=0x84;
    public int DOM_VK_F22=0x85;
    public int DOM_VK_F23=0x86;
    public int DOM_VK_F24=0x87;
    public int DOM_VK_NUM_LOCK      =0x90;
    public int DOM_VK_SCROLL_LOCK   =0x91;
    public int DOM_VK_COMMA         =0xBC;
    public int DOM_VK_PERIOD        =0xBE;
    public int DOM_VK_SLASH         =0xBF;
    public int DOM_VK_BACK_QUOTE    =0xC0;
    public int DOM_VK_OPEN_BRACKET  =0xDB;
    public int DOM_VK_BACK_SLASH    =0xDC;
    public int DOM_VK_CLOSE_BRACKET =0xDD;
    public int DOM_VK_QUOTE         =0xDE;
    public int DOM_VK_META          =0xE0;


    /**
     * The <code>initKeyEvent</code> method is used to initialize the value of
     * a <code>ItsNatKeyEvent</code>.
     *
     * <p>This method may only be called before the
     * <code>ItsNatKeyEvent</code> has been dispatched via the
     * <code>dispatchEvent</code> method, though it may be called multiple
     * times during that phase if necessary. If called multiple times, the
     * final invocation takes precedence.
     * </p>
     *
     * @param typeArg specifies the event type.
     * @param canBubbleArg specifies whether or not the event can bubble.
     * @param cancelableArg specifies whether or not the event's default
     *   action can be prevented.
     * @param viewArg specifies the <code>Event</code>'s <code>AbstractView</code>.
     * @param ctrlKeyArg specifies whether or not control key was depressed
     *   during the <code>Event</code>.
     * @param altKeyArg specifies whether or not alt key was depressed during
     *   the <code>Event</code>.
     * @param shiftKeyArg specifies whether or not shift key was depressed
     *   during the <code>Event</code>.
     * @param metaKeyArg specifies whether or not meta key was depressed
     *   during the <code>Event</code>.
     * @param keyCodeArg specifies the Unicode value of a non-character key in a keypress event or any key in any other type of keyboard event.
     * @param charCodeArg specifies the Unicode value of a character key that was pressed as part of a keypress event.
     */
    public void initKeyEvent(String typeArg,boolean canBubbleArg,boolean cancelableArg,AbstractView viewArg,boolean ctrlKeyArg,boolean altKeyArg,boolean shiftKeyArg,boolean metaKeyArg,int keyCodeArg,int charCodeArg);

    /**
     * If the alternative (Alt) key modifier is activated.
     *
     * @return true if Alt is activated.
     */
    public boolean getAltKey();

    /**
     * Returns the Unicode value of a character key that was pressed as part of a keypress event.
     *
     * @return the character code.
     */
    public int getCharCode();

    /**
     * If the Ctrl key modifier is activated.
     *
     * @return true if Ctrl is activated.
     */
    public boolean getCtrlKey();

    /**
     * Returns the Unicode value of a non-character key in a keypress event or any key in any other type of keyboard event.
     *
     * @return the key code.
     */
    public int getKeyCode();

    /**
     * If the Meta key modifier is activated. Only Mac keyboards.
     *
     * @return true if Meta is activated.
     */
    public boolean getMetaKey();

    /**
     * If the Shift key modifier is activated.
     *
     * @return true if Shift is activated.
     */
    public boolean getShiftKey();

}
