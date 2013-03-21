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
import org.itsnat.impl.core.event.client.domstd.ClientItsNatDOMStdEventImpl;
import org.itsnat.impl.core.listener.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.impl.core.resp.norm.ResponseDOMEventImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class RequestDOMStdEventImpl extends RequestDOMEventImpl
{
    seguir sacando código de aquí y llevarlo bajo RequestDOMExtEventImpl
    
    public RequestDOMStdEventImpl(int evtType,ItsNatServletRequestImpl itsNatRequest)
    {
        super(evtType,itsNatRequest);
    }

    public ResponseNormalEventImpl createResponseNormalEvent(String listenerId,ClientDocumentStfulImpl clientDoc)
    {
        ItsNatDOMEventListenerWrapperImpl listener = null;
        if (evtType == DOMSTD_EVENT)
        {
            listener = clientDoc.getDOMStdEventListenerById(listenerId);
            // Puede ocurrir que sea nulo, por ejemplo cuando en el cliente se emiten dos eventos
            // seguidos (ej. change y blur en un <input>) y enviados asíncronamente y al procesar uno de ellos y eliminar en el servidor el listener del otro
            // el código de desregistrar no llega antes de que se envíe el segundo evento.
        }
        else if (evtType == USER_EVENT)
        {
            listener = clientDoc.getUserEventListenerById(listenerId);
        }
        else if (evtType == TIMER_EVENT)
        {
            listener = clientDoc.getTimerEventListenerById(listenerId);
            // Puede ocurrir que sea nulo (es raro pero ocurre), es el caso de que se ha cancelado en el servidor pero en el cliente había un timer pendiente que no se ha cancelado
        }
        else if (evtType == CONTINUE_EVENT)
        {
            listener = clientDoc.removeContinueEventListener(listenerId); // Se ha de borrar cuando se procesa el evento pues es de una sola ejecución
            // Si listener == null no provocamos error, es posible que haya sido ya procesado por un evento lanzado y despachado desde el servidor sin contar con el browser (locally)
        }
        else throw new ItsNatException("Malformed URL/request",clientDoc);

        // listener puede ser null pero puede haber código pendiente a enviar
        return new ResponseDOMEventImpl(this,listener);
    }

    public boolean isLoadEvent()
    {
        if (evtType == DOMSTD_EVENT)
        {
            String eventType = ClientItsNatDOMStdEventImpl.getParameter(this,"type");
            if (eventType.equals("load") || 
                eventType.equals("DOMContentLoaded") ||
                eventType.equals("SVGLoad")) // beforeunload es por si se usa en un futuro como alternativa (cancelable) al unload
                return true;
        }
        return false;
    }

    public boolean isUnloadEvent()
    {
        if (evtType == DOMSTD_EVENT)
        {
            String eventType = ClientItsNatDOMStdEventImpl.getParameter(this,"type");
            if (eventType.equals("unload") || 
                eventType.equals("beforeunload") ||
                eventType.equals("SVGUnload")) // beforeunload es por si se usa en un futuro como alternativa (cancelable) al unload
                return true;
        }
        return false;
    }

}
