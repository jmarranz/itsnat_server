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
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementLabel;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

public class PatternBasedElementLabelTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkToResolve;
    protected HTMLInputElement inputElem;
    protected ElementLabel label;

    public PatternBasedElementLabelTreeNode()
    {
    }

    public void startExamplePanel()
    {
        final ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkToResolve = doc.getElementById("linkToResolveId");
        ((EventTarget)linkToResolve).addEventListener("click",this,false);

        this.inputElem = (HTMLInputElement)doc.getElementById("inputElemId");
        ParamTransport prop = new NodePropertyTransport("value");
        itsNatDoc.addEventListener((EventTarget)inputElem,"change",this,false,prop);

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        this.label = factory.createElementLabel(doc.getElementById("elementId"),true,null);
    }

    public void endExamplePanel()
    {
        this.label = null;

        ((EventTarget)linkToResolve).removeEventListener("click",this,false);
        this.linkToResolve = null;

        ((EventTarget)inputElem).removeEventListener("change",this,false);
        this.inputElem = null;
    }

    public void handleEvent(Event evt)
    {
        if (linkToResolve == evt.getCurrentTarget())
            label.setLabelValue(inputElem.getValue());
    }

}
