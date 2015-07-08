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

package org.itsnat.impl.core.resp.attachcli;

import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventImpl;
import org.itsnat.impl.core.listener.attachcli.ItsNatAttachedClientEventListenerUtil;
import org.itsnat.impl.core.listener.attachcli.ItsNatAttachedClientEventListenerWrapperImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClient;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClientEventImpl;
import org.itsnat.impl.core.resp.ResponseEventStfulImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseAttachedClientEventImpl extends ResponseEventStfulImpl implements ResponseAttachedClient
{
    protected ItsNatAttachedClientEventListenerWrapperImpl listener;

    /**
     * Creates a new instance of ResponseAttachedClientEventImpl
     */
    public ResponseAttachedClientEventImpl(ItsNatAttachedClientEventListenerWrapperImpl listener,RequestAttachedClientEventImpl request)
    {
        super(request);

        this.listener = listener;
    }

    public ClientDocumentAttachedClientImpl getClientDocumentAttachedClient()
    {
        return (ClientDocumentAttachedClientImpl)getClientDocument();
    }

    public RequestAttachedClient getRequestAttachedClient()
    {
        return (RequestAttachedClient)request;
    }

    public RequestAttachedClientEventImpl getRequestAttachedClientEvent()
    {
        return (RequestAttachedClientEventImpl)request;
    }

    public void processEvent()
    {
        // Casos: ItsNatAttachedClientEvent.REFRESH y ItsNatAttachedClientEvent.UNLOAD

        ItsNatAttachedClientEventImpl event = listener.createItsNatAttachedClientEvent(getRequestAttachedClientEvent());

        ItsNatAttachedClientEventListenerUtil.handleEventIncludingGlobalListeners(event);

        ClientDocumentAttachedClientImpl clientDoc = getClientDocumentAttachedClient();
        clientDoc.attachedClientEventReceived();

        if (clientDoc.isInvalid() || (clientDoc.getPhase() == ItsNatAttachedClientEvent.UNLOAD))
        {
            // Lo hacemos antes de enviar el código JavaScript pues al desregistrar se invalida también
            // el cliente (si no lo está) y se genera también código JavaScript
            clientDoc.invalidateAndUnregister(); // Se desregistra de la sesión
        }
    }

    public void postSendPendingCode()
    {
    }
}
