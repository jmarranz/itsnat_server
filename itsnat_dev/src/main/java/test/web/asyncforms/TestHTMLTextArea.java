/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.asyncforms;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLTextAreaElement;
import test.web.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestHTMLTextArea extends TestBaseHTMLDocument implements EventListener
{
    protected HTMLTextAreaElement textAreaElem;
    protected Element changeTextAreaElem;
    protected Element removeTextAreaElem;

    public TestHTMLTextArea(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        Document doc = itsNatDoc.getDocument();

        this.textAreaElem = (HTMLTextAreaElement)doc.getElementById("textAreaId");
        ((EventTarget)textAreaElem).addEventListener("change", this, false);

        this.changeTextAreaElem = doc.getElementById("changeTextAreaId");
        ((EventTarget)changeTextAreaElem).addEventListener("click", this, false);

        this.removeTextAreaElem = doc.getElementById("removeTextAreaId");
        ((EventTarget)removeTextAreaElem).addEventListener("click", this, false);
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == textAreaElem)
            outText(type + ": " + textAreaElem.getValue() + " ");
        else if (currTarget == changeTextAreaElem)
            textAreaElem.setValue("Value \nFrom \nServer");
        else if (currTarget == removeTextAreaElem)
            textAreaElem.removeAttribute("value");
    }
}
