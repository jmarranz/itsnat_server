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

package org.itsnat.impl.core.resp.norm;

import org.itsnat.impl.core.resp.*;
import org.itsnat.impl.comp.iframe.HTMLIFrameFileUploadImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.req.norm.RequestIFrameFileUploadImpl;
import org.itsnat.impl.core.req.norm.RequestNormal;

/**
 *
 * @author jmarranz
 */
public class ResponseIFrameFileUploadImpl extends ResponseAlreadyLoadedDocImpl implements ResponseNormal
{
    protected HTMLIFrameFileUploadImpl listener;

    /** Creates a new instance of ResponseNormalEventImpl */
    public ResponseIFrameFileUploadImpl(RequestIFrameFileUploadImpl request,HTMLIFrameFileUploadImpl listener)
    {
        super(request);

        this.listener = listener; // No puede ser nulo
    }

    public ClientDocumentStfulImpl getClientStfulDocument()
    {
        return (ClientDocumentStfulImpl)getClientDocument();
    }

    public RequestNormal getRequestNormal()
    {
        return (RequestNormal)request;
    }

    public RequestIFrameFileUploadImpl getRequestIFrameFileUpload()
    {
        return (RequestIFrameFileUploadImpl)request;
    }

    protected void processResponse()
    {
        listener.dispatchRequestListeners(itsNatResponse);
    }
}
