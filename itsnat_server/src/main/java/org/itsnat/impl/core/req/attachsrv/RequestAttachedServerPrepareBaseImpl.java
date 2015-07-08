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
import org.itsnat.impl.core.resp.attachsrv.ResponseAttachedServerPrepareBaseImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateAttachedServerImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateImpl;

/**
 *
 * @author jmarranz
 */
public abstract class RequestAttachedServerPrepareBaseImpl extends RequestAttachedServerImpl
{
    public RequestAttachedServerPrepareBaseImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
    }

    public static RequestAttachedServerPrepareBaseImpl createRequestAttachedServerPrepareBase(ItsNatServletRequestImpl itsNatRequest)
    {
        String docName = itsNatRequest.getAttrOrParamExist("itsnat_doc_name");

        ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();
        ItsNatDocumentTemplateImpl docTemplate = itsNatServlet.getItsNatDocumentTemplateImpl(docName);
        if (docTemplate == null)
            return new RequestAttachedServerPrepareNotFoundImpl(docName,itsNatRequest);
        else
        {
            // Provocamos una excepción sin más contemplaciones pues se detectará
            // en tiempo de desarrollo o por un intento de violación de seguridad
            if (!(docTemplate instanceof ItsNatStfulDocumentTemplateAttachedServerImpl))
                throw new ItsNatException("Document/page " + docName + " is not of type attached server, required in this context",itsNatRequest);

            return new RequestAttachedServerPrepareImpl((ItsNatStfulDocumentTemplateAttachedServerImpl)docTemplate,itsNatRequest);
        }
    }

    public ResponseAttachedServerPrepareBaseImpl getResponseAttachedServerPrepareBase()
    {
        return (ResponseAttachedServerPrepareBaseImpl)response;
    }

}
