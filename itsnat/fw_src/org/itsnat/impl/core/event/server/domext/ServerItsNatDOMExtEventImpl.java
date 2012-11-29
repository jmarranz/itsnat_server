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


package org.itsnat.impl.core.event.server.domext;

import org.itsnat.core.event.ItsNatDOMExtEvent;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.server.ServerItsNatDOMEventImpl;
import org.w3c.dom.DOMException;

/**
 *
 * @author jmarranz
 */
public abstract class ServerItsNatDOMExtEventImpl extends ServerItsNatDOMEventImpl implements ItsNatDOMExtEvent
{

    /** Creates a new instance of ServerItsNatDOMExtEventImpl */
    public ServerItsNatDOMExtEventImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public static ServerItsNatDOMExtEventImpl createServerDOMExtEvent(String eventGroup,ItsNatStfulDocumentImpl itsNatDoc) throws DOMException
    {
        if (eventGroup.startsWith("itsnat:UserEvents") || eventGroup.startsWith("itsnat:UserEvent"))
            return new ServerItsNatUserEventImpl(itsNatDoc);
        else if (eventGroup.startsWith("itsnat:ContinueEvents") || eventGroup.startsWith("itsnat:ContinueEvent"))
            return new ServerItsNatContinueEventImpl(itsNatDoc);
        else
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,"NOT_SUPPORTED_ERR: The implementation does not support the requested type of event.");
    }

    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
    {
        super.initEvent(eventTypeArg,canBubbleArg,cancelableArg);

        if (canBubbleArg) throw new DOMException(DOMException.NOT_SUPPORTED_ERR,"NOT_SUPPORTED_ERR: DOM extension events do not bubble.");
        if (cancelableArg) throw new DOMException(DOMException.NOT_SUPPORTED_ERR,"NOT_SUPPORTED_ERR: DOM extension events are not cancelable");
    }
}
