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

import java.lang.reflect.Method;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatNode;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatUserEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLIFrameElement;

public class IFrameAutoBindingParent implements EventListener
{
    protected HTMLIFrameElement iframe;

    public IFrameAutoBindingParent()
    {
    }

    public void load()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        this.iframe = (HTMLIFrameElement)doc.getElementById("iframeBoundId");

        itsNatDoc.addUserEventListener(null,"update",this);
    }

    public void handleEvent(Event evt)
    {
        ItsNatDocument itsNatDoc = ((ItsNatEvent)evt).getItsNatDocument();
        if (evt instanceof ItsNatUserEvent) // Button received
        {
            prepareButtonToSend(itsNatDoc);
        }
        else // Button clicked
        {
            sendToIFrame(itsNatDoc);
        }
    }

    public void prepareButtonToSend(ItsNatDocument itsNatDoc)
    {
        Document doc = itsNatDoc.getDocument();
        Element button = doc.getElementById("buttonId");

        Text text = (Text)button.getFirstChild();
        text.setData("Send to IFrame");

        ((EventTarget)button).addEventListener("click",this,false);
    }

    public void sendToIFrame(ItsNatDocument itsNatDoc)
    {
        Element button = removeButton(itsNatDoc);

        Document iframeDoc;
        try
        {
            iframeDoc = iframe.getContentDocument();
        }
        catch(NoSuchMethodError ex)
        {
            // Cause: Xerces compatibility package of Tomcat 5.5
            // misses this standard DOM method in HTMLIFrameElement interface
            // Don't worry, our required method is there.
            try
            {
                Method method = iframe.getClass().getMethod("getContentDocument",null);
                iframeDoc = (Document)method.invoke(iframe,(Object[])null);
            }
            catch(Exception ex2) { throw new RuntimeException(ex2); }
        }

        if (iframeDoc == null)
        {
            itsNatDoc.addCodeToSend("alert('Not loaded yet');");
            return;
        }
        ItsNatDocument iframeItsNatDoc = ((ItsNatNode)iframeDoc).getItsNatDocument(); // This method is multithread

        synchronized(iframeItsNatDoc) // NEEDED!!!
        {
            Element buttonInParent = (Element)iframeDoc.importNode(button,true);
            Element contElem = iframeDoc.getElementById("iframeChildPutHereId");
            contElem.appendChild(buttonInParent);

            // Notify child document
            String ref = itsNatDoc.getScriptUtil().getNodeReference(iframe);
            StringBuffer code = new StringBuffer();
            code.append("var elem = " + ref + ";");
            code.append("var doc = (typeof elem.contentDocument != \"undefined\") ? elem.contentDocument : elem.contentWindow.document;"); // contentWindow in MSIE
            code.append("doc.getItsNatDoc().fireUserEvent(null,'update');");
            itsNatDoc.addCodeToSend(code.toString());
        }
    }

    public Element removeButton(ItsNatDocument itsNatDoc)
    {
        Document doc = itsNatDoc.getDocument();
        Element button = doc.getElementById("buttonId");
        // if (button == null) return null;
        ((EventTarget)button).removeEventListener("click",this,false);
        button.getParentNode().removeChild(button);
        return button;
    }
}
