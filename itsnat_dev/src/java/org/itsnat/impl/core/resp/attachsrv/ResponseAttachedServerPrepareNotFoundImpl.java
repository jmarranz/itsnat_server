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

package org.itsnat.impl.core.resp.attachsrv;

import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerPrepareNotFoundImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseAttachedServerPrepareNotFoundImpl extends ResponseAttachedServerPrepareBaseImpl
{

    /**
     * Creates a new instance of ResponseNormalLoadDocImpl
     */
    public ResponseAttachedServerPrepareNotFoundImpl(RequestAttachedServerPrepareNotFoundImpl request)
    {
        super(request);
    }

    public RequestAttachedServerPrepareNotFoundImpl getRequestAttachedServerPrepareNotFound()
    {
        return (RequestAttachedServerPrepareNotFoundImpl)request;
    }

    protected void processResponse()
    {
        dispatchRequestListeners();
    }

    public void dispatchRequestListeners()
    {
        ItsNatServletRequestImpl itsNatRequest = itsNatResponse.getItsNatServletRequestImpl();
        ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();
        if (!itsNatServlet.dispatchItsNatServletRequestListeners(itsNatRequest))
        {
            // No hay listeners globales provocamos un error
            String docName = getRequestAttachedServerPrepareNotFound().getDocName();
            throw new ItsNatException("Document/page " + docName + " does not exist",itsNatRequest);
        }
    }
}
