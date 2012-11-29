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

import java.util.LinkedList;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.event.client.ClientDOMEventErrorImpl;
import org.itsnat.impl.core.listener.EventListenerUtil;
import org.itsnat.impl.core.req.norm.RequestNormal;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.itsnat.impl.core.resp.ResponseEventImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseNormalEventErrorImpl extends ResponseEventImpl implements ResponseNormal
{
    /**
     * Creates a new instance of ResponseNormalLostClientDocImpl
     */
    public ResponseNormalEventErrorImpl(RequestNormalEventImpl request)
    {
        super(request);
    }

    public RequestNormal getRequestNormal()
    {
        return (RequestNormal)request;
    }

    public RequestNormalEventImpl getRequestNormalEvent()
    {
        return (RequestNormalEventImpl)request;
    }

    public void postSendPendingCode()
    {
    }

    protected boolean processGlobalListeners()
    {
        // Sabemos que NO es un unload.

        ItsNatServletImpl itsNatServlet = request.getItsNatServletRequest().getItsNatServletImpl();

        if (itsNatServlet.hasEventListenerListeners())
        {
            LinkedList listeners = new LinkedList();
            itsNatServlet.getGlobalEventListenerList(listeners);

            ClientDOMEventErrorImpl evt = new ClientDOMEventErrorImpl(getRequestNormalEvent());
            EventListenerUtil.handleEventListeners(evt, listeners);

            return true;
        }
        // Si no hay listeners globales el programador ha perdido su oportunidad
        // de informar al usuario y hacer un window.location.reload(true) o similar,

        // En el caso de evento async task o comet, también ejecutamos los listeners globales
        // Es muy raro que estos eventos se reciban cuando la sesión caduca (mucha casualidad) y si el servidor
        // se reinicia se puede decir que el usuario "ve" que se está reiniciando
        // el servidor en ese momento, pero por homogeniedad lo tratamos igual.

        return false;
    }

}
