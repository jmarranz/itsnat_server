/*
 * OnClickFireEventFromServerTest.java
 *
 * Created on 6 de agosto de 2007, 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.shared;


import java.io.Serializable;
import java.lang.reflect.Method;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatNode;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatUserEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLIFrameElement;

/**
 *
 * @author jmarranz
 */
public class TestIFrameBoundHTML implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected HTMLIFrameElement iframe;

    /** Creates a new instance of TestIFrameInsertion */
    public TestIFrameBoundHTML(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        final Document doc = itsNatDoc.getDocument();
        this.iframe = (HTMLIFrameElement)doc.getElementById("testIFrameBoundId");

        itsNatDoc.addUserEventListener(null,"update",this);

        Element reinsertElem = doc.getElementById("testIFrameBoundReinsertId");

        EventListener reinsListener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                removeButton(); // Por si estuviera arriba de una prueba

                HTMLIFrameElement newIFrame = (HTMLIFrameElement)iframe.cloneNode(true); // El objeto "bind" interno obviamente NO se clonará
                Node parentNode = iframe.getParentNode();
                parentNode.replaceChild(newIFrame, iframe);

                iframe = newIFrame;
            }
        };
        ((EventTarget)reinsertElem).addEventListener("click",reinsListener,false);

        Element changeSrcElem = doc.getElementById("testIFrameBoundChangeSrcId");

        EventListener changeSrcListener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                removeButton(); // Por si estuviera arriba de una prueba

                String src = iframe.getSrc();
                iframe.setSrc("http://www.google.com"); // Hace perder el actual
                iframe.setSrc(src); // Restauramos
            }
        };
        ((EventTarget)changeSrcElem).addEventListener("click",changeSrcListener,false);

        Element checkServerElem = doc.getElementById("testIFrameBoundCheckServerId");

        EventListener checkServerListener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                // Para actualizar el cliente. Util en full remote control
            }
        };
        ((EventTarget)checkServerElem).addEventListener("click",checkServerListener,false);
    }

    public void handleEvent(Event evt)
    {
        if (evt instanceof ItsNatUserEvent) // Button received
        {
            prepareButtonToSend();
        }
        else // Button clicked
        {
            sendToIFrame(evt);
        }
    }

    public void prepareButtonToSend()
    {
        Document doc = itsNatDoc.getDocument();
        Element button = doc.getElementById("buttonId");

        Text text = (Text)button.getFirstChild();
        text.setData("Send to IFrame");

        ((EventTarget)button).addEventListener("click",this,false);
    }

    public void sendToIFrame(Event evt)
    {
        Element button = removeButton();

        Document iframeDoc;
        try
        {
            iframeDoc = iframe.getContentDocument();
        }
        catch(NoSuchMethodError ex) // Cause: Xerces compatibility package of Tomcat 5.5
        {
            try
            {
                Method method = iframe.getClass().getMethod("getContentDocument",(Class[])null); // El cast es para evitar un warnning en Java 1.5+
                iframeDoc = (Document)method.invoke(iframe,null);
            }
            catch(Exception ex2) { throw new RuntimeException(ex2); }
        }

        ItsNatDocument iframeItsNatDoc = ((ItsNatNode)iframeDoc).getItsNatDocument(); // This method is multithread
        if (iframeItsNatDoc == null)
        {
            itsNatDoc.addCodeToSend("alert('Not loaded yet');");
            return;
        }

        synchronized(iframeItsNatDoc) // NEEDED
        {
            Element buttonInParent = (Element)iframeDoc.importNode(button,true);
            Element contElem = iframeDoc.getElementById("testIFrameChildPutHereId");
            contElem.appendChild(buttonInParent);

            // Notify child document
            String ref = itsNatDoc.getScriptUtil().getNodeReference(iframe);
            StringBuffer code = new StringBuffer();
            code.append("var elem = " + ref + ";");
            code.append("var doc = elem.contentDocument ? elem.contentDocument : elem.contentWindow.document;"); // contentWindow in MSIE
            code.append("doc.getItsNatDoc().fireUserEvent(null,'update');");

            ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();
            clientDoc.addCodeToSend(code.toString());
        }
    }

    public Element removeButton()
    {
        Document doc = itsNatDoc.getDocument();
        Element button = doc.getElementById("buttonId");
        if (button == null) return null;
        ((EventTarget)button).removeEventListener("click",this,false);
        button.getParentNode().removeChild(button);
        return button;
    }
}
