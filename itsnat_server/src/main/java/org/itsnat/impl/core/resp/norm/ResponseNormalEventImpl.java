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

package org.itsnat.impl.core.resp.norm;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.resp.*;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormal;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseNormalEventImpl extends ResponseEventStfulImpl implements ResponseNormal
{
    protected ItsNatNormalEventListenerWrapperImpl listener;

    /** Creates a new instance of ResponseNormalEventImpl */
    public ResponseNormalEventImpl(RequestNormalEventImpl request,ItsNatNormalEventListenerWrapperImpl listener)
    {
        super(request);

        this.listener = listener;
        
        if ((listener != null) &&
             !getClientDocumentStful().canReceiveNormalEvents(listener))
        {
            // Chequeo de seguridad para evitar que clientes de control remoto
            // envíen eventos cuando no están autorizados (son read only por ejemplo). En teoría no se envió
            // código JavaScript para ello (registro de listener) pero un malicioso usuario
            // podría intentarlo enviando requests AJAX "a pelo".
            throw new ItsNatException("Security violation attempt");
        }        
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return (ClientDocumentStfulImpl)getClientDocument();
    }

    public RequestNormal getRequestNormal()
    {
        return (RequestNormal)request;
    }

    public RequestNormalEventImpl getRequestNormalEvent()
    {
        return (RequestNormalEventImpl)request;
    }

    public void processEvent()
    {
        if (listener != null) // Hay un caso en el que es null, yo creo que en ese caso tampoco tiene sentido llamar a los listeners globales pues es el caso de un "evento perdido"
        {
            RequestNormalEventImpl request = getRequestNormalEvent();
            ClientItsNatNormalEventImpl event = listener.createClientItsNatNormalEvent(request);
            event.resolveNodePaths(); // Ver notas en el método

            listener.processEvent(event);
        }
    }

    public void postSendPendingCode()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        clientDoc.normalEventReceived();
    }

}
