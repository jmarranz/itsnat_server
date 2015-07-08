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

package org.itsnat.feashow.features.core.domutils;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class DisconnectNodesTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element link;
    protected Element container;
    protected Element disconnectChildNode;
    protected DocumentFragment childNodes = null;

    public DisconnectNodesTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.link = doc.getElementById("testDisconnectNodeId");
        ((EventTarget)link).addEventListener("click",this,false);
        this.container = doc.getElementById("testDisconnectNodeContainerId");
        this.disconnectChildNode = doc.getElementById("disconnectChildNodeId");

        setupToDisconnectChildNodes();
    }

    public void endExamplePanel()
    {
        ((EventTarget)link).removeEventListener("click",this,false);
        this.link = null;
        this.container = null;
        ((EventTarget)disconnectChildNode).removeEventListener("click",this,false);
        this.disconnectChildNode = null;
        this.childNodes = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == link)
        {
            if (childNodes == null)  // Are connected => disconnect
                disconnectChildNodesFromClient();
            else   // Are disconnected => reconnect
                reconnectChildNodesToClient();
        }
        else // disconnectChildNode
        {
            ItsNatDOMUtil.setTextContent(disconnectChildNode, "OK");
        }
    }

    public void disconnectChildNodesFromClient()
    {
        // Because is going to be removed (is not mandatory but recommended)
        ((EventTarget)disconnectChildNode).addEventListener("click",this,false);

        ItsNatDocument itsNatDoc = getItsNatDocument();
        this.childNodes = (DocumentFragment)itsNatDoc.disconnectChildNodesFromClient(container);
        if (container.hasChildNodes())
            throw new RuntimeException("Unexpected Error");

        setupToReconnectChildNodes();
    }

    public void reconnectChildNodesToClient()
    {
        // This code shows how we can automatically reconnect the content of the node by adding
        // a new child node.
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Element elemTemp = itsNatDoc.getDocument().createElement("input");
        container.appendChild(elemTemp);
        if (itsNatDoc.isDisconnectedChildNodesFromClient(container))
            throw new RuntimeException("Unexpected Error");

        // Disconnecting again (the container only has one child node)
        Element elemTemp2 = (Element)itsNatDoc.disconnectChildNodesFromClient(container);
        if (elemTemp != elemTemp2) throw new RuntimeException("Unexpected Error");

        // Finally explicit reconnection, previous content is restored, the client DOM
        // is again in sync with server removing current content in client
        itsNatDoc.reconnectChildNodesToClient(container);
        container.appendChild(childNodes);
        this.childNodes = null;

        setupToDisconnectChildNodes();
    }

    public void setupToDisconnectChildNodes()
    {
        ItsNatDOMUtil.setTextContent(link, "Click to Disconnect Nodes");
        ItsNatDOMUtil.setTextContent(disconnectChildNode, "Click Me Before Disconnect");

        ((EventTarget)disconnectChildNode).addEventListener("click",this,false);
    }

    public void setupToReconnectChildNodes()
    {
        ItsNatDOMUtil.setTextContent(link, "Click to Reconnect Nodes");

        // This code changes the client DOM with JavaScript to show the client DOM remains untouched
        // in spite of the same DOM is missing in server. Now this client zone
        // (the content of the disconnected node) can be freely modified

        StringBuilder code = new StringBuilder();
        code.append("var elem = document.getElementById('disconnectChildNodeId');");
        code.append("elem.innerHTML = \"<span style='border: solid blue 1px'><b>Rewritten By JavaScript</b></span>\"; ");
        ItsNatDocument itsNatDoc = getItsNatDocument();
        itsNatDoc.addCodeToSend(code.toString());
    }
}
