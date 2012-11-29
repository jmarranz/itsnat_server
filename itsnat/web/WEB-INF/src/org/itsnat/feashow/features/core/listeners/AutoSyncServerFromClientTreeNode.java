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

package org.itsnat.feashow.features.core.listeners;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.NodeAttributeTransport;
import org.itsnat.core.event.NodeCompleteTransport;
import org.itsnat.core.event.NodeInnerTransport;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

public class AutoSyncServerFromClientTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkSyncAttr;
    protected Element linkSyncNodeInner;
    protected Element linkSyncNodeComp;
    protected HTMLInputElement inputSyncProp;

    public AutoSyncServerFromClientTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkSyncAttr = doc.getElementById("linkSyncAttrId");
        itsNatDoc.addEventListener((EventTarget)linkSyncAttr,"click",this,false,new NodeAttributeTransport("style"));
        // Alternative:
        // itsNatDoc.addEventListener((EventTarget)linkSyncAttr,"click",this,false,new NodeAllAttribTransport());

        this.linkSyncNodeInner = doc.getElementById("linkSyncNodeInnerId");
        itsNatDoc.addEventListener((EventTarget)linkSyncNodeInner,"click",this,false,new NodeInnerTransport());

        this.linkSyncNodeComp = doc.getElementById("linkSyncNodeCompId");
        itsNatDoc.addEventListener((EventTarget)linkSyncNodeComp,"click",this,false,new NodeCompleteTransport());

        this.inputSyncProp = (HTMLInputElement)doc.getElementById("linkSyncPropId");
        itsNatDoc.addEventListener((EventTarget)inputSyncProp,"change",this,false,new NodePropertyTransport("value"));
        // Alternative:
        //itsNatDoc.addEventListener((EventTarget)inputSyncProp,"change",this,false,new NodePropertyTransport("value",String.class));
    }

    public void endExamplePanel()
    {
        ((EventTarget)linkSyncAttr).removeEventListener("click",this,false);
        this.linkSyncAttr = null;

        ((EventTarget)linkSyncNodeInner).removeEventListener("click",this,false);
        this.linkSyncNodeInner = null;

        ((EventTarget)linkSyncNodeComp).removeEventListener("click",this,false);
        this.linkSyncNodeComp = null;

        ((EventTarget)inputSyncProp).removeEventListener("change",this,false);
        this.inputSyncProp = null;
    }

    public void handleEvent(Event evt)
    {
        Element currTarget = (Element)evt.getCurrentTarget();
        if (currTarget == linkSyncAttr)
        {
            log("New style value: " + currTarget.getAttribute("style"));
        }
        else if (currTarget == linkSyncNodeInner)
        {
            Node newNode = currTarget.getLastChild();
            Text text = (Text)newNode.getFirstChild();
            log("New node : " + newNode + " " + text.getData());
        }
        else if (currTarget == linkSyncNodeComp)
        {
            Node newNode = currTarget.getLastChild();
            Text text = (Text)newNode.getFirstChild();
            log("New node : " + newNode + " " + text.getData());
            log("New style value: " + currTarget.getAttribute("style"));
        }
        else if (currTarget == inputSyncProp)
        {
            log("New value: " + inputSyncProp.getValue());
        }
    }

}
