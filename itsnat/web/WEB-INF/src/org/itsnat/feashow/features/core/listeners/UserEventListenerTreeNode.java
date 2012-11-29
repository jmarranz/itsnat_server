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
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class UserEventListenerTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkToStart;

    public UserEventListenerTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkToStart = doc.getElementById("linkToStartId");

        itsNatDoc.addUserEventListener((EventTarget)linkToStart,"myUserAction",this);

        String code = "var itsNatDoc = document.getItsNatDoc();" +
                      "var evt = itsNatDoc.createUserEvent('myUserAction');" +
                      "evt.setExtraParam('title',document.title);" +
                      "evt.setExtraParam('url',document.location);" +
                      "itsNatDoc.dispatchUserEvent(this,evt);";
        linkToStart.setAttribute("onclick",code);
    }

    public void endExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        itsNatDoc.removeUserEventListener((EventTarget)linkToStart,"myUserAction",this);

        this.linkToStart = null;
    }

    public void handleEvent(Event evt)
    {
        ItsNatUserEvent userEvt = (ItsNatUserEvent)evt;
        String title = (String)userEvt.getExtraParam("title");
        log("Page title: " + title);
        String url = (String)userEvt.getExtraParam("url");
        log("URL: " + url);
    }
}
