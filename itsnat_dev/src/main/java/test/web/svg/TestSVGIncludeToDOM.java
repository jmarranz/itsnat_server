/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.svg;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestSVGIncludeToDOM implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element includeFragElem;

    public TestSVGIncludeToDOM(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        Document doc = itsNatDoc.getDocument();
        this.includeFragElem = doc.getElementById("toDOMId");
        ((EventTarget)includeFragElem).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        String code = "<text x=\"25\" y=\"170\" font-family=\"Verdana\" font-size=\"18\" fill=\"green\" >" +
                      "Text Included via toDOM()" +
                      "</text>";
        DocumentFragment fragment = itsNatDoc.toDOM(code);
        Element rootElem = itsNatDoc.getDocument().getDocumentElement();
        rootElem.appendChild(fragment);
    }
}
