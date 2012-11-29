/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.comp.functest;

import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class EventListenerWait implements EventListener
{
    public boolean received = false;
    protected Element elem;
    protected String type;

    public EventListenerWait(Element elem,String type)
    {
        this.elem = elem;
        this.type = type;
    }

    public void handleEvent(Event evt)
    {
        String recType = evt.getType();
        EventTarget recTarget = evt.getTarget();
        if (type.equals(recType) && (elem == recTarget))
            this.received = true;
    }
}
