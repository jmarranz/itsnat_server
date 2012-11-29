/*
 * TestFastLoadMode.java
 *
 * Created on 30 de mayo de 2007, 13:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLTableElement;
import test.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestFastLoadMode
{

    /** Creates a new instance of TestFastLoadMode */
    public TestFastLoadMode(ItsNatHTMLDocument itsNatDoc)
    {
        // No funciona en fast mode porque al eliminar al final el <table>
        // no existe el nodo

        if (itsNatDoc.getItsNatDocumentTemplate().isFastLoadMode())
            return;

        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        HTMLTableElement tableElem = (HTMLTableElement)doc.createElement("table");
        doc.getBody().appendChild(tableElem);

        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt) { }
        };

        ((EventTarget)tableElem).addEventListener("click",listener,false);

        ((EventTarget)tableElem).removeEventListener("click",listener,false);

        doc.getBody().removeChild(tableElem);
    }

}
