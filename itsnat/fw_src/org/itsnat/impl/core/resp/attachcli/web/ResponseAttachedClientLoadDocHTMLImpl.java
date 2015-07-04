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

package org.itsnat.impl.core.resp.attachcli.web;

import org.itsnat.impl.core.clientdoc.web.SVGWebInfoImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClientLoadDocImpl;
import org.itsnat.impl.core.resp.shared.html.ResponseDelegateHTMLLoadDocImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ResponseAttachedClientLoadDocHTMLImpl extends ResponseAttachedClientLoadDocWebImpl
{
    /** Creates a new instance of ResponseAttachedClientLoadDocImpl */
    public ResponseAttachedClientLoadDocHTMLImpl(RequestAttachedClientLoadDocImpl request)
    {
        super(request);
    }

    public ResponseDelegateHTMLLoadDocImpl getResponseDelegateHTMLLoadDoc()
    {
        return (ResponseDelegateHTMLLoadDocImpl)responseDelegate;
    }

    public void processResponse()
    {
        // Debe llamarse antes del copyCacheFromOwner y el
        // clientDoc.registerInSession() (que renderiza los listeners de documento)
        // pues influye en ellos.
        getResponseDelegateHTMLLoadDoc().detectSVGWeb();

        super.processResponse();
    }

    protected boolean isIgnoredNodeForCaching(Node node)
    {
        // No cacheamos nodos SVG SVGWeb pues en carga hasta que no se renderiza el contenido del <script type="image/svg+xml"> los nodos SVG no existen
        // (y aunque existieran, caso de inserción dinámica, hasta que no se renderiza no son los definitivos).
        
        ClientDocumentStfulDelegateWebImpl clientDoc = (ClientDocumentStfulDelegateWebImpl)getClientDocumentStful().getClientDocumentStfulDelegate();
        return SVGWebInfoImpl.isSVGNodeProcessedBySVGWebFlash(node,clientDoc);
    }
}
