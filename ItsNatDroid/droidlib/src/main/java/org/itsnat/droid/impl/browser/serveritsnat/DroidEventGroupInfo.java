package org.itsnat.droid.impl.browser.serveritsnat;

import android.view.KeyEvent;
import android.view.MotionEvent;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidFocusEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidKeyEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidMotionEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidOtherEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidTextChangeEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.DroidEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 22/08/14.
 */

public class DroidEventGroupInfo
{
    // Copiado/basado en DroidEventGroupInfo en el código servidor, ¡¡¡¡¡mantener alineados!!!!!

    public static final int UNKNOWN_EVENT = 0;
    public static final int MOTION_EVENT = 1;
    public static final int KEY_EVENT = 2;
    public static final int FOCUS_EVENT = 3;
    public static final int TEXT_CHANGE_EVENT = 4;
    public static final int OTHER_EVENT = 5;

    protected static final Map<String,DroidEventGroupInfo> eventGroups = new HashMap<String,DroidEventGroupInfo>( 16 ); // no sincronizamos porque es sólo lectura
    protected static final DroidEventGroupInfo eventGroupUnknownDefault = new DroidEventGroupInfo(UNKNOWN_EVENT);

    static
    {
        // Según el W3C
        eventGroups.put("click",      new DroidEventGroupInfo(MOTION_EVENT));
        eventGroups.put("touchstart", new DroidEventGroupInfo(MOTION_EVENT));
        eventGroups.put("touchend",   new DroidEventGroupInfo(MOTION_EVENT));
        eventGroups.put("touchmove",  new DroidEventGroupInfo(MOTION_EVENT));
        eventGroups.put("touchcancel",new DroidEventGroupInfo(MOTION_EVENT));

        eventGroups.put("keydown",    new DroidEventGroupInfo(KEY_EVENT));
        eventGroups.put("keyup",      new DroidEventGroupInfo(KEY_EVENT));

        eventGroups.put("focus",      new DroidEventGroupInfo(FOCUS_EVENT));
        eventGroups.put("blur",       new DroidEventGroupInfo(FOCUS_EVENT));

        eventGroups.put("change",     new DroidEventGroupInfo(TEXT_CHANGE_EVENT));

        eventGroups.put("load",       new DroidEventGroupInfo(OTHER_EVENT));
        eventGroups.put("unload",     new DroidEventGroupInfo(OTHER_EVENT));
    }

    protected int eventGroupCode;

    /**
     * Creates a new instance of DroidEventGroupInfo
     */
    public DroidEventGroupInfo(int eventGroupCode)
    {
        this.eventGroupCode = eventGroupCode;
    }

    public static DroidEventGroupInfo getEventGroupInfo(String type)
    {
        DroidEventGroupInfo info = eventGroups.get(type);
        if (info == null) // evento desconocido
            info = eventGroupUnknownDefault;
        return info;
    }

    public static int getEventGroupCode(String type)
    {
        DroidEventGroupInfo info = eventGroups.get(type);
        if (info == null)
            return UNKNOWN_EVENT;
        return info.getEventGroupCode();
    }

    public int getEventGroupCode()
    {
        return eventGroupCode;
    }

    /*
    public static String getEventGroup(Event evt)
    {
        // No se usa todavía
        if (evt instanceof DroidEvent)
        {
            if (evt instanceof DroidMotionEvent)
                return "MotionEvent";
            else if (evt instanceof DroidKeyEvent)
                return "KeyEvent";
            else if (evt instanceof DroidFocusEvent)
                return "FocusEvent";
            else if (evt instanceof DroidTextChangeEvent)
                return "TextChangeEvent";
            else if (evt instanceof DroidOtherEvent)
                return "OtherEvent";
        }
        return null;
    }
*/

    public static DroidEventImpl createDroidEvent(int eventGroupCode, DroidEventListener listener, Object evt)
    {
        switch(eventGroupCode)
        {
            case MOTION_EVENT: return new DroidMotionEventImpl(listener,(MotionEvent)evt);
            case KEY_EVENT:    return new DroidKeyEventImpl(listener,(KeyEvent)evt);
            case FOCUS_EVENT:  return new DroidFocusEventImpl(listener,(Boolean)evt);
            case TEXT_CHANGE_EVENT:  return new DroidTextChangeEventImpl(listener,(CharSequence)evt);
            case OTHER_EVENT:  return new DroidOtherEventImpl(listener,evt);

            default: throw new ItsNatDroidException("Event type not supported yet: " + listener.getType());
        }
    }
}
