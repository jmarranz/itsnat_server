package org.itsnat.droid.impl.browser.clientdoc.event;

import android.view.KeyEvent;
import android.view.MotionEvent;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;

/**
 * Created by jmarranz on 8/07/14.
 */
public class W3CKeyEvent extends W3CUIEvent
{
    public W3CKeyEvent(DOMStdEventListener listener, KeyEvent evtNative)
    {
        super(listener,evtNative);
    }

    public KeyEvent getKeyEvent()
    {
        return (KeyEvent)evtNative;
    }

    public StringBuilder genParamURL()
    {
        StringBuilder url = super.genParamURL();

        KeyEvent evt = getKeyEvent();

        boolean altKey = evt.isAltPressed();
        boolean ctrlKey = evt.isCtrlPressed();
        boolean metaKey = evt.isMetaPressed();
        boolean shiftKey = evt.isShiftPressed();

        int keyCode = evt.getKeyCode();
        int charCode = evt.getUnicodeChar();

        /*
        var charCode = 0;
        if (evt.type == "keypress")
        {
            charCode = evt.charCode;
            if (typeof charCode == "undefined") charCode = 0; // Opera y BlackBerryOld no tienen
            if ((charCode == 0) && evt.itsnat_charCode) charCode = evt.itsnat_charCode; // WebKit y BlackBerryOld
        }
        */

        url.append( "&itsnat_evt_charCode=" + charCode );

        /*
        if (itsNatDoc.browser.isWebKit()||itsNatDoc.browser.isBlackBerryOld())
        {
            url += "&itsnat_evt_keyIdentifier=" + encodeURIComponent(evt.keyIdentifier);
            url += "&itsnat_evt_keyLocation=" + evt.keyLocation;
        }
        */

        url.append( "&itsnat_evt_keyCode="  + keyCode ); // evt.keyCode;
        url.append( "&itsnat_evt_altKey="   + altKey ); // evt.altKey;
        url.append( "&itsnat_evt_ctrlKey="  + ctrlKey ); // evt.ctrlKey;
        url.append( "&itsnat_evt_metaKey="  + metaKey ); // evt.metaKey;
        url.append( "&itsnat_evt_shiftKey=" + shiftKey ); // evt.shiftKey;
        return url;
    }
}
