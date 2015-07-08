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

package org.itsnat.impl.core.event.client;

import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClient;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatAttachedClientEventImpl extends ClientItsNatEventStfulImpl implements ItsNatAttachedClientEvent
{
    /**
     * Creates a new instance of ItsNatAttachedClientEventImpl
     */
    public ItsNatAttachedClientEventImpl(RequestAttachedClient request)
    {
        super(request); // listenerWrapper puede ser null
    }

    public ClientDocumentAttachedClientImpl getClientDocumentAttachedClient()
    {
        return (ClientDocumentAttachedClientImpl)getClientDocumentImpl();
    }

    public int getCommModeDeclared()
    {
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
        return clientDoc.getCommModeDeclared();
    }

    public int getPhase()
    {
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
        return clientDoc.getPhase();
    }

    public long getEventTimeout()
    {
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
        return clientDoc.getEventTimeout();
    }

    public long getWaitDocTimeout()
    {
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
        return clientDoc.getWaitDocTimeout();
    }
    
    public boolean isAccepted()
    {
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
        return clientDoc.isAccepted();
    }

    public void setAccepted(boolean accepted)
    {
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
        clientDoc.setAccepted(accepted);
    }

    public boolean isReadOnly()
    {
        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
        return clientDoc.isReadOnly();
    }
}
