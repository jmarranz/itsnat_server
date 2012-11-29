/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */
package org.itsnat.feashow;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IndexDocumentLoadListener implements ItsNatServletRequestListener
{
    public IndexDocumentLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        HttpServletRequest servReq = (HttpServletRequest)request.getServletRequest();
        ServletContext context = servReq.getSession().getServletContext();
        boolean gaeEnabled = context.getServerInfo().startsWith("Google App Engine");

        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        String elemHideId = gaeEnabled ? "notGAE" : "GAE";
        Element elemHide = doc.getElementById(elemHideId);
        elemHide.setAttribute("style","display:none");
    }
}
