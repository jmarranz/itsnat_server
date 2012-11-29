/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.mobile;

import java.io.Serializable;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import test.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestEmptyAnswer implements Serializable
{
    public TestEmptyAnswer(final ItsNatHTMLDocument itsNatDoc)
    {
        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
            }
        };
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Element emptyAnswerElem = doc.getElementById("emptyAnswerId");
        ((EventTarget)emptyAnswerElem).addEventListener("click",listener,false);
        // Este test es para algunos navegadores con WebKit antiguo, 4xx (sin AJAX síncrono)
        // que tienen un error tonto cuando la respuesta es estríctamente vacía.
    }
}
