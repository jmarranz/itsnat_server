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
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerFormImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerScriptImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.resp.attachsrv.ResponseAttachedServerPrepareFormImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.resp.attachsrv.ResponseAttachedServerPrepareScriptImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateAttachedServerImpl;


/**
 *
 * @author jmarranz
 */
public class RequestAttachedServerPrepareImpl extends RequestAttachedServerPrepareBaseImpl
{
    protected ItsNatStfulDocumentTemplateAttachedServerImpl docTemplate;

    /**
     * Creates a new instance of RequestAttachedServerPrepareImpl
     */
    public RequestAttachedServerPrepareImpl(ItsNatStfulDocumentTemplateAttachedServerImpl docTemplate,ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);

        this.docTemplate = docTemplate;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer()
    {
        // El referrer sólo tiene sentido en tiempo de carga de una página para que tenga acceso a la anterior
        // en este caso el request se acabará procesando por un request normal que es que recogerá el referrer
        return null;
    }

    public ResponseAttachedServerPrepareScriptImpl getResponseAttachedServerPrepareImpl()
    {
        return (ResponseAttachedServerPrepareScriptImpl)response;
    }

    public ItsNatStfulDocumentTemplateAttachedServerImpl getItsNatStfulDocumentTemplateAttachedServer()
    {
        return (ItsNatStfulDocumentTemplateAttachedServerImpl)docTemplate;
    }

    public void processRequest()
    {
        String loadMethod = getAttrOrParam("itsnat_method");

        boolean loadMethodScript;
        if (loadMethod != null)
        {
            if ("script".equals(loadMethod))
                loadMethodScript = true;
            else if("form".equals(loadMethod))
                loadMethodScript = false;
            else
                throw new ItsNatException("Unknown value of itsnat_method parameter (expected \"script\" or \"form\"):" + loadMethod);
        }
        else loadMethodScript = false; // Por defecto es "form" que es mucho más rápido

        ItsNatSessionImpl itsNatSession = getItsNatServletRequest().getItsNatSessionImpl();
        ClientDocumentAttachedServerImpl clientDoc;
        if (loadMethodScript)
        {
            clientDoc = new ClientDocumentAttachedServerScriptImpl(itsNatSession,docTemplate);
        }
        else
        {
            long timeoutLoadMarkup;

            String timeoutStr = getAttrOrParam("itsnat_timeout");
            if (timeoutStr != null)
            {
                timeoutLoadMarkup = Long.parseLong(timeoutStr);
                if (timeoutLoadMarkup < 0) throw new ItsNatException("itsnat_timeout cannot be negative");
            }
            else timeoutLoadMarkup = 10*60*1000; // 10 minutos (en millisec)

            clientDoc = new ClientDocumentAttachedServerFormImpl(itsNatSession,docTemplate,timeoutLoadMarkup);
        }

        bindClientToRequest(clientDoc,false);

        try
        {
            if (loadMethodScript)
                this.response = new ResponseAttachedServerPrepareScriptImpl(this);
            else // Form
                this.response = new ResponseAttachedServerPrepareFormImpl(this);

            response.process();
        }
        catch(RuntimeException ex)
        {
            clientDoc.setInvalid();
            throw ex;
        }
    }
}
