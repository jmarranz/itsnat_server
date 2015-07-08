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

package org.itsnat.feashow.features.comp.buttons.normal;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class InputSubmitFormTreeNode extends FeatureTreeNode implements EventListener
{
    protected ItsNatHTMLInputSubmit inputComp;

    public InputSubmitFormTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.inputComp = (ItsNatHTMLInputSubmit)compMgr.createItsNatComponentById("inputId");

        inputComp.setEventListenerParams("click",false,CommMode.XHR_SYNC,null,null,-1);
        inputComp.addEventListener("click",this);
    }

    public void endExamplePanel()
    {
        this.inputComp.dispose();
        this.inputComp = null;
    }

    public void handleEvent(Event evt)
    {
        log(evt);

        evt.preventDefault(); // Prevent default (to cancel the submission) only works in SYNC mode

        log("Canceled");
    }

}
