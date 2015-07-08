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

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.ItsNatEventListenerChain;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class EvtListChainCtrlTreeNode extends FeatureTreeNode
{
    protected Element linkToStart;
    protected EventListener globalListener;
    protected EventListener[] listeners = new EventListener[3];

    public EvtListChainCtrlTreeNode()
    {
    }

    public void startExamplePanel()
    {
        final ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkToStart = doc.getElementById("linkToStartId");

        this.globalListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                if (!(evt instanceof ItsNatDOMStdEvent)) return;

                ItsNatDOMStdEvent itsNatEvt = ((ItsNatDOMStdEvent)evt);
                String num = (String)itsNatEvt.getExtraParam("num");
                if (num == null) return; // Any other
                log("Enter in global listener");
                ItsNatEventListenerChain chain = itsNatEvt.getItsNatEventListenerChain();
                try
                {
                    if (num.equals("1")||num.equals("2"))
                        chain.continueChain();
                    else
                        chain.stop(); // Avoids third listener
                }
                catch(Exception ex)
                {
                    log("Catched exception thrown by first listener");
                }
                finally
                {
                    log("Exit from global listener");
                }
            }
        };
        itsNatDoc.addEventListener(globalListener);

        listeners[0] = new EventListener() // First listener
        {
            public void handleEvent(Event evt)
            {
                throw new RuntimeException("Must be catched by global");
            }
        };
        itsNatDoc.addEventListener((EventTarget)linkToStart,"click",listeners[0],false,new CustomParamTransport("num","1"));

        listeners[1] = new EventListener() // Second listener
        {
            public void handleEvent(Event evt)
            {
                log("Second listener executed");
            }
        };
        itsNatDoc.addEventListener((EventTarget)linkToStart,"click",listeners[1],false,new CustomParamTransport("num","2"));

        listeners[2] = new EventListener() // Third listener
        {
            public void handleEvent(Event evt)
            {
                throw new RuntimeException("NEVER THROWN!");
            }
        };
        itsNatDoc.addEventListener((EventTarget)linkToStart,"click",listeners[2],false,new CustomParamTransport("num","3"));
    }

    public void endExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        itsNatDoc.removeEventListener(globalListener);

        for(int i = 0; i < listeners.length; i++)
            ((EventTarget)linkToStart).removeEventListener("click",listeners[i],false);

        this.linkToStart = null;
    }
}
