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

public class CommModesTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element xhrSyncModeElem;
    protected Element xhrAsyncHoldModeElem;
    protected Element xhrAsyncModeElem;
    protected Element scriptModeElem;
    protected Element scriptHoldModeElem;

    public CommModesTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.xhrSyncModeElem = doc.getElementById("xhrSyncModeId");
        this.xhrAsyncModeElem = doc.getElementById("xhrAsyncModeId");
        this.xhrAsyncHoldModeElem = doc.getElementById("xhrAsyncHoldModeId");
        this.scriptModeElem = doc.getElementById("scriptModeId");
        this.scriptHoldModeElem = doc.getElementById("scriptHoldModeId");

        itsNatDoc.addEventListener((EventTarget)xhrSyncModeElem,"click",this,false,CommMode.XHR_SYNC);
        itsNatDoc.addEventListener((EventTarget)xhrAsyncModeElem,"click",this,false,CommMode.XHR_ASYNC);
        itsNatDoc.addEventListener((EventTarget)xhrAsyncHoldModeElem,"click",this,false,CommMode.XHR_ASYNC_HOLD);
        itsNatDoc.addEventListener((EventTarget)scriptModeElem,"click",this,false,CommMode.SCRIPT);
        itsNatDoc.addEventListener((EventTarget)scriptHoldModeElem,"click",this,false,CommMode.SCRIPT_HOLD);
    }

    public void endExamplePanel()
    {
        ((EventTarget)xhrSyncModeElem).removeEventListener("click",this,false);
        ((EventTarget)xhrAsyncModeElem).removeEventListener("click",this,false);
        ((EventTarget)xhrAsyncHoldModeElem).removeEventListener("click",this,false);
        ((EventTarget)scriptModeElem).removeEventListener("click",this,false);
        ((EventTarget)scriptHoldModeElem).removeEventListener("click",this,false);

        this.xhrSyncModeElem = null;
        this.xhrAsyncModeElem = null;
        this.xhrAsyncHoldModeElem = null;
        this.scriptModeElem = null;
        this.scriptHoldModeElem = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        try{ Thread.sleep(1000); } catch(InterruptedException ex){ }

        String msg = "";
        if (currTarget == xhrSyncModeElem)
            msg = "XHR_SYNC";
        else if (currTarget == xhrAsyncModeElem)
            msg = "XHR_ASYNC";
        else if (currTarget == xhrAsyncHoldModeElem)
            msg = "XHR_ASYNC HOLD";
        else if (currTarget == scriptModeElem)
            msg = "SCRIPT";
        else if (currTarget == scriptHoldModeElem)
            msg = "SCRIPT_HOLD";
        
        log("Clicked : " + msg);
    }

}
