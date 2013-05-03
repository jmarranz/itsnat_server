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

package org.itsnat.impl.core.req.norm;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.listener.domext.ItsNatAsyncTaskEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.GenericTaskImpl;
import org.itsnat.impl.core.listener.domext.ItsNatGenericTaskEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatNormalCometEventListenerWrapperImpl;
import org.itsnat.impl.core.resp.norm.ResponseGenericTaskEventImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class RequestGenericTaskEventImpl extends RequestDOMExtEventImpl
{
    protected ItsNatGenericTaskEventListenerWrapperImpl taskPendingToFinish = null;

    public RequestGenericTaskEventImpl(int evtType,ItsNatServletRequestImpl itsNatRequest)
    {
        super(evtType,itsNatRequest);
    }

    @Override
    protected void processClientDocument2(String listenerId,ClientDocumentStfulImpl clientDoc)
    {
        do
        {
            super.processClientDocument2(listenerId,clientDoc);

            if (taskPendingToFinish != null)
            {
                GenericTaskImpl task = taskPendingToFinish.getGenericTask();
                task.waitToFinish();
                if (clientDoc.isInvalid())
                {
                    task.dispose();
                    taskPendingToFinish = null; // No se debe procesar este evento, no vale la pena y puede confundir al programador
                }
            }
        }
        while(taskPendingToFinish != null);
    }

    public ResponseNormalEventImpl createResponseNormalEvent(String listenerId,ClientDocumentStfulImpl clientDoc)
    {
        if (taskPendingToFinish == null) // Primera vez que se llega aquí
        {
            if (evtType == EVENT_TYPE_ASYNC_RET)
            {
                this.taskPendingToFinish = (ItsNatAsyncTaskEventListenerWrapperImpl)clientDoc.removeAsynchronousTask(listenerId);
            }
            else if (evtType == EVENT_TYPE_COMET_RET)
            {
                this.taskPendingToFinish = (ItsNatNormalCometEventListenerWrapperImpl)clientDoc.removeCometTask(listenerId);
            }
            else throw new ItsNatException("Malformed URL/request",clientDoc);
        }

        GenericTaskImpl task = taskPendingToFinish.getGenericTask();
        if (!task.mustWait())
        {
            task.dispose(); // Si ya se llamó no hace nada

            ItsNatGenericTaskEventListenerWrapperImpl listener = this.taskPendingToFinish;
            this.taskPendingToFinish = null;
            return new ResponseGenericTaskEventImpl(this,listener);
        }
        else
        {
            return null; // Hay que esperar
        }
    }

    public boolean isLoadEvent()
    {
        return false;
    }

    public boolean isUnloadEvent()
    {
        return false;
    }
}
