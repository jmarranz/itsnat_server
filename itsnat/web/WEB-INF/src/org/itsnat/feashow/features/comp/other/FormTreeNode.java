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

package org.itsnat.feashow.features.comp.other;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.ItsNatHTMLForm;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchor;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;


public class FormTreeNode extends FeatureTreeNode implements EventListener
{
    protected ItsNatHTMLForm formComp;
    protected ItsNatHTMLAnchor linkComp;

    public FormTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.formComp = (ItsNatHTMLForm)compMgr.createItsNatComponentById("formId");

        formComp.setEventListenerParams("submit",false,CommMode.XHR_SYNC,null,null,-1);
        formComp.setEventListenerParams("reset",false,CommMode.XHR_SYNC,null,null,-1);

        formComp.addEventListener("submit",this);
        formComp.addEventListener("reset",this);

        this.linkComp = (ItsNatHTMLAnchor)compMgr.createItsNatComponentById("linkId");
        linkComp.addEventListener("click",this);
    }

    public void endExamplePanel()
    {
        this.formComp.dispose();
        this.formComp = null;

        this.linkComp.dispose();
        this.linkComp = null;
    }

    public void handleEvent(Event evt)
    {
        log(evt);

        EventTarget currentTarget = evt.getCurrentTarget();
        if (currentTarget == formComp.getHTMLFormElement())
        {
            if (evt.getType().equals("submit"))
            {
                log("Submit canceled");
                evt.preventDefault(); // Cancels the submission, only works in SYNC mode
            }
            // reset is not cancellable
        }
        else
        {
            formComp.reset(); // submit() method is defined too
        }
    }
}
