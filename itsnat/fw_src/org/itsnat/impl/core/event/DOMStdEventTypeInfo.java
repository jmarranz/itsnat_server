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
public class DOMStdEventTypeInfo
{
    // http://en.wikipedia.org/wiki/DOM_Events

    public static final int UNKNOWN_EVENT = 0;
    public static final int UI_EVENT = 1;
    public static final int MOUSE_EVENT = 2;
    public static final int HTML_EVENT = 3;
    public static final int MUTATION_EVENT = 4;
    public static final int KEY_EVENT = 5;

    protected static final Map eventTypes = new HashMap(); // no sincronizamos porque es sólo lectura
    protected static final DOMStdEventTypeInfo eventTypeUnknownDefault = new DOMStdEventTypeInfo(UNKNOWN_EVENT,true,true);

    static
    {
        // Según el W3C
        eventTypes.put("click",    new DOMStdEventTypeInfo(MOUSE_EVENT,true,true));
        eventTypes.put("dblclick", new DOMStdEventTypeInfo(MOUSE_EVENT,true,true));
        eventTypes.put("mousedown",new DOMStdEventTypeInfo(MOUSE_EVENT,true,true));
        eventTypes.put("mouseup",  new DOMStdEventTypeInfo(MOUSE_EVENT,true,true));
        eventTypes.put("mouseover",new DOMStdEventTypeInfo(MOUSE_EVENT,true,true));
        eventTypes.put("mousemove",new DOMStdEventTypeInfo(MOUSE_EVENT,true,false));
        eventTypes.put("mouseout", new DOMStdEventTypeInfo(MOUSE_EVENT,true,true));

        eventTypes.put("keypress", new DOMStdEventTypeInfo(KEY_EVENT,true,true));
        eventTypes.put("keydown",  new DOMStdEventTypeInfo(KEY_EVENT,true,true));
        eventTypes.put("keyup",    new DOMStdEventTypeInfo(KEY_EVENT,true,true));

        eventTypes.put("load",     new DOMStdEventTypeInfo(HTML_EVENT,false,false));
        eventTypes.put("unload",   new DOMStdEventTypeInfo(HTML_EVENT,false,false));
        eventTypes.put("abort",    new DOMStdEventTypeInfo(HTML_EVENT,true,false));
        eventTypes.put("error",    new DOMStdEventTypeInfo(HTML_EVENT,true,false));
        eventTypes.put("resize",   new DOMStdEventTypeInfo(HTML_EVENT,true,false));
        eventTypes.put("scroll",   new DOMStdEventTypeInfo(HTML_EVENT,true,false));
        eventTypes.put("select",   new DOMStdEventTypeInfo(HTML_EVENT,true,false));
        eventTypes.put("change",   new DOMStdEventTypeInfo(HTML_EVENT,true,false));
        eventTypes.put("submit",   new DOMStdEventTypeInfo(HTML_EVENT,true,true));
        eventTypes.put("reset",    new DOMStdEventTypeInfo(HTML_EVENT,true,false));
        eventTypes.put("focus",    new DOMStdEventTypeInfo(HTML_EVENT,false,false));
        eventTypes.put("blur",     new DOMStdEventTypeInfo(HTML_EVENT,false,false));

        eventTypes.put("DOMFocusIn",new DOMStdEventTypeInfo(UI_EVENT,true,false));
        eventTypes.put("DOMFocusOut",new DOMStdEventTypeInfo(UI_EVENT,true,false));
        eventTypes.put("DOMActivate",new DOMStdEventTypeInfo(UI_EVENT,true,true));

        eventTypes.put("DOMSubtreeModified",new DOMStdEventTypeInfo(MUTATION_EVENT,true,false));
        eventTypes.put("DOMNodeInserted",new DOMStdEventTypeInfo(MUTATION_EVENT,true,false));
        eventTypes.put("DOMNodeRemoved",new DOMStdEventTypeInfo(MUTATION_EVENT,true,false));
        eventTypes.put("DOMAttrModified",new DOMStdEventTypeInfo(MUTATION_EVENT,true,false));
        eventTypes.put("DOMCharacterDataModified",new DOMStdEventTypeInfo(MUTATION_EVENT,true,false));
        eventTypes.put("DOMNodeInsertedIntoDocument",new DOMStdEventTypeInfo(MUTATION_EVENT,false,false));
        eventTypes.put("DOMNodeRemovedFromDocument",new DOMStdEventTypeInfo(MUTATION_EVENT,false,false));

        // No W3C pero soportado por FireFox (desde 1.0) y MSIE y Safari (no Opera)
        eventTypes.put("beforeunload",new DOMStdEventTypeInfo(HTML_EVENT,false,true));

        // Soportado por FireFox, Opera 9, Safari y navegadores W3C en general
        eventTypes.put("DOMContentLoaded",new DOMStdEventTypeInfo(UNKNOWN_EVENT,false,false));
    }

    protected int typeCode;
    protected boolean bubbles;
    protected boolean cancelable;

    /**
     * Creates a new instance of DOMStdEventTypeInfo
     */
    public DOMStdEventTypeInfo(int typeCode,boolean bubbles,boolean cancelable)
    {
        this.typeCode = typeCode;
        this.bubbles = bubbles;
        this.cancelable = cancelable;
    }

    public static DOMStdEventTypeInfo getEventTypeInfo(String type)
    {
        DOMStdEventTypeInfo info = (DOMStdEventTypeInfo)eventTypes.get(type);
        if (info == null) // evento desconocido
            info = eventTypeUnknownDefault;
        return info;
    }

    public static int getEventTypeCode(String type)
    {
        DOMStdEventTypeInfo info = (DOMStdEventTypeInfo)eventTypes.get(type);
        if (info == null)
            return UNKNOWN_EVENT;
        return info.getTypeCode();
    }

    public int getTypeCode()
    {
        return typeCode;
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
