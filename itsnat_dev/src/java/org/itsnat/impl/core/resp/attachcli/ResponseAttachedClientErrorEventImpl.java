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

import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientErrorImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClient;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClientEventImpl;
import org.itsnat.impl.core.resp.ResponseEventStfulImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseAttachedClientErrorEventImpl extends ResponseEventStfulImpl implements ResponseAttachedClient
{
    public ResponseAttachedClientErrorEventImpl(RequestAttachedClientEventImpl request)
    {
        super(request);
    }

    public RequestAttachedClient getRequestAttachedClient()
    {
        return (RequestAttachedClient)request;
    }

    public RequestAttachedClientEventImpl getRequestAttachedClientEvent()
    {
        return (RequestAttachedClientEventImpl)request;
    }

    public boolean processGlobalListeners()
    {
        return ResponseAttachedClientErrorSharedImpl.processGlobalListeners(this);
    }

    public void processEvent()
    {
        itsNatResponse.addCodeToSend("try{ document.getItsNatDoc().stopAttachTimerRefresh(); } catch(e) { }"); // Por si hubiera un timer, para que pare, si no hay timer (Comet) no hace nada

        processGlobalListeners();
    }

    public void postSendPendingCode()
    {
    }

    public ClientDocumentAttachedClientErrorImpl getClientDocumentAttachedClientError()
    {
        return (ClientDocumentAttachedClientErrorImpl)getClientDocument();
    }
}
