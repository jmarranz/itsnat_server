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
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class ToDOMTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element link;
    protected Element ref;

    public ToDOMTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.link = doc.getElementById("linkId");
        ((EventTarget)link).addEventListener("click",this,false);

        this.ref = doc.getElementById("refId");
    }

    public void endExamplePanel()
    {
        ((EventTarget)link).removeEventListener("click",this,false);

        this.link = null;
        this.ref = null;
    }

    public void handleEvent(Event evt)
    {
        String code = "<b>New Markup Inserted</b><br />";
        ItsNatDocument itsNatDoc = getItsNatDocument();
        DocumentFragment docFrag = itsNatDoc.toDOM(code);
        ref.getParentNode().insertBefore(docFrag, ref);
    }
}
