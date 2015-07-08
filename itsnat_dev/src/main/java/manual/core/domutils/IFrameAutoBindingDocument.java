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
 * Author: Jose Maria Arranz Santamaria
 * (C) Innowhere Software Services S.L., Spanish company
 */

package manual.core.domutils;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatNode;
import org.itsnat.core.event.ItsNatUserEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class IFrameAutoBindingDocument implements EventListener
{
    protected ItsNatDocument itsNatDoc;

    public IFrameAutoBindingDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        itsNatDoc.addUserEventListener(null,"update",this);

        prepareButtonToSend();
    }

    public void handleEvent(Event evt)
    {
        if (evt instanceof ItsNatUserEvent) // Button received
        {
            prepareButtonToSend();
        }
        else // Button clicked
        {
            sendToParent();
        }
    }

    public void prepareButtonToSend()
    {
        Document doc = itsNatDoc.getDocument();
        Element button = doc.getElementById("buttonId");

        Text text = (Text)button.getFirstChild();
        text.setData("Send to Parent");

        ((EventTarget)button).addEventListener("click",this,false);
    }

    public void sendToParent()
    {
        // Synchronization not needed, parent document is ever synchronized
        Element iframeElem = (Element)itsNatDoc.getContainerNode();
        if (iframeElem == null)
        {
            itsNatDoc.addCodeToSend("alert('Disconnected from parent!');");
            return;
        }
        ItsNatDocument parentItsNatDoc = ((ItsNatNode)iframeElem).getItsNatDocument();

        Document doc = itsNatDoc.getDocument();
        Element button = doc.getElementById("buttonId");
        ((EventTarget)button).removeEventListener("click",this,false);
        button.getParentNode().removeChild(button);

        Document parentDoc = parentItsNatDoc.getDocument();
        Element buttonInParent = (Element)parentDoc.importNode(button,true);
        Element contElem = parentDoc.getElementById("iframeParentPutHereId");
        contElem.appendChild(buttonInParent);

        // Notify the parent document
        StringBuilder code = new StringBuilder();
        code.append("if (window.parent == window) alert('NOT SUPPORTED');"); // A algun navegador antiguo no soportado ya le pasaba
        code.append("else window.parent.document.getItsNatDoc().fireUserEvent(null,'update');");
        itsNatDoc.addCodeToSend(code.toString());
    }
}
