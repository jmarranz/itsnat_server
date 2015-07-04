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
import org.itsnat.impl.core.listener.dom.domext.ItsNatDOMExtEventListenerWrapperImpl;
import org.itsnat.impl.core.resp.norm.ResponseDOMExtEventOtherImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class RequestDOMExtEventOtherImpl extends RequestDOMExtEventImpl
{
    public RequestDOMExtEventOtherImpl(int evtType,ItsNatServletRequestImpl itsNatRequest)
    {
        super(evtType,itsNatRequest);
    }

    public ResponseNormalEventImpl createResponseNormalEvent(String listenerId,ClientDocumentStfulImpl clientDoc)
    {
        ItsNatDOMExtEventListenerWrapperImpl listener = null;

        if (evtType == EVENT_TYPE_USER)
        {
            listener = clientDoc.getUserEventListenerById(listenerId);
        }
        else if (evtType == EVENT_TYPE_TIMER)
        {
            listener = clientDoc.getTimerEventListenerById(listenerId);
            // Puede ocurrir que sea nulo (es raro pero ocurre), es el caso de que se ha cancelado en el servidor pero en el cliente había un timer pendiente que no se ha cancelado
        }
        else if (evtType == EVENT_TYPE_CONTINUE)
        {
            listener = clientDoc.removeContinueEventListener(listenerId); // Se ha de borrar cuando se procesa el evento pues es de una sola ejecución
            // Si listener == null no provocamos error, es posible que haya sido ya procesado por un evento lanzado y despachado desde el servidor sin contar con el browser (locally)
        }
        else throw new ItsNatException("Malformed URL/request",clientDoc);

        // listener puede ser null pero puede haber código pendiente a enviar
        return new ResponseDOMExtEventOtherImpl(this,listener);
    }

}
