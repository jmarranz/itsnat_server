package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.clientdoc.CustomFunction;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidKeyEvent;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidMotionEvent;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGeneric;
import org.itsnat.droid.impl.browser.clientdoc.event.NormalEvent;

import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public abstract class DOMExtEventListener extends NormalEventListener
{

    public DOMExtEventListener(ItsNatDocImpl parent,String eventType, View view, CustomFunction customFunc, String id, int commMode, long timeout)
    {
        super(parent,eventType,view,customFunc,id,commMode,timeout);
    }

}
