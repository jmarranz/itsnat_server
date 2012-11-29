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

public class MarkupDrivenInputTextTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element parentElem;
    protected HTMLInputElement inputElem;
    protected Element changeElem;

    public MarkupDrivenInputTextTreeNode()
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

        this.inputElem = (HTMLInputElement)doc.getElementById("inputId");
        ((EventTarget)inputElem).addEventListener("change",this,false);

        this.changeElem = doc.getElementById("changeFromServerId");
        ((EventTarget)changeElem).addEventListener("click",this,false);

        if (isUCWEB() || isBolt())
        {
            Element ucwebElem = itsNatDoc.getDocument().getElementById("ucwebId");
            ucwebElem.removeAttribute("style");
        }
    }

    public void endExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        ((EventTarget)inputElem).removeEventListener("click",this,false);
        this.inputElem = null;

        ((EventTarget)changeElem).removeEventListener("click",this,false);
        this.changeElem = null;

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.removeItsNatComponents(parentElem,true);
        compMgr.setMarkupDrivenComponents(false);

        this.parentElem = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == changeElem)
            inputElem.setValue( "Text in server" );

        log("Current Value: " + inputElem.getValue());
    }
}
