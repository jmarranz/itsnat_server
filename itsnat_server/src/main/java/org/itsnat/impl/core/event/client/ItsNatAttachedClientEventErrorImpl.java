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
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientErrorImpl;
import org.itsnat.impl.core.req.RequestImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClient;

/**
 *
 * @author jmarranz
 */
public class ItsNatAttachedClientEventErrorImpl extends ClientItsNatEventStfulImpl implements ItsNatAttachedClientEvent
{
    public ItsNatAttachedClientEventErrorImpl(RequestAttachedClient request)
    {
        super(request);
    }

    public RequestImpl getRequest()
    {
        return (RequestImpl)getSource();
    }

    public ClientDocumentAttachedClientErrorImpl getClientDocumentAttachedClientError()
    {
        return (ClientDocumentAttachedClientErrorImpl)getClientDocumentImpl();
    }

    public int getCommModeDeclared()
    {
        ClientDocumentAttachedClientErrorImpl clientDoc = getClientDocumentAttachedClientError();
        return clientDoc.getCommModeDeclared();
    }

    public long getEventTimeout()
    {
        ClientDocumentAttachedClientErrorImpl clientDoc = getClientDocumentAttachedClientError();
        return clientDoc.getEventTimeout();
    }

    public long getWaitDocTimeout()
    {
        ClientDocumentAttachedClientErrorImpl clientDoc = getClientDocumentAttachedClientError();
        return clientDoc.getWaitDocTimeout();
    }

    public int getPhase()
    {
        ClientDocumentAttachedClientErrorImpl clientDoc = getClientDocumentAttachedClientError();
        return clientDoc.getPhase();
    }

    public void setPhase(int phase)
    {
        ClientDocumentAttachedClientErrorImpl clientDoc = getClientDocumentAttachedClientError();
        clientDoc.setPhase(phase);
    }

    public boolean isAccepted()
    {
        ClientDocumentAttachedClientErrorImpl clientDoc = getClientDocumentAttachedClientError();
        return clientDoc.isAccepted();
    }

    public void setAccepted(boolean accepted)
    {
        ClientDocumentAttachedClientErrorImpl clientDoc = getClientDocumentAttachedClientError();
        clientDoc.setAccepted(accepted);
    }

    public boolean isReadOnly()
    {
        ClientDocumentAttachedClientErrorImpl clientDoc = getClientDocumentAttachedClientError();
        return clientDoc.isReadOnly();
    }
}
