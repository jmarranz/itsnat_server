/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.core;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestInputFileInsertion implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;

    public TestInputFileInsertion(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        ((EventTarget)view).addEventListener("load",this,false);
    }

    public void handleEvent(Event evt)
    {
        // Este test es especialmente interesante en BlackBerry
        HTMLDocument doc = itsNatDoc.getHTMLDocument();

        final HTMLInputElement input1 = (HTMLInputElement)doc.createElement("input");
        input1.setAttribute("type","file");
        input1.setAttribute("value","HOLA");
        doc.getBody().appendChild(input1); // Se usarán métodos DOM
        input1.setAttribute("value","ADIOS");

        final Element div = doc.createElement("div");
        doc.getBody().appendChild(div);

        HTMLInputElement input2 = (HTMLInputElement)doc.createElement("input");
        input2.setAttribute("type","file");
        input2.setAttribute("value","Test TestInputFileInsertion");
        div.appendChild(input2); // Seguramente se usará innerHTML

        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                // Limpiamos
                input1.getParentNode().removeChild(input1);
                div.getParentNode().removeChild(div);
            }
        };

        ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();
        clientDoc.addContinueEventListener((EventTarget)doc, listener);
    }


}
