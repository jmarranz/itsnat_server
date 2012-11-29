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
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.feashow.BrowserUtil;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class DOMEventListenerTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element clickable1;
    protected Element clickable2;

    public DOMEventListenerTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.clickable1 = doc.getElementById("clickableId1");
        this.clickable2 = doc.getElementById("clickableId2");

        enableFirst();
    }

    public void endExamplePanel()
    {
        ((EventTarget)clickable1).removeEventListener("click",this,false);
        ((EventTarget)clickable2).removeEventListener("click",this,false);

        this.clickable1 = null;
        this.clickable2 = null;
    }

    public void enableFirst()
    {
        enable(clickable1);
        disable(clickable2);
    }

    public void enableSecond()
    {
        disable(clickable1);
        enable(clickable2);
    }

    public void enable(Element clickable)
    {
        ((EventTarget)clickable).addEventListener("click",this,false);
        ItsNatDOMUtil.setTextContent(clickable,"You can click me!!");
        clickable.setAttribute("style","color:red;");
    }

    public void disable(Element clickable)
    {
        ((EventTarget)clickable).removeEventListener("click",this,false);
        ItsNatDOMUtil.setTextContent(clickable,"You cannot click me");
        clickable.removeAttribute("style");
    }

    public void handleEvent(Event evt)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        if (BrowserUtil.isUCWEB(((ItsNatEvent)evt).getItsNatServletRequest()))
            log("Clicked!!"); // alert() works bad in UCWEB
        else
            itsNatDoc.addCodeToSend("alert('Clicked!!');");

        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == clickable1)
        {
            enableSecond();
        }
        else
        {
            enableFirst();
        }

        log(evt);
    }

}
