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

package org.itsnat.impl.core.resp.attachsrv;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserMSIE9Up;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerImpl;
import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerPrepareImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseAttachedServerPrepareImpl extends ResponseAttachedServerPrepareBaseImpl
{

    /** Creates a new instance of ResponseNormalLoadDocImpl */
    public ResponseAttachedServerPrepareImpl(RequestAttachedServerPrepareImpl request)
    {
        super(request);
    }

    public RequestAttachedServerPrepareImpl getRequestAttachedServerPrepare()
    {
        return (RequestAttachedServerPrepareImpl)request;
    }

    public ClientDocumentAttachedServerImpl getClientDocumentAttachedServer()
    {
        return (ClientDocumentAttachedServerImpl)getClientDocument();
    }

    protected void processResponse()
    {
        ClientDocumentAttachedServerImpl clientDoc = getClientDocumentAttachedServer();
        clientDoc.registerInSession();

        String code = genSendMarkupCode();
        writeResponse(code);
    }

    public abstract String genSendMarkupCodeByMethod();

    public String genSendMarkupCode()
    {
        ClientDocumentAttachedServerImpl clientDoc = getClientDocumentAttachedServer();
        Browser browser = clientDoc.getBrowser();

        StringBuilder code = new StringBuilder();
        if ((browser instanceof BrowserMSIEOld)||(browser instanceof BrowserMSIE9Up)) // IE 9 no tiene XMLSerializer
        {
            // MSIE ve el <!DOCTYPE> como un comentario, en el texto del mismo está
            // la declaración de forma correcta. El elemento no puede ser otro que el <html>
            code.append("var markupOrig = \"\";\n");
            code.append("for(var i = 0; i < document.childNodes.length ; i++)\n");
            code.append("{ \n");
            code.append("    var node = document.childNodes[i];\n");
            code.append("    if (node.nodeType == 8) markupOrig += node.text;\n"); // Node.COMMENT_NODE
            code.append("    else if (node.nodeType == 1) markupOrig += node.outerHTML;\n"); // Node.ELEMENT_NODE
            code.append("}\n");
        }
        else // Navegadores W3C (excepto IE9)
        {
            // Hay que estudiar más navegadores aparte de FireFox, Chrome y Safari
            code.append("var markupOrig = new XMLSerializer().serializeToString(document);");
        }

        code.append("var itsnat = new Object();\n");

        code.append( genSendMarkupCodeByMethod() );

        code.append("itsnat.init(markupOrig);\n");

        return code.toString();
    }
}
