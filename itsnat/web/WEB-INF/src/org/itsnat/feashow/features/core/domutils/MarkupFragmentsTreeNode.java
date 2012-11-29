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
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.tmpl.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

public class MarkupFragmentsTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element includeParent;
    protected HTMLInputElement button;
    protected boolean included;
    protected ItsNatHTMLDocFragmentTemplate docFragTemplate;

    public MarkupFragmentsTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.includeParent = doc.getElementById("includeId");

        this.button = (HTMLInputElement)doc.getElementById("buttonId");
        ((EventTarget)button).addEventListener("click",this,false);

        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        this.docFragTemplate = (ItsNatHTMLDocFragmentTemplate)servlet.getItsNatDocFragmentTemplate("feashow.fragmentExample");

        include();
    }

    public void endExamplePanel()
    {
        ((EventTarget)button).removeEventListener("click",this,false);

        this.includeParent = null;
        this.button = null;
        this.docFragTemplate = null;
    }

    public void handleEvent(Event evt)
    {
        if (included)
            uninclude();
        else
            include();
    }

    public void include()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        DocumentFragment docFrag = docFragTemplate.loadDocumentFragmentBody(itsNatDoc);

        includeParent.appendChild(docFrag); // docFrag is empty now

        button.setValue("Remove Included");

        this.included = true;
    }

    public void uninclude()
    {
        ItsNatDOMUtil.removeAllChildren(includeParent);

        button.setValue("Include");

        this.included = false;
    }

}
