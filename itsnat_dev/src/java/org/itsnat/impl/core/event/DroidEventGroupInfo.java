/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.event;

import java.util.HashMap;
import java.util.Map;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.droid.DroidEvent;
import org.itsnat.core.event.droid.DroidFocusEvent;
import org.itsnat.core.event.droid.DroidKeyEvent;
import org.itsnat.core.event.droid.DroidMotionEvent;
import org.itsnat.core.event.droid.DroidTextChangeEvent;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.client.droid.ClientItsNatDroidEventImpl;
import org.itsnat.impl.core.event.client.droid.ClientItsNatDroidFocusEventImpl;
import org.itsnat.impl.core.event.client.droid.ClientItsNatDroidKeyEventImpl;
import org.itsnat.impl.core.event.client.droid.ClientItsNatDroidMotionEventImpl;
import org.itsnat.impl.core.event.client.droid.ClientItsNatDroidOtherEventImpl;
import org.itsnat.impl.core.event.client.droid.ClientItsNatDroidTextChangeEventImpl;
import org.itsnat.impl.core.event.server.droid.ServerItsNatDroidEventImpl;
import org.itsnat.impl.core.event.server.droid.ServerItsNatDroidFocusEventImpl;
import org.itsnat.impl.core.event.server.droid.ServerItsNatDroidKeyEventImpl;
import org.itsnat.impl.core.event.server.droid.ServerItsNatDroidMotionEventImpl;
import org.itsnat.impl.core.event.server.droid.ServerItsNatDroidOtherEventImpl;
import org.itsnat.impl.core.event.server.droid.ServerItsNatDroidTextChangeEventImpl;
import org.itsnat.impl.core.listener.droid.ItsNatDroidEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class DroidEventGroupInfo
{
    public static final int UNKNOWN_EVENT = 0;
    public static final int MOTION_EVENT = 1;
    public static final int KEY_EVENT = 2;
    public static final int FOCUS_EVENT = 3;
    public static final int TEXT_CHANGE_EVENT = 4;    
    public static final int OTHER_EVENT = 5;
        
    protected static final Map<String,DroidEventGroupInfo> eventGroups = new HashMap<String,DroidEventGroupInfo>(); // no sincronizamos porque es sólo lectura
    protected static final DroidEventGroupInfo eventGroupUnknownDefault = new DroidEventGroupInfo(UNKNOWN_EVENT);

    static
    {
        // Según el W3C
        eventGroups.put("click",      new DroidEventGroupInfo(MOTION_EVENT));
        eventGroups.put("touchstart", new DroidEventGroupInfo(MOTION_EVENT));
        eventGroups.put("touchend",   new DroidEventGroupInfo(MOTION_EVENT));        
        eventGroups.put("touchmove",  new DroidEventGroupInfo(MOTION_EVENT)); 
        eventGroups.put("touchcancel",new DroidEventGroupInfo(MOTION_EVENT)); 
        
        eventGroups.put("keydown",  new DroidEventGroupInfo(KEY_EVENT));
        eventGroups.put("keyup",    new DroidEventGroupInfo(KEY_EVENT));
        
        eventGroups.put("focus",  new DroidEventGroupInfo(FOCUS_EVENT));
        eventGroups.put("blur",   new DroidEventGroupInfo(FOCUS_EVENT));        
        
        eventGroups.put("change",   new DroidEventGroupInfo(TEXT_CHANGE_EVENT));        
       
        eventGroups.put("unload",   new DroidEventGroupInfo(OTHER_EVENT));                
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

    public static ClientItsNatDroidEventImpl createClientItsNatDroidEvent(String type,ItsNatDroidEventListenerWrapperImpl listener,RequestNormalEventImpl request)
    {
        if ("click".equals(type) ||
            "touchstart".equals(type) || 
            "touchend".equals(type) ||
            "touchmove".equals(type) ||
            "touchcancel".equals(type))
            return new ClientItsNatDroidMotionEventImpl(listener,request);
        else if ("keydown".equals(type) ||
                 "keyup".equals(type))
            return new ClientItsNatDroidKeyEventImpl(listener,request);
        else if ("focus".equals(type) ||
                 "blur".equals(type))
            return new ClientItsNatDroidFocusEventImpl(listener,request);        
        else if ("change".equals(type))
            return new ClientItsNatDroidTextChangeEventImpl(listener,request);        
        else if ("unload".equals(type))
            return new ClientItsNatDroidOtherEventImpl(listener,request);         
        return null;
    }    
    
    public static ServerItsNatDroidEventImpl createServerItsNatDroidEvent(String eventGroup,ItsNatStfulDocumentImpl itsNatDoc) throws DOMException
    {
        if ("MotionEvent".equals(eventGroup))
            return new ServerItsNatDroidMotionEventImpl(itsNatDoc);
        else if ("KeyEvent".equals(eventGroup))
            return new ServerItsNatDroidKeyEventImpl(itsNatDoc);        
        else if ("FocusEvent".equals(eventGroup))
            return new ServerItsNatDroidFocusEventImpl(itsNatDoc);        
        else if ("TextChangeEvent".equals(eventGroup))
            return new ServerItsNatDroidTextChangeEventImpl(itsNatDoc);         
        else if ("OtherEvent".equals(eventGroup))
            return new ServerItsNatDroidOtherEventImpl(itsNatDoc);        
        throw new ItsNatException("Event name " + eventGroup + " is unknown");
    }    
    
    public static String getEventGroup(Event evt)
    {
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
}
