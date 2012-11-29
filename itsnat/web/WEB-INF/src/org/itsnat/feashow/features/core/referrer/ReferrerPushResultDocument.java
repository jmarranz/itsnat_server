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


import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ReferrerPushResultDocument implements ItsNatServletRequestListener,Serializable
{
    protected ItsNatDocument itsNatDoc;

    public ReferrerPushResultDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load();
    }

    public void load()
    {
        itsNatDoc.addReferrerItsNatServletRequestListener(this);
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        // Link to Self or Back Button
        Document doc = itsNatDoc.getDocument();
        Element listParentElem = doc.getElementById("messageListId");
        Node contentNode = ItsNatDOMUtil.extractChildren(listParentElem.cloneNode(true));

        ItsNatDocument itsNatDocTarget = request.getItsNatDocument();
        Document docTarget = itsNatDocTarget.getDocument();
        Element listParentElemTarget = docTarget.getElementById("messageListId");

        if (contentNode != null)
        {
            String thisName = itsNatDoc.getItsNatDocumentTemplate().getName();
            String targetName = itsNatDocTarget.getItsNatDocumentTemplate().getName();
            if (!thisName.equals(targetName))
            {
                // Back button
                if (ItsNatTreeWalker.hasChildElements(contentNode))
                {
                    ItsNatDOMUtil.removeAllChildren(listParentElemTarget); // Remove the row pattern
                    itsNatDocTarget.setAttribute("removePattern",Boolean.FALSE);
                }
                else
                    itsNatDocTarget.setAttribute("removePattern",Boolean.TRUE);
            }
            else
            {
                // Link to Self, nothing to do
            }

            contentNode = docTarget.importNode(contentNode,true);
            listParentElemTarget.appendChild(contentNode);
        }
    }

}
