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

package org.itsnat.impl.core.resp.shared.otherns;

import org.itsnat.impl.core.resp.shared.*;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.web.ItsNatSVGDocumentImpl;
import org.itsnat.impl.core.doc.web.ItsNatXULDocumentImpl;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegateOtherNSLoadDocImpl extends ResponseDelegateStfulWebLoadDocImpl
{

    /**
     * Creates a new instance of ResponseDelegateOtherNSLoadDocImpl
     */
    public ResponseDelegateOtherNSLoadDocImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public static ResponseDelegateOtherNSLoadDocImpl createResponseDelegateOtherNSLoadDoc(ResponseLoadStfulDocumentValid responseParent)
    {
        ItsNatStfulDocumentImpl itsNatDoc = responseParent.getItsNatStfulDocument();
        if (itsNatDoc instanceof ItsNatSVGDocumentImpl)
            return ResponseDelegateSVGLoadDocImpl.createResponseDelegateSVGLoadDoc(responseParent);
        else if (itsNatDoc instanceof ItsNatXULDocumentImpl)
            return new ResponseDelegateXULLoadDocImpl(responseParent);
        return null; // Nunca ocurre
    }

    public void setScriptContent(Element scriptElem,String code)
    {
        Document doc = scriptElem.getOwnerDocument();
        scriptElem.appendChild(doc.createCDATASection(code));
    }

    public String getJavaScriptDocumentName()
    {
        return "W3COtherNSDocument";
    }

    public String addMarkupToTheEndOfDoc(String docMarkup,String scriptsMarkup)
    {
        StringBuilder finalMarkup = new StringBuilder();

        int posRootTagEnd = docMarkup.lastIndexOf('<');
        String preScript = docMarkup.substring(0,posRootTagEnd);
        String posScript = docMarkup.substring(posRootTagEnd);

        finalMarkup.append(preScript);
        finalMarkup.append(scriptsMarkup);
        finalMarkup.append(posScript);

        return finalMarkup.toString();
    }

    protected void rewriteClientHTMLTextAreaProperties(HTMLTextAreaElement elem,StringBuilder code)
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();
        // Los <textarea> fuera de X/HTML ignoran el nodo de texto hijo
        processUIControlProperty(elem,"value",code,clientDoc);
    }
}
