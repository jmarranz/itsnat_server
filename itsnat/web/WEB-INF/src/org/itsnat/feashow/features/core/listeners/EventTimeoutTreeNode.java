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

package org.itsnat.feashow.features.core.listeners;

import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class EventTimeoutTreeNode extends FeatureTreeNode implements EventListener
{
    protected static final long eventTimeout = 1000;
    protected Element linkToStart;

    public EventTimeoutTreeNode()
    {
    }

    public void startExamplePanel()
    {
        final ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkToStart = doc.getElementById("linkToStartId");

        itsNatDoc.addEventListener((EventTarget)linkToStart,"click",this,false,
                CommMode.XHR_ASYNC,null,null,eventTimeout);
    }

    public void endExamplePanel()
    {
        ((EventTarget)linkToStart).removeEventListener("click",this,false);

        this.linkToStart = null;
    }

    public void handleEvent(Event evt)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        try
        {
            Thread.sleep(eventTimeout + 1000); // greater than event timeout
        }
        catch(InterruptedException ex) { }

        itsNatDoc.addCodeToSend("alert('Message never shown');");
    }
}
