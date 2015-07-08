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

package org.itsnat.feashow.features.comp.autobuild.mdforms;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

public class MarkupDrivenRadioTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element parentElem;
    protected HTMLInputElement inputElem1;
    protected HTMLInputElement inputElem2;
    protected Element toggleElem;

    public MarkupDrivenRadioTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.parentElem = doc.getElementById("compGroupId");

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.setMarkupDrivenComponents(true);
        compMgr.buildItsNatComponents(parentElem);

        this.inputElem1 = (HTMLInputElement)doc.getElementById("radioId1");
        ((EventTarget)inputElem1).addEventListener("click",this,false);

        this.inputElem2 = (HTMLInputElement)doc.getElementById("radioId2");
        ((EventTarget)inputElem2).addEventListener("click",this,false);

        this.toggleElem = doc.getElementById("toggleId");
        ((EventTarget)toggleElem).addEventListener("click",this,false);
    }

    public void endExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        ((EventTarget)inputElem1).removeEventListener("click",this,false);
        this.inputElem1 = null;

        ((EventTarget)inputElem2).removeEventListener("click",this,false);
        this.inputElem2 = null;

        ((EventTarget)toggleElem).removeEventListener("click",this,false);
        this.toggleElem = null;

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.removeItsNatComponents(parentElem,true);
        compMgr.setMarkupDrivenComponents(false);

        this.parentElem = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == toggleElem)
        {
            // Cycle: (false,false) => (true,false) => (false,true) =>
            boolean check1 = inputElem1.getChecked();
            boolean check2 = inputElem2.getChecked();
            if (!check1 && !check2) // (false,false)
                inputElem1.setChecked(true); // (true,false)
            else if (check1 && !check2) // (true,false)
                inputElem2.setChecked(true); // (false,true)
            else if (!check1 && check2) // (false,true)
                inputElem2.setChecked(false); // (false,false)
        }

        log("State changed to: " + inputElem1.getChecked() + " - " + inputElem2.getChecked());
    }
}
