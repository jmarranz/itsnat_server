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

package org.itsnat.feashow.features.core.serverevts;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

public class ServerEventsUsingBrowserTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkElem;
    protected Element[] buttonElems;
    protected Element userButton;

    public ServerEventsUsingBrowserTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkElem = (Element)doc.getElementById("linkId");
        ((EventTarget)linkElem).addEventListener("click",this,false);

        this.buttonElems = new Element[3];
        for(int i = 0; i < buttonElems.length; i++)
            buttonElems[i] = doc.getElementById("buttonId" + i);
        for(int i = 0; i < buttonElems.length; i++)
            ((EventTarget)buttonElems[i]).addEventListener("click",this,false);

        this.userButton = doc.getElementById("userButtonId");
        itsNatDoc.addUserEventListener((EventTarget)userButton,"myEvent",this);
        userButton.setAttribute("onclick","document.getItsNatDoc().fireUserEvent(this,'myEvent');");
    }

    public void endExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        ((EventTarget)linkElem).removeEventListener("click",this,false);
        this.linkElem = null;

        for(int i = 0; i < buttonElems.length; i++)
            ((EventTarget)buttonElems[i]).removeEventListener("click",this,false);
        this.buttonElems = null;

        itsNatDoc.removeUserEventListener((EventTarget)userButton,"myEvent",this);
        this.userButton = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == linkElem)
        {
            fireEventsFromServer();
        }
        else
        {
            log("Clicked: " + ((Element)currTarget).getAttribute("value"));
        }
    }

    public void fireEventsFromServer()
    {
        final ItsNatDocument itsNatDoc = getItsNatDocument();
        final ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner();

        Runnable dispCode = new Runnable()
        {
            public void run()
            {
                for(int i = 0; i < buttonElems.length; i++)
                {
                    Element buttonElem;
                    MouseEvent mouseEvt;
                    synchronized(itsNatDoc)
                    {
                        Document doc = itsNatDoc.getDocument();
                        AbstractView view = ((DocumentView)doc).getDefaultView();

                        mouseEvt = (MouseEvent)((DocumentEvent)doc).createEvent("MouseEvents");
                        mouseEvt.initMouseEvent("click",true,true,view,0,
                                0,0,0,0,false,false,false,false,(short)0/*left button*/,null);

                        buttonElem = buttonElems[i];
                    }

                    ((EventTarget)buttonElem).dispatchEvent(mouseEvt);
                    // Alternatives:
                    // itsNatDoc.dispatchEvent((EventTarget)buttonElem,mouseEvt);
                    // clientDoc.dispatchEvent((EventTarget)buttonElem,mouseEvt);
                }

                ItsNatUserEvent userEvt;
                synchronized(itsNatDoc)
                {
                    Document doc = itsNatDoc.getDocument();
                    userEvt = (ItsNatUserEvent)((DocumentEvent)doc).createEvent("itsnat:UserEvents");
                    userEvt.initEvent("itsnat:user:myEvent",false,false);
                }

                ((EventTarget)userButton).dispatchEvent(userEvt);
            }
        };

        clientDoc.startEventDispatcherThread(dispCode);
    }
}
