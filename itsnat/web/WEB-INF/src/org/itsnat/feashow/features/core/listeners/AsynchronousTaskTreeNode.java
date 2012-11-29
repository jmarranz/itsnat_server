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
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class AsynchronousTaskTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkToStart;

    public AsynchronousTaskTreeNode()
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
        final ItsNatDocument itsNatDoc = getItsNatDocument();
        ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();

        final Runnable task = new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(4000);
                }
                catch(InterruptedException ex) { }

                synchronized(itsNatDoc)
                {
                    log("Asynchronous task finished, id: " + hashCode());
                }
            }
        };

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                log("Finished really, id: " + task.hashCode());
            }
        };
        clientDoc.addAsynchronousTask(task,listener);

        log("An asynchronous task has started, id: " + task.hashCode());
    }

}
