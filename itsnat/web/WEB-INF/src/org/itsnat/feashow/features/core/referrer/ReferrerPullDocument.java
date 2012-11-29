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
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.NodePropertyTransport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;


public class ReferrerPullDocument implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected ElementList list;
    protected HTMLInputElement msgInput;
    protected Element addMsgButton;

    public ReferrerPullDocument(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = request.getItsNatDocument();

        load(request,response);
    }

    public void load(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        Document doc = itsNatDoc.getDocument();

        Element listParentElem = doc.getElementById("messageListId");

        boolean removePattern = true;
        ItsNatDocument itsNatDocRef = request.getItsNatDocumentReferrer();
        if (itsNatDocRef != null) // Reload or Back button from feashow.core.referrerExampleResult
        {
            Document docRef = itsNatDocRef.getDocument();

            Element listParentElemRef = docRef.getElementById("messageListId");
            Node contentNode = ItsNatDOMUtil.extractChildren(listParentElemRef.cloneNode(true));
            if (contentNode != null)
            {
                if (ItsNatTreeWalker.hasChildElements(contentNode))
                {
                    ItsNatDOMUtil.removeAllChildren(listParentElem); // Remove the row pattern

                    contentNode = doc.importNode(contentNode,true);
                    listParentElem.appendChild(contentNode);
                    removePattern = false;
                }
            }
        }
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        this.list = factory.createElementList(listParentElem,removePattern);

        this.msgInput = (HTMLInputElement)doc.getElementById("newMessageId");
        itsNatDoc.addEventListener((EventTarget)msgInput,"change",this,false,new NodePropertyTransport("value"));

        this.addMsgButton = doc.getElementById("addMessageId");
        ((EventTarget)addMsgButton).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        // ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == addMsgButton)
        {
            String newMsg = msgInput.getValue();
            if (!newMsg.equals(""))
            {
                list.addElement(newMsg);
                msgInput.setValue("");
            }
        }
    }

}
