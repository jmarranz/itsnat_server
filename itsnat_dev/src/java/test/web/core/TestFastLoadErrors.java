/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.core;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestFastLoadErrors
{
    public TestFastLoadErrors(ItsNatHTMLDocument itsNatDoc)
    {
        if (false) // Poner a true si queremos detectar en modo fast load un uso ilegal, autoCleanEventListener y debugMode deben estar a true
        {
            HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
            Element elem = doc.createElement("div");
            doc.getBody().appendChild(elem);
            EventListener evtListener = new EventListenerSerial()
            {
                public void handleEvent(Event evt)
                {
                }
            };
            ((EventTarget)elem).addEventListener("click", evtListener, false);
            doc.getBody().removeChild(elem); // Produce el error
        }
        if (false) // Idem, esta vez por el cacheado del nodo, debe ser fast load y debugMode debe estar a true
        {
            HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
            Element elem = doc.createElement("div");
            doc.getBody().appendChild(elem);
            String elemRef = itsNatDoc.getScriptUtil().getNodeReference(elem); // Produce el cacheo del nodo
            String code = elemRef + ".style.visibility = 'visible';"; // Por hacer algo
            itsNatDoc.addCodeToSend(code);
            doc.getBody().removeChild(elem);  // Produce el error
        }
    }

}
