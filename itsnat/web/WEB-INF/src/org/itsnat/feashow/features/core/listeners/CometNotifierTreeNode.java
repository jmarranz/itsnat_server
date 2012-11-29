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

import java.util.Date;
import org.itsnat.core.CometNotifier;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class CometNotifierTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkToStart;
    protected Thread backgroundThr;
    protected boolean endBgTask;
    protected CometNotifier notifier;
    protected Element timeElem;
    protected EventListener cometListener;

    public CometNotifierTreeNode()
    {
    }

    public void startExamplePanel()
    {
        final ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkToStart = doc.getElementById("linkToStartId");
        ((EventTarget)linkToStart).addEventListener("click",this,false);

        this.timeElem = doc.getElementById("timeId");

        this.endBgTask = false;
        this.backgroundThr = new Thread()
        {
            public void run()
            {
                synchronized(itsNatDoc)
                {
                    log("Background server task started");
                }
                long t1 = System.currentTimeMillis();
                long t2 = t1;
                do
                {
                    try
                    {
                        Thread.sleep(2000);
                    }
                    catch(InterruptedException ex) { }

                    CometNotifier notifier = CometNotifierTreeNode.this.notifier;
                    if ((notifier != null) && !notifier.isStopped())
                    {
                        synchronized(itsNatDoc)
                        {
                            log("Tick"); // Access/modifies the document
                        }
                        notifier.notifyClient();
                    }
                    t2 = System.currentTimeMillis();
                }
                while( !endBgTask && ((t2 - t1) < 1*60*1000) ); // Max 1 minute

                if (notifier != null) notifier.stop();

                synchronized(itsNatDoc)
                {
                    log("Background server task finished");
                }
            }
        };

        backgroundThr.start();
    }

    public void endExamplePanel()
    {
        ((EventTarget)linkToStart).removeEventListener("click",this,false);

        stopNotifier();

        this.linkToStart = null;
        this.timeElem = null;

        this.endBgTask = true;
        this.backgroundThr = null;
    }

    public void stopNotifier()
    {
        if (notifier == null) return;

        notifier.stop();

        notifier.removeEventListener(cometListener);

        this.cometListener = null;
        this.notifier = null;
    }

    public void handleEvent(Event evt)
    {
        if (notifier != null)
        {
            stopNotifier();
            log("COMET notifier stopped");
        }

        this.notifier = ((ItsNatEvent)evt).getClientDocument().createCometNotifier();
        this.cometListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                Text text = (Text)timeElem.getFirstChild();
                text.setData(new Date().toString());
            }
        };
        notifier.addEventListener(cometListener);

        log("COMET notifier started");
    }
}
