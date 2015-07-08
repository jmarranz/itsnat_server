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
package org.itsnat.feashow.features.core.misc.remctrl;

import javax.servlet.ServletRequest;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.ItsNatVariableResolver;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;

public class RemCtrlReqRejectedLoadListener implements ItsNatServletRequestListener
{
    public RemCtrlReqRejectedLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ServletRequest servRequest = request.getServletRequest();
        String reason = servRequest.getParameter("reason");
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver(true);
        resolver.setLocalVariable("reason",reason);
        resolver.resolve(doc);
    }
}
