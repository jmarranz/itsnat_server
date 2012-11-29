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

package org.itsnat.impl.core.listener.domstd;

import java.io.Serializable;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.event.ItsNatEventImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class RegisterThisDocAsReferrerListenerImpl implements EventListener,Serializable
{
    public static final RegisterThisDocAsReferrerListenerImpl SINGLETON = new RegisterThisDocAsReferrerListenerImpl();

    /**
     * Creates a new instance of RegisterThisDocAsReferrerListenerImpl
     */
    private RegisterThisDocAsReferrerListenerImpl()
    {
    }

    public void handleEvent(Event evt)
    {
        ItsNatEventImpl itsNatEvt = (ItsNatEventImpl)evt;

        ItsNatServletRequestImpl itsNatRequest = itsNatEvt.getItsNatServletRequestImpl();
        ItsNatSessionImpl itsNatSession = itsNatRequest.getItsNatSessionImpl();
        ItsNatStfulDocumentImpl itsNatDoc = itsNatEvt.getItsNatStfulDocument();
        itsNatSession.getReferrer().pushItsNatStfulDocument(itsNatDoc);
    }

}
