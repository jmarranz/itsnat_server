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

package org.itsnat.impl.core.req.script;

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocSynchronizerImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.req.RequestStfulDocument;
import org.itsnat.impl.core.resp.script.ResponseLoadScriptInitialImpl;

/**
 *
 * @author jmarranz
 */
public class RequestLoadScriptInitialImpl extends RequestLoadScriptImpl implements RequestStfulDocument
{
    /**
     * Creates a new instance of RequestNormalLoadDocImpl
     */
    public RequestLoadScriptInitialImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return (ItsNatStfulDocumentImpl)getItsNatDocument();
    }

    @Override
    public void processRequest(ClientDocumentStfulImpl clientDocStateless)
    {
        String clientId = getAttrOrParamExist("itsnat_client_id"); // Si no está dará error

        ItsNatSessionImpl session = getItsNatSession();
        final ClientDocumentStfulImpl clientDoc = session.getClientDocumentStfulById(clientId);
        // Es raro que clientDoc sea nulo, si es así es que fue
        // invalidado por el código del cliente en carga
        // No procesamos por ahora un posible error pero debería hacerse.

        ItsNatDocumentImpl itsNatDoc = clientDoc.getItsNatDocumentImpl();

        // La verdad es que no es necesario sincronizar los padres porque este solo afecta al actual documento pero por ser sistemático...
        ItsNatDocSynchronizerImpl syncTask = new ItsNatDocSynchronizerImpl()
        {
            protected void syncMethod()
            {
                processThreadSync(clientDoc);
            }
        };
        syncTask.exec(itsNatDoc);
    }

    public void processThreadSync(ClientDocumentStfulImpl clientDoc)
    {
        bindClientToRequest(clientDoc);

        try
        {
            this.response = new ResponseLoadScriptInitialImpl(this);
            response.process();
        }
        finally
        {
            unbindRequestFromDocument();
        }
    }

}
