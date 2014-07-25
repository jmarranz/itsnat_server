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
public class DroidEventGroupInfo
{
    public static final int UNKNOWN_EVENT = 0;
    public static final int MOTION_EVENT = 1;
    public static final int KEY_EVENT = 2;

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

}
