package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.clientdoc.CustomFunction;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidFocusEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidKeyEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidMotionEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidOtherEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidTextChangeEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGenericImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.NormalEventImpl;

import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public class DroidEventListener extends NormalEventListener
{
    // Ver DroidEventGroupInfo en el código servidor

    public static final int UNKNOWN_EVENT = 0;
    public static final int MOTION_EVENT = 1;
    public static final int KEY_EVENT = 2;
    public static final int FOCUS_EVENT = 3;
    public static final int TEXT_CHANGE_EVENT = 4;
    public static final int OTHER_EVENT = 5;

    protected String type;
    protected boolean useCapture;
    protected int eventGroupCode;

    public DroidEventListener(ItsNatDocImpl parent, View currentTarget, String type, CustomFunction customFunc, String id, boolean useCapture, int commMode, long timeout, int eventGroupCode)
    {
        super(parent,"droid",currentTarget,customFunc,id,commMode,timeout);
        this.type = type;
        this.useCapture = useCapture;
        this.eventGroupCode = eventGroupCode;
    }

    public boolean isUseCapture()
    {
        return useCapture;
    }

    public String getType()
    {
        return type;
    }

    public NormalEventImpl createEventWrapper(Object evt)
    {
        switch(eventGroupCode)
        {
            case MOTION_EVENT: return new DroidMotionEventImpl(this,(MotionEvent)evt);
            case KEY_EVENT:    return new DroidKeyEventImpl(this,(KeyEvent)evt);
            case FOCUS_EVENT:  return new DroidFocusEventImpl(this,(Boolean)evt);
            case TEXT_CHANGE_EVENT:  return new DroidTextChangeEventImpl(this,(CharSequence)evt);
            case OTHER_EVENT:  return new DroidOtherEventImpl(this,evt);

            default: throw new ItsNatDroidException("Event type not supported yet: " + type);
        }
    }

    public void genParamURL(EventGenericImpl evt,List<NameValuePair> params)
    {
        super.genParamURL(evt,params);
        params.add(new BasicNameValuePair("itsnat_evt_type", "" + this.type));
    }
}
