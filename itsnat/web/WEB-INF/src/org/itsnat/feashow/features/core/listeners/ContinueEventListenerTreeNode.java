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

import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatContinueEvent;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class ContinueEventListenerTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkToStart;

    public ContinueEventListenerTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkToStart = doc.getElementById("linkToStartId");

        ((EventTarget)linkToStart).addEventListener("click",this,false);
    }

    public void endExamplePanel()
    {
        ((EventTarget)linkToStart).removeEventListener("click",this,false);

        this.linkToStart = null;
    }

    public void handleEvent(Event evt)
    {
        // EventTarget currTarget = evt.getCurrentTarget();

        log("Uh! I forgot the document tittle!");

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ItsNatContinueEvent contEvt = (ItsNatContinueEvent)evt;
                String title = (String)contEvt.getExtraParam("title");
                log("OK this is the title: " + title + " ");
            }
        };

        ItsNatEvent itsNatEvent = (ItsNatEvent)evt;
        ClientDocument clientDoc = itsNatEvent.getClientDocument();
        ParamTransport[] extraParams = new ParamTransport[] { new CustomParamTransport("title","document.title") };
        clientDoc.addContinueEventListener(null,listener,itsNatEvent.getCommMode(),extraParams,null,-1);
    }

}
