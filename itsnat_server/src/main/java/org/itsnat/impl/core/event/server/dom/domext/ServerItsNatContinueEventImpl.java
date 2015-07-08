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

package org.itsnat.impl.core.event.server.dom.domext;

import org.itsnat.core.event.ItsNatContinueEvent;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatContinueEventListenerWrapperImpl;
import org.w3c.dom.DOMException;

/**
 *
 * @author jmarranz
 */
public class ServerItsNatContinueEventImpl extends ServerItsNatDOMExtEventImpl implements ItsNatContinueEvent
{
    /** Creates a new instance of ServerItsNatContinueEventImpl */
    public ServerItsNatContinueEventImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
    {
        super.initEvent(eventTypeArg,canBubbleArg,cancelableArg);

        if (!ItsNatContinueEventListenerWrapperImpl.isContinueType(eventTypeArg))
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,"NOT_SUPPORTED_ERR: Expected type:" + ItsNatContinueEventListenerWrapperImpl.getTypeStatic());
    }

}
