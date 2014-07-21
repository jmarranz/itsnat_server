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

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.listener.droid.ItsNatDroidEventListenerWrapperImpl;
import org.itsnat.impl.core.resp.norm.ResponseDroidEventImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class RequestDroidEventImpl extends RequestNormalEventImpl
{
    public RequestDroidEventImpl(int evtType,ItsNatServletRequestImpl itsNatRequest)
    {
        super(evtType,itsNatRequest);
    }

    public ResponseNormalEventImpl createResponseNormalEvent(String listenerId,ClientDocumentStfulImpl clientDoc)
    {
        ClientDocumentStfulDelegateDroidImpl clientDocDeleg = (ClientDocumentStfulDelegateDroidImpl)clientDoc.getClientDocumentStfulDelegate();
        ItsNatDroidEventListenerWrapperImpl listener = clientDocDeleg.getDroidEventListenerById(listenerId);

        // Puede ocurrir que sea nulo, por ejemplo cuando en el cliente se emiten dos eventos
        // seguidos (ej. change y blur en un <input>) y enviados asíncronamente y al procesar uno de ellos y eliminar en el servidor el listener del otro
        // el código de desregistrar no llega antes de que se envíe el segundo evento.

        // listener puede ser null pero puede haber código pendiente a enviar
        return new ResponseDroidEventImpl(this,listener);
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
