/*
 * TestAddRowListener.java
 *
 * Created on 2 de octubre de 2006, 21:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class TestAddRowListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of TestAddRowListener
     */
    public TestAddRowListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element addRowElem = itsNatDoc.getDocument().getElementById("add-row");
        ((EventTarget)addRowElem).addEventListener("click",this,false);
        itsNatDoc.setUserValue("rows",new Integer(0));
    }

    public void handleEvent(Event evt)
    {
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ItsNatDocument itsNatDoc = itsNatEvent.getItsNatDocument();
        int rows = ((Integer)itsNatDoc.getUserValue("rows")).intValue();

        Element elem = (Element)evt.getCurrentTarget();
        Document doc = elem.getOwnerDocument();
        Element pattern = doc.getElementById("row-pattern");
        HTMLElement newRow = (HTMLElement)pattern.cloneNode(true);
        newRow.setAttribute("id","row-" + rows);
        newRow.setAttribute("style","display:block;color:red;");

        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("add-row-value");
        newRow.appendChild(doc.createTextNode(rows + " - " + inputElem.getValue()));
        rows++;
        itsNatDoc.setUserValue("rows",new Integer(rows));

        Element parent = doc.getElementById("rows");
        parent.appendChild(newRow);
    }

}
