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
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerScriptImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.resp.attachsrv.ResponseAttachedServerLoadDocImpl;
import org.itsnat.impl.core.resp.attachsrv.ResponseAttachedServerLoadMarkupFormImpl;
import org.itsnat.impl.core.resp.attachsrv.ResponseAttachedServerLoadMarkupScriptImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;

/**
 *
 * @author jmarranz
 */
public class RequestAttachedServerLoadMarkupImpl extends RequestAttachedServerImpl
{
    protected String clientId;

    public RequestAttachedServerLoadMarkupImpl(String clientId,ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);

        this.clientId = clientId;
    }

    public static RequestAttachedServerLoadMarkupImpl createRequestAttachedServerLoadDocBase(String clientId,ItsNatServletRequestImpl itsNatRequest)
    {
        return new RequestAttachedServerLoadMarkupImpl(clientId,itsNatRequest);
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer()
    {
        // El referrer sólo tiene sentido en tiempo de carga de una página para que tenga acceso a la anterior
        // en este caso el request se acabará procesando por un request normal que es que recogerá el referrer
        return null;
    }

    public ResponseAttachedServerLoadDocImpl getResponseAttachedServerLoadDoc()
    {
        return (ResponseAttachedServerLoadDocImpl)response;
    }

    public void processRequest()
    {
        ClientDocumentAttachedServerImpl clientDoc = itsNatRequest.getItsNatSessionImpl().getClientDocumentAttachedServersById(clientId);
        if (clientDoc == null) throw new ItsNatException("Not found in server"); // MUY raro, las requests son muy seguidas, posiblemente de un cliente malicioso

        try
        {
            bindClientToRequest(clientDoc,false);

            String clientMarkup = getAttrOrParamExist("itsnat_markup_code");
            clientDoc.addClientMarkup(clientMarkup);

            if (clientDoc instanceof ClientDocumentAttachedServerScriptImpl)
                this.response = new ResponseAttachedServerLoadMarkupScriptImpl(this);
            else
                this.response = new ResponseAttachedServerLoadMarkupFormImpl(this);
            response.process();
        }
        catch(RuntimeException ex)
        {
            clientDoc.setInvalid();
            throw ex;
        }
    }
}
