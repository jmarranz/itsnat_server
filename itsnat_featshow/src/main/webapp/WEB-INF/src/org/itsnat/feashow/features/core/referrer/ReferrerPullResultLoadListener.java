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

package org.itsnat.feashow.features.core.referrer;


import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ReferrerPullResultLoadListener implements ItsNatServletRequestListener
{
    public ReferrerPullResultLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        ItsNatDocument itsNatDocRef = request.getItsNatDocumentReferrer();
        if (itsNatDocRef != null) // Reload, Normal navigation or "forward" button from "feashow.ext.core.referrer.pullExample" page
        {
            Document docRef = itsNatDocRef.getDocument();

            Element listParentElem = doc.getElementById("messageListId");
            Element listParentElemRef = docRef.getElementById("messageListId");
            Node contentNode = ItsNatDOMUtil.extractChildren(listParentElemRef.cloneNode(true));
            if (contentNode != null)
            {
                contentNode = doc.importNode(contentNode,true);
                listParentElem.appendChild(contentNode);
            }
        }
        else
        {
            itsNatDoc.addCodeToSend("alert('Referrer lost');");
        }
    }

}
