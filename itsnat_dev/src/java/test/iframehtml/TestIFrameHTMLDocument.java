/*
 * TestMobileDocument.java
 *
 * Created on 12 de agosto de 2007, 9:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.iframehtml;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatNode;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestIFrameHTMLDocument implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;

    /**
     * Creates a new instance of TestMobileDocument
     */
    public TestIFrameHTMLDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        itsNatDoc.addUserEventListener(null,"update",this);

        Element checkServer = itsNatDoc.getDocument().getElementById("checkServerId");
        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                // Sirve para que actualice el cliente con los cambios en el servidor
            }
        };
        ((EventTarget)checkServer).addEventListener("click", listener,false);

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
            sendToParent(evt);
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

    public void sendToParent(Event evt)
    {
        // No hace falta sincronizar el acceso al documento padre
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
        Element contElem = parentDoc.getElementById("testIFrameParentPutHereId");
        contElem.appendChild(buttonInParent);

        // Notify the parent document
        StringBuilder code = new StringBuilder();
        code.append("if (window.parent == window) alert('NOT SUPPORTED');"); // Pasaba en algún browser antiguo móvil
        code.append("else window.parent.document.getItsNatDoc().fireUserEvent(null,'update');");
        ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();
        clientDoc.addCodeToSend(code.toString());
    }
}
