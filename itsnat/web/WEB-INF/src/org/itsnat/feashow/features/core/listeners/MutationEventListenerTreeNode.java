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
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;


public class MutationEventListenerTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element panelElem;

    public MutationEventListenerTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.panelElem = doc.getElementById("mutationPanelId");

        itsNatDoc.addMutationEventListener((EventTarget)panelElem,this,false);
    }

    public void endExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        itsNatDoc.removeMutationEventListener((EventTarget)panelElem,this,false);

        this.panelElem = null;
    }

    public void handleEvent(Event evt)
    {
        MutationEvent mutEvent = (MutationEvent)evt;

        String msg = "";
        String type = mutEvent.getType();
        if (type.equals("DOMNodeInserted"))
        {
            Element parent = (Element)mutEvent.getRelatedNode();
            Node newNode = (Node)mutEvent.getTarget();

            Node sibling = newNode.getNextSibling();
            if (sibling != null)
                msg = newNode.getNodeName() + " sibling: " + sibling.getNodeName() + " parent: " + parent.getNodeName();
            else
                msg = newNode.getNodeName() + " parent: " + parent.getNodeName();
        }
        else if (type.equals("DOMNodeRemoved"))
        {
            Element parent = (Element)mutEvent.getRelatedNode();
            Node removedNode = (Node)mutEvent.getTarget();

            msg = removedNode.getNodeName() + " parent: " + parent.getNodeName();
        }
        else if (type.equals("DOMAttrModified"))
        {
            //Attr attr = (Attr)mutEvent.getRelatedNode();
            //Element elem = (Element)mutEvent.getTarget();
            String attrName = mutEvent.getAttrName();
            int changeType = mutEvent.getAttrChange();
            switch(changeType)
            {
                case MutationEvent.ADDITION:
                    msg = "added, name: " + attrName + " , value: " + mutEvent.getNewValue();
                    break;
                case MutationEvent.MODIFICATION:
                    msg = "updated, name: " + attrName + " , new value: " + mutEvent.getNewValue();
                    break;
                case MutationEvent.REMOVAL:
                    msg = "removed, name: " + attrName;
                    break;
            }
        }
        else if (type.equals("DOMCharacterDataModified"))
        {
            //CharacterData charNode = (CharacterData)mutEvent.getTarget();
            msg = "new value: " + mutEvent.getNewValue();
        }

        log(mutEvent.getType() + " " + msg + "\n");
    }

}
