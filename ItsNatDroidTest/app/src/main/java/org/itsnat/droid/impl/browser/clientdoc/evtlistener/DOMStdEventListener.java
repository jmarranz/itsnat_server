package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.clientdoc.CustomFunction;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DOMStdEvent;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGeneric;
import org.itsnat.droid.impl.browser.clientdoc.event.W3CKeyEvent;
import org.itsnat.droid.impl.browser.clientdoc.event.W3CMouseEvent;

import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public class DOMStdEventListener extends DOMEventListener
{
    // Ver DOMStdEventTypeInfo en el código servidor

    public static final int UNKNOWN_EVENT = 0;
    public static final int UI_EVENT = 1;
    public static final int MOUSE_EVENT = 2;
    public static final int HTML_EVENT = 3;
    public static final int MUTATION_EVENT = 4;
    public static final int KEY_EVENT = 5;

    protected String type;
    protected boolean useCapture;
    protected int typeCode;

    public DOMStdEventListener(ItsNatDocImpl parent,View view,String type,CustomFunction customFunc,String id,boolean useCapture,int commMode,long timeout,int typeCode)
    {
        super(parent,"domstd",view,customFunc,id,commMode,timeout);
        this.type = type;
        this.useCapture = useCapture;
        this.typeCode = typeCode;
    }

    public String getType()
    {
        return type;
    }

    public DOMStdEvent createEventWrapper(Object evt)
    {
        switch(typeCode)
        {
            case MOUSE_EVENT: return new W3CMouseEvent(this,(MotionEvent)evt);
            case KEY_EVENT:   return new W3CKeyEvent(this,(KeyEvent)evt);
            default: throw new ItsNatDroidException("Event type not supported yet:" + type);
        }
    }

    public void genParamURL(EventGeneric evt,List<NameValuePair> params)
    {
        super.genParamURL(evt,params);
        params.add(new BasicNameValuePair("itsnat_evt_type", "" + this.type));
        //url.append( evt.genParamURL() );
    }
}
