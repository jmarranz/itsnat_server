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
package org.itsnat.feashow.features.core.misc;


import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class PrettyURLDocument implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;

    public PrettyURLDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();

        Element elem = doc.getElementById("elemId");

        ((EventTarget)elem).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element p = doc.createElement("p");
        p.appendChild(doc.createTextNode("Clicked"));
        doc.getBody().appendChild(p);
    }

}
