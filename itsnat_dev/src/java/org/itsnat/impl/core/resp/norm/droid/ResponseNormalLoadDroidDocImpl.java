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

package org.itsnat.impl.core.resp.norm.droid;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalLoadStfulDocImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseNormalLoadDroidDocImpl extends ResponseNormalLoadStfulDocImpl
{
    /**
     * Creates a new instance of ResponseNormalLoadDroidDocImpl
     * @param request
     */
    public ResponseNormalLoadDroidDocImpl(RequestNormalLoadDocImpl request)
    {
        super(request);
    }

    @Override
    public void dispatchRequestListeners()
    {
        // Caso de carga del documento por primera vez, el documento está recién creado

        super.dispatchRequestListeners(); // En el método base en el caso de referrer se procesará el anterior antes de ser substituido por el actual documento
        
        if (isReferrerEnabled())
        {
            // No nos complicamos la vida con listeners load etc
            ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();            
            ClientDocumentStfulImpl clientDoc = getClientDocumentStful();            
            ItsNatSessionImpl itsNatSession = clientDoc.getItsNatSessionImpl();
            itsNatSession.getReferrer().pushItsNatStfulDocument(itsNatDoc);            
        }   
    }
}
