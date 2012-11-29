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

package org.itsnat.impl.core.resp.script;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.req.script.RequestLoadScriptInitialImpl;
import org.itsnat.impl.core.resp.ResponseStfulDocument;

/**
 *
 * @author jmarranz
 */
public class ResponseLoadScriptInitialImpl extends ResponseLoadScriptImpl implements ResponseStfulDocument
{

    /** Creates a new instance of ResponseNormalLoadDocImpl */
    public ResponseLoadScriptInitialImpl(RequestLoadScriptInitialImpl request)
    {
        super(request);
    }

    public RequestLoadScriptInitialImpl getRequestLoadScriptInitial()
    {
        return (RequestLoadScriptInitialImpl)request;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return getRequestLoadScriptInitial().getItsNatStfulDocument();
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return (ClientDocumentStfulImpl)getClientDocument();
    }

    public void processResponse()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();

        String code = clientDoc.getScriptLoadCode();
        writeResponse(code);
    }
}
