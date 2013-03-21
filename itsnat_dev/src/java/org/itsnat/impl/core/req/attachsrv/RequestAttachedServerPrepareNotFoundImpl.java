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

import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentNoServerDocDefaultImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.req.ContainsItsNatStfulDocumentReferrer;
import org.itsnat.impl.core.resp.attachsrv.ResponseAttachedServerPrepareNotFoundImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalLoadDocNotFoundImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;

/**
 *
 * @author jmarranz
 */
public class RequestAttachedServerPrepareNotFoundImpl extends RequestAttachedServerPrepareBaseImpl
                        implements ContainsItsNatStfulDocumentReferrer
{
    protected String docName;
    protected ItsNatStfulDocumentImpl itsNatDocReferrer;
    
    /**
     * Creates a new instance of RequestNormalLoadDocValidImpl
     */
    public RequestAttachedServerPrepareNotFoundImpl(String docName,ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);

        this.docName = docName;
    }

    public String getDocName()
    {
        return docName;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer()
    {
        return itsNatDocReferrer;
    }

    public void setItsNatStfulDocumentReferrer(ItsNatStfulDocumentImpl itsNatDocReferrer)
    {
        this.itsNatDocReferrer = itsNatDocReferrer;
    }

    public ResponseNormalLoadDocNotFoundImpl getResponseNormalLoadDocNotFound()
    {
        return (ResponseNormalLoadDocNotFoundImpl)response;
    }

    public void processRequest(ClientDocumentImpl clientDocStateless)
    {
        ItsNatSessionImpl session = getItsNatSession();
        // También tiene derecho a recibir el referrer de un documento anterior stateful
        setItsNatStfulDocumentReferrer( session.getReferrer().popItsNatStfulDocument() );

        ClientDocumentNoServerDocDefaultImpl clientDoc = new ClientDocumentNoServerDocDefaultImpl(session);

        bindClientToRequest(clientDoc,false);  // El documento es nulo, por tanto no se vincula el request al doc

        this.response = new ResponseAttachedServerPrepareNotFoundImpl(this);
        response.process();
    }
}
