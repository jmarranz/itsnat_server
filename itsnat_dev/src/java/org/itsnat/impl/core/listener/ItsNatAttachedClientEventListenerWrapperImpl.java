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

import org.itsnat.impl.core.CommModeImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClientEventImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatAttachedClientEventListenerWrapperImpl extends ItsNatEventListenerWrapperImpl
{
    protected ClientDocumentAttachedClientImpl clientDoc; // Nunca es null

    /**
     * Creates a new instance of ItsNatAttachedClientEventListenerWrapperImpl
     */
    public ItsNatAttachedClientEventListenerWrapperImpl(ClientDocumentAttachedClientImpl clientDoc)
    {
        super(clientDoc.getItsNatStfulDocument());

        this.clientDoc = clientDoc;
    }

    public ClientDocumentAttachedClientImpl getClientDocumentAttachedClient()
    {
        return clientDoc;
    }
    
    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return clientDoc;
    }

    public int getCommModeDeclared()
    {
        return clientDoc.getCommModeDeclared();
    }

    public long getEventTimeout()
    {
        return clientDoc.getEventTimeout();
    }

    public abstract ItsNatAttachedClientEventImpl createItsNatAttachedClientEvent(RequestAttachedClientEventImpl request);
}
