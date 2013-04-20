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

import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentWithoutDocumentDefaultImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.req.ContainsItsNatStfulDocumentReferrer;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalLoadDocNotFoundImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;

/**
 *
 * @author jmarranz
 */
public class RequestNormalLoadDocNotFoundImpl extends RequestNormalLoadDocBaseImpl        
{
    protected String docName;

    /**
     * Creates a new instance of RequestNormalLoadDocValidImpl
     */
    public RequestNormalLoadDocNotFoundImpl(String docName,ItsNatServletRequestImpl itsNatRequest,boolean stateless)
    {
        super(itsNatRequest,stateless);

        this.docName = docName;
    }

    public String getDocName()
    {
        return docName;
    }

    public ResponseNormalLoadDocNotFoundImpl getResponseNormalLoadDocNotFound()
    {
        return (ResponseNormalLoadDocNotFoundImpl)response;
    }

    @Override    
    public void processRequest(ClientDocumentStfulImpl clientDocStateless)
    {
        // También tiene derecho a recibir el referrer de un documento anterior AJAX
        if (!stateless)        
            setItsNatStfulDocumentReferrer( getItsNatSession().getReferrer().popItsNatStfulDocument() );

        ItsNatSessionImpl session = getItsNatSession();        
        ClientDocumentWithoutDocumentDefaultImpl clientDoc = new ClientDocumentWithoutDocumentDefaultImpl(session);

        bindClientToRequest(clientDoc,false);  // El documento es nulo, por tanto no se vincula el request al doc

        this.response = new ResponseNormalLoadDocNotFoundImpl(this);
        response.process();
    }
}
