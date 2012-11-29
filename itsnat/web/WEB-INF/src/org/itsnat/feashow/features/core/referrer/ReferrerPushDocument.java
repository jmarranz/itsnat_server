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
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.event.NodePropertyTransport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

public class ReferrerPushDocument implements EventListener,ItsNatServletRequestListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected ElementList list;
    protected HTMLInputElement msgInput;
    protected Element addMsgButton;

    public ReferrerPushDocument(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = request.getItsNatDocument();

        load();
    }

    public void load()
    {
        itsNatDoc.addReferrerItsNatServletRequestListener(this);

        Document doc = itsNatDoc.getDocument();

        Element listParentElem = doc.getElementById("messageListId");

        boolean removePattern = true;
        Boolean value = (Boolean)itsNatDoc.getAttribute("removePattern");
        if (value != null)
            removePattern = value.booleanValue();

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

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        // Normal navigation or Forward button
        Document doc = itsNatDoc.getDocument();
        Element listParentElem = doc.getElementById("messageListId");
        Node contentNode = ItsNatDOMUtil.extractChildren(listParentElem.cloneNode(true));
        if (contentNode != null)
        {
            ItsNatDocument itsNatDocTarget = request.getItsNatDocument();
            Document docTarget = itsNatDocTarget.getDocument();

            Element listParentElemTarget = docTarget.getElementById("messageListId");
            contentNode = docTarget.importNode(contentNode,true);
            listParentElemTarget.appendChild(contentNode);
        }
    }

}
