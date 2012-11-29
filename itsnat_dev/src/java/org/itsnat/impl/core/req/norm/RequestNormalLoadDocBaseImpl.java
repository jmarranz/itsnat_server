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
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.req.ContainsItsNatStfulDocumentReferrer;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.req.RequestLoadDocImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormal;
import org.itsnat.impl.core.resp.norm.ResponseNormalLoadDocBaseImpl;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateAttachedServerImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateImpl;

/**
 *
 * @author jmarranz
 */
public abstract class RequestNormalLoadDocBaseImpl extends RequestLoadDocImpl 
        implements RequestNormal,ContainsItsNatStfulDocumentReferrer
{
    protected ItsNatStfulDocumentImpl itsNatDocReferrer;
    
    /**
     * Creates a new instance of RequestNormalLoadDocImpl
     */
    public RequestNormalLoadDocBaseImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
    }

    public static RequestNormalLoadDocBaseImpl createRequestNormalLoadDocBase(String docName,ItsNatServletRequestImpl itsNatRequest)
    {
        // Tenemos la seguridad de que docName no es nulo, sino no llegaríamos aquí

        ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();
        ItsNatDocumentTemplateImpl docTemplate = itsNatServlet.getItsNatDocumentTemplateImpl(docName);

        if (docTemplate == null)
            return new RequestNormalLoadDocNotFoundImpl(docName,itsNatRequest);
        else
        {
            // Provocamos una excepción sin más contemplaciones pues se detectará
            // en tiempo de desarrollo o por un intento de violación de seguridad
            if (docTemplate instanceof ItsNatStfulDocumentTemplateAttachedServerImpl)
                throw new ItsNatException("Document/page " + docName + " is of type attached server, not valid in this context",itsNatRequest);
            return new RequestNormalLoadDocDefaultImpl(docTemplate,itsNatRequest);
        }
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer()
    {
        return itsNatDocReferrer;
    }

    public void setItsNatStfulDocumentReferrer(ItsNatStfulDocumentImpl itsNatDocReferrer)
    {
        this.itsNatDocReferrer = itsNatDocReferrer;
    }

    public ResponseNormal getResponseNormal()
    {
        return (ResponseNormal)response;
    }

    public ResponseNormalLoadDocBaseImpl getResponseNormalLoadDocBase()
    {
        return (ResponseNormalLoadDocBaseImpl)response;
    }

}
