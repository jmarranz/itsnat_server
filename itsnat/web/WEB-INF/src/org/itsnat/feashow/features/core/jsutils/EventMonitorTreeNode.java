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

package org.itsnat.feashow.features.core.jsutils;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class EventMonitorTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkElem;

    public EventMonitorTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkElem = (Element)doc.getElementById("linkId");
        ((EventTarget)linkElem).addEventListener("click",this,false);

        String code = "document.monitor = new EventMonitor(); \n" +
                      "document.getItsNatDoc().addEventMonitor(document.monitor); \n";
        itsNatDoc.addCodeToSend(code);
    }

    public void endExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        String code = "";
        code += "document.getItsNatDoc().removeEventMonitor(document.monitor); \n";
        itsNatDoc.addCodeToSend(code);

        ((EventTarget)linkElem).removeEventListener("click",this,false);
        this.linkElem = null;
    }

    public void handleEvent(Event evt)
    {
        try{ Thread.sleep(2000); } catch(InterruptedException ex) { }
    }
}
