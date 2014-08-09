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

package org.itsnat.impl.core.listener.attachcli;

import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientTimerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventTimerImpl;
import org.itsnat.impl.core.scriptren.jsren.listener.attachcli.JSRenderItsNatAttachedClientTimerEventListenerImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClientEventImpl;
import org.itsnat.impl.core.scriptren.bsren.listener.attachcli.BSRenderItsNatAttachedClientTimerEventListenerImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatAttachedClientTimerEventListenerWrapperImpl extends ItsNatAttachedClientEventListenerWrapperImpl
{
    /**
     * Creates a new instance of ItsNatAttachedClientTimerEventListenerWrapperImpl
     */
    public ItsNatAttachedClientTimerEventListenerWrapperImpl(ClientDocumentAttachedClientTimerImpl clientDoc)
    {
        super(clientDoc);

        String code = null;
        ClientDocumentStfulDelegateImpl clientDocDeleg = clientDoc.getClientDocumentStfulDelegate();        
        if (clientDocDeleg instanceof ClientDocumentStfulDelegateWebImpl)
        {
            JSRenderItsNatAttachedClientTimerEventListenerImpl render = JSRenderItsNatAttachedClientTimerEventListenerImpl.getJSRenderItsNatAttachedClientTimerEventListener();
            code = render.addItsNatEventListenerCodeClient(this,(ClientDocumentStfulDelegateWebImpl)clientDocDeleg);
        }
        else if (clientDocDeleg instanceof ClientDocumentStfulDelegateDroidImpl)
        {
            BSRenderItsNatAttachedClientTimerEventListenerImpl render = BSRenderItsNatAttachedClientTimerEventListenerImpl.getBSRenderItsNatAttachedClientTimerEventListener();
            code = render.addItsNatEventListenerCodeClient(this,(ClientDocumentStfulDelegateDroidImpl)clientDocDeleg);
        }        
        clientDocDeleg.addCodeToSend(code);
    }

    public ClientDocumentAttachedClientTimerImpl getClientDocumentAttachedClientTimer()
    {
        return (ClientDocumentAttachedClientTimerImpl)clientDoc;
    }

    public ItsNatAttachedClientEventImpl createItsNatAttachedClientEvent(RequestAttachedClientEventImpl request)
    {
        return new ItsNatAttachedClientEventTimerImpl(request);
    }

    public int getRefreshInterval()
    {
        return getClientDocumentAttachedClientTimer().getRefreshInterval();
    }

}
