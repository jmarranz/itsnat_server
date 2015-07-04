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
public class DOMStdEventGroupInfo
{
    // http://en.wikipedia.org/wiki/DOM_Events

    public static final int UNKNOWN_EVENT = 0;
    public static final int UI_EVENT = 1;
    public static final int MOUSE_EVENT = 2;
    public static final int HTML_EVENT = 3;
    public static final int MUTATION_EVENT = 4;
    public static final int KEY_EVENT = 5;

    protected static final Map<String,DOMStdEventGroupInfo> eventGroups = new HashMap<String,DOMStdEventGroupInfo>(); // no sincronizamos porque es sólo lectura
    protected static final DOMStdEventGroupInfo eventGroupUnknownDefault = new DOMStdEventGroupInfo(UNKNOWN_EVENT,true,true);

    static
    {
        // Según el W3C
        eventGroups.put("click",    new DOMStdEventGroupInfo(MOUSE_EVENT,true,true));
        eventGroups.put("dblclick", new DOMStdEventGroupInfo(MOUSE_EVENT,true,true));
        eventGroups.put("mousedown",new DOMStdEventGroupInfo(MOUSE_EVENT,true,true));
        eventGroups.put("mouseup",  new DOMStdEventGroupInfo(MOUSE_EVENT,true,true));
        eventGroups.put("mouseover",new DOMStdEventGroupInfo(MOUSE_EVENT,true,true));
        eventGroups.put("mousemove",new DOMStdEventGroupInfo(MOUSE_EVENT,true,false));
        eventGroups.put("mouseout", new DOMStdEventGroupInfo(MOUSE_EVENT,true,true));

        eventGroups.put("keypress", new DOMStdEventGroupInfo(KEY_EVENT,true,true));
        eventGroups.put("keydown",  new DOMStdEventGroupInfo(KEY_EVENT,true,true));
        eventGroups.put("keyup",    new DOMStdEventGroupInfo(KEY_EVENT,true,true));

        eventGroups.put("load",     new DOMStdEventGroupInfo(HTML_EVENT,false,false));
        eventGroups.put("unload",   new DOMStdEventGroupInfo(HTML_EVENT,false,false));
        eventGroups.put("abort",    new DOMStdEventGroupInfo(HTML_EVENT,true,false));
        eventGroups.put("error",    new DOMStdEventGroupInfo(HTML_EVENT,true,false));
        eventGroups.put("resize",   new DOMStdEventGroupInfo(HTML_EVENT,true,false));
        eventGroups.put("scroll",   new DOMStdEventGroupInfo(HTML_EVENT,true,false));
        eventGroups.put("select",   new DOMStdEventGroupInfo(HTML_EVENT,true,false));
        eventGroups.put("change",   new DOMStdEventGroupInfo(HTML_EVENT,true,false));
        eventGroups.put("submit",   new DOMStdEventGroupInfo(HTML_EVENT,true,true));
        eventGroups.put("reset",    new DOMStdEventGroupInfo(HTML_EVENT,true,false));
        eventGroups.put("focus",    new DOMStdEventGroupInfo(HTML_EVENT,false,false));
        eventGroups.put("blur",     new DOMStdEventGroupInfo(HTML_EVENT,false,false));

        eventGroups.put("DOMFocusIn",new DOMStdEventGroupInfo(UI_EVENT,true,false));
        eventGroups.put("DOMFocusOut",new DOMStdEventGroupInfo(UI_EVENT,true,false));
        eventGroups.put("DOMActivate",new DOMStdEventGroupInfo(UI_EVENT,true,true));

        eventGroups.put("DOMSubtreeModified",new DOMStdEventGroupInfo(MUTATION_EVENT,true,false));
        eventGroups.put("DOMNodeInserted",new DOMStdEventGroupInfo(MUTATION_EVENT,true,false));
        eventGroups.put("DOMNodeRemoved",new DOMStdEventGroupInfo(MUTATION_EVENT,true,false));
        eventGroups.put("DOMAttrModified",new DOMStdEventGroupInfo(MUTATION_EVENT,true,false));
        eventGroups.put("DOMCharacterDataModified",new DOMStdEventGroupInfo(MUTATION_EVENT,true,false));
        eventGroups.put("DOMNodeInsertedIntoDocument",new DOMStdEventGroupInfo(MUTATION_EVENT,false,false));
        eventGroups.put("DOMNodeRemovedFromDocument",new DOMStdEventGroupInfo(MUTATION_EVENT,false,false));

        // No W3C pero soportado por FireFox (desde 1.0) y MSIE y Safari (no Opera)
        eventGroups.put("beforeunload",new DOMStdEventGroupInfo(HTML_EVENT,false,true));

        // Soportado por FireFox, Opera 9, Safari y navegadores W3C en general
        eventGroups.put("DOMContentLoaded",new DOMStdEventGroupInfo(UNKNOWN_EVENT,false,false));
    }

    protected int eventGroupCode;
    protected boolean bubbles;
    protected boolean cancelable;

    /**
     * Creates a new instance of DOMStdEventGroupInfo
     */
    public DOMStdEventGroupInfo(int eventGroupCode,boolean bubbles,boolean cancelable)
    {
        this.eventGroupCode = eventGroupCode;
        this.bubbles = bubbles;
        this.cancelable = cancelable;
    }

    public static DOMStdEventGroupInfo getEventGroupInfo(String type)
    {
        DOMStdEventGroupInfo info = eventGroups.get(type);
        if (info == null) // evento desconocido
            info = eventGroupUnknownDefault;
        return info;
    }

    public static int getEventGroupCode(String type)
    {
        DOMStdEventGroupInfo info = eventGroups.get(type);
        if (info == null)
            return UNKNOWN_EVENT;
        return info.getEventGroupCode();
    }

    public int getEventGroupCode()
    {
        return eventGroupCode;
    }

    public boolean getBubbles()
    {
        return bubbles;
    }

    public boolean getCancelable()
    {
        return cancelable;
    }
}
