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

package org.itsnat.impl.core.event;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jmarranz
 */
public class DroidEventTypeInfo
{
    public static final int UNKNOWN_EVENT = 0;
    public static final int MOTION_EVENT = 1;
    public static final int KEY_EVENT = 2;

    protected static final Map<String,DroidEventTypeInfo> eventTypes = new HashMap<String,DroidEventTypeInfo>(); // no sincronizamos porque es sólo lectura
    protected static final DroidEventTypeInfo eventTypeUnknownDefault = new DroidEventTypeInfo(UNKNOWN_EVENT);

    static
    {
        // Según el W3C
        eventTypes.put("click",      new DroidEventTypeInfo(MOTION_EVENT));
        eventTypes.put("touchstart", new DroidEventTypeInfo(MOTION_EVENT));
        eventTypes.put("touchend",   new DroidEventTypeInfo(MOTION_EVENT));        
        eventTypes.put("touchmove",  new DroidEventTypeInfo(MOTION_EVENT)); 
        eventTypes.put("touchcancel",new DroidEventTypeInfo(MOTION_EVENT)); 
        
        eventTypes.put("keydown",  new DroidEventTypeInfo(KEY_EVENT));
        eventTypes.put("keyup",    new DroidEventTypeInfo(KEY_EVENT));
    }

    protected int typeCode;

    /**
     * Creates a new instance of DOMStdEventTypeInfo
     */
    public DroidEventTypeInfo(int typeCode)
    {
        this.typeCode = typeCode;
    }

    public static DroidEventTypeInfo getEventTypeInfo(String type)
    {
        DroidEventTypeInfo info = eventTypes.get(type);
        if (info == null) // evento desconocido
            info = eventTypeUnknownDefault;
        return info;
    }

    public static int getEventTypeCode(String type)
    {
        DroidEventTypeInfo info = eventTypes.get(type);
        if (info == null)
            return UNKNOWN_EVENT;
        return info.getTypeCode();
    }

    public int getTypeCode()
    {
        return typeCode;
    }

}
