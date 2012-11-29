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

package org.itsnat.impl.core.req.attachcli;

import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientCometImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.listener.CometTaskImpl;
import org.itsnat.impl.core.listener.ItsNatAttachedClientCometEventListenerWrapperImpl;
import org.itsnat.impl.core.resp.attachcli.ResponseAttachedClientEventImpl;

/**
 *
 * @author jmarranz
 */
public class RequestAttachedClientEventCometImpl extends RequestAttachedClientEventImpl
{
    protected ItsNatAttachedClientCometEventListenerWrapperImpl taskPendingToFinish = null;

    public RequestAttachedClientEventCometImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
    }

    public void processClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        if (isUnloadEvent())
        {
            super.processClientDocumentAttachedClient(clientDoc);
        }
        else
        {
            do
            {
                super.processClientDocumentAttachedClient(clientDoc);

                if (taskPendingToFinish != null)
                {
                    CometTaskImpl task = taskPendingToFinish.getCometTask();
                    task.waitToFinish();

                    if (clientDoc.getPhase() == ItsNatAttachedClientEvent.UNLOAD)
                    {
                        task.dispose();
                        taskPendingToFinish = null; // No tiene sentido procesar este refresco pues no vale la pena y confundiría al programador con un refresco tras un unload
                    }
                }
            }
            while(taskPendingToFinish != null);
        }
    }

    public ResponseAttachedClientEventImpl createResponseAttachedClientEventUnload(ClientDocumentAttachedClientImpl clientDoc)
    {
        ClientDocumentAttachedClientCometImpl observerComet = (ClientDocumentAttachedClientCometImpl)clientDoc;
        ItsNatAttachedClientCometEventListenerWrapperImpl listener = new ItsNatAttachedClientCometEventListenerWrapperImpl(null,observerComet);
        return new ResponseAttachedClientEventImpl(listener,this);
    }

    public ResponseAttachedClientEventImpl createResponseAttachedClientEventRefresh(ClientDocumentAttachedClientImpl clientDoc)
    {
        ClientDocumentAttachedClientCometImpl observerComet = (ClientDocumentAttachedClientCometImpl)clientDoc;

        if (taskPendingToFinish == null) // La primera vez
        {
            String listenerId = getAttrOrParamExist("itsnat_listener_id");
            this.taskPendingToFinish = (ItsNatAttachedClientCometEventListenerWrapperImpl)observerComet.removeAttachedClientCometTask(listenerId);
        }

        CometTaskImpl task = taskPendingToFinish.getCometTask();
        if (!task.mustWait())
        {
            task.dispose(); // Si ya se llamó no hace nada
            ItsNatAttachedClientCometEventListenerWrapperImpl listener = this.taskPendingToFinish;
            this.taskPendingToFinish = null;
            return new ResponseAttachedClientEventImpl(listener,this);
        }
        else
        {
            return null; // Hay que esperar
        }
    }
}
