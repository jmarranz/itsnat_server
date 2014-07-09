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

    /*
    function genParamURL()
    {
        var url = this.W3CKeyEventUtil_super_genParamURL(evt,itsNatDoc);

        var charCode = 0;
        if (evt.type == "keypress")
        {
            charCode = evt.charCode;
            if (typeof charCode == "undefined") charCode = 0; // Opera y BlackBerryOld no tienen
            if ((charCode == 0) && evt.itsnat_charCode) charCode = evt.itsnat_charCode; // WebKit y BlackBerryOld
        }
        url += "&itsnat_evt_charCode=" + charCode;

        if (itsNatDoc.browser.isWebKit()||itsNatDoc.browser.isBlackBerryOld())
        {
            url += "&itsnat_evt_keyIdentifier=" + encodeURIComponent(evt.keyIdentifier);
            url += "&itsnat_evt_keyLocation=" + evt.keyLocation;
        }
        url += "&itsnat_evt_keyCode=" + evt.keyCode;
        url += "&itsnat_evt_altKey=" + evt.altKey;
        url += "&itsnat_evt_ctrlKey=" + evt.ctrlKey;
        url += "&itsnat_evt_metaKey=" + evt.metaKey;
        url += "&itsnat_evt_shiftKey=" + evt.shiftKey;
        return url;
    }
    */
}
