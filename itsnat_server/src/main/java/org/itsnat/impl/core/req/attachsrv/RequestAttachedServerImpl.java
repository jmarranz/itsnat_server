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

package org.itsnat.impl.core.req.attachsrv;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.req.RequestImpl;
import org.itsnat.impl.core.resp.attachsrv.ResponseAttachedServerImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;

/**
 *
 * @author jmarranz
 */
public abstract class RequestAttachedServerImpl extends RequestImpl 
{
    /**
     * Creates a new instance of RequestNormalLoadDocImpl
     */
    public RequestAttachedServerImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
    }

    public static RequestAttachedServerImpl createRequestAttachedServer(ItsNatServletRequestImpl itsNatRequest)
    {
        String clientId = itsNatRequest.getAttrOrParam("itsnat_client_id");
        if (clientId == null)
            return RequestAttachedServerPrepareBaseImpl.createRequestAttachedServerPrepareBase(itsNatRequest);
        else
        {
            String subaction = itsNatRequest.getAttrOrParamExist("itsnat_subaction");
            if (subaction.equals("load_markup"))
                return new RequestAttachedServerLoadMarkupImpl(clientId,itsNatRequest);
            else if (subaction.equals("load_doc"))
                return RequestAttachedServerLoadDocImpl.createRequestAttachedServerLoadDocBase(clientId,itsNatRequest);
            else
                throw new ItsNatException("Unexpected subaction");
        }
    }

    public ResponseAttachedServerImpl getResponseAttachedServer()
    {
        return (ResponseAttachedServerImpl)response;
    }

    protected boolean isMustNotifyEndOfRequestToSession()
    {
        // En todos los tipos de requests hay que devolver true, incluso
        // en el caso de PrepareNotFound porque cambiamos el referrer de la sesión (poco más)
        return true;
    }

}

