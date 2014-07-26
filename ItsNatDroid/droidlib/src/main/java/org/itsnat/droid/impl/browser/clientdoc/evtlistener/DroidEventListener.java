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
public class DroidEventListener extends NormalEventListener
{
    // Ver DroidEventGroupInfo en el código servidor

    public static final int UNKNOWN_EVENT = 0;
    public static final int MOTION_EVENT = 1;
    public static final int KEY_EVENT = 2;

    protected String type;
    protected boolean useCapture;
    protected int eventGroupCode;

    public DroidEventListener(ItsNatDocImpl parent, View view, String type, CustomFunction customFunc, String id, boolean useCapture, int commMode, long timeout, int eventGroupCode)
    {
        super(parent,"droid",view,customFunc,id,commMode,timeout);
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

    public NormalEvent createEventWrapper(Object evt)
    {
        switch(eventGroupCode)
        {
            case MOTION_EVENT: return new DroidMotionEvent(this,(MotionEvent)evt);
            case KEY_EVENT:   return new DroidKeyEvent(this,(KeyEvent)evt);
            default: throw new ItsNatDroidException("Event type not supported yet: " + type);
        }
    }

    public void genParamURL(EventGeneric evt,List<NameValuePair> params)
    {
        super.genParamURL(evt,params);
        params.add(new BasicNameValuePair("itsnat_evt_type", "" + this.type));
        //url.append( evt.genParamURL() );
    }
}
