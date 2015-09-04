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

package org.itsnat.impl.core.listener;

import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class WaitForEventListenerImpl implements EventListenerSerializableInternal
{
    protected Element elem;
    protected String type;

    public WaitForEventListenerImpl(Element elem,String type)
    {
        this.elem = elem;
        this.type = type;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + (this.elem != null ? this.elem.hashCode() : 0);
        hash = 67 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (super.equals(obj))
            return true; // Identidad a nivel de puntero

        if (!(obj instanceof WaitForEventListenerImpl)) return false;

        WaitForEventListenerImpl other = (WaitForEventListenerImpl)obj;
        return (type.equals(other.type) && (elem == other.elem));
    }

    public Element getElement()
    {
        return elem;
    }

    public String getType()
    {
        return type;
    }

    @Override
    public void handleEvent(Event evt)
    {
        ClientDocumentStfulImpl clientDoc = (ClientDocumentStfulImpl)((ItsNatEvent)evt).getClientDocument();
        clientDoc.getCodeToSendRegistry().removeWaitForEventListener(this);
    }
}
