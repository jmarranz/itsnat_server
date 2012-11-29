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

import java.util.Collections;
import java.util.Comparator;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementListFree;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

public class FreeElementListTreeNode extends FeatureTreeNode implements EventListener
{
    protected ElementListFree elemList;
    protected Element sortButton;
    protected Element rotateButton;
    protected Element shuffleButton;
    protected HTMLInputElement masterCheckBox;

    public FreeElementListTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.sortButton = doc.getElementById("sortId");
        ((EventTarget)sortButton).addEventListener("click",this,false);

        this.rotateButton = doc.getElementById("rotateId");
        ((EventTarget)rotateButton).addEventListener("click",this,false);

        this.shuffleButton = doc.getElementById("shuffleId");
        ((EventTarget)shuffleButton).addEventListener("click",this,false);

        this.masterCheckBox = (HTMLInputElement)doc.getElementById("masterId");
        itsNatDoc.addEventListener((EventTarget)masterCheckBox,"click",this,false,new NodePropertyTransport("checked",boolean.class));

        boolean master = masterCheckBox.getChecked();
        Element parent = doc.getElementById("elementListId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        this.elemList = factory.createElementListFree(parent,master);

        elemList.addElement(createElement("Madrid"));
        elemList.addElement(createElement("Sevilla"));
        elemList.addElement(createElement("Segovia"));
        elemList.addElement(createElement("Barcelona"));
        elemList.addElement(createElement("Valencia"));
        elemList.addElement(createElement("Oviedo"));
    }

    public Element createElement(String text)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element itemElem = doc.createElement("li");
        itemElem.appendChild(doc.createTextNode(text));
        return itemElem;
    }

    public void endExamplePanel()
    {
        this.elemList = null;
        this.sortButton = null;
        this.rotateButton = null;
        this.shuffleButton = null;
        this.masterCheckBox = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == sortButton)
        {
            Collections.sort(elemList,NodeComparator.SINGLETON);
        }
        else if (currTarget == rotateButton)
        {
            Collections.rotate(elemList,1);
        }
        else if (currTarget == shuffleButton)
        {
            Collections.shuffle(elemList);
        }
        else if (currTarget == masterCheckBox)
        {
            boolean master = masterCheckBox.getChecked();
            if (master != elemList.isMaster())
            {
                ItsNatDocument itsNatDoc = getItsNatDocument();
                ElementGroupManager factory = itsNatDoc.getElementGroupManager();
                elemList = factory.createElementListFree(elemList.getParentElement(),master);
            }
        }
    }

    public static class NodeComparator implements Comparator
    {
        public static final NodeComparator SINGLETON = new NodeComparator();

        public int compare(Object o1, Object o2)
        {
            Element li1 = (Element)o1;
            Text text1 = (Text)li1.getFirstChild();

            Element li2 = (Element)o2;
            Text text2 = (Text)li2.getFirstChild();

            return text1.getData().compareTo(text2.getData());
        }
    }
}
