/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.asyncforms;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;
import test.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestHTMLInputText extends TestBaseHTMLDocument implements EventListener
{
    protected HTMLInputElement inputTextElem;
    protected Element changeInputTextElem;
    protected Element removeInputTextElem;

    public TestHTMLInputText(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        Document doc = itsNatDoc.getDocument();

        this.inputTextElem = (HTMLInputElement)doc.getElementById("inputTextId");
        ((EventTarget)inputTextElem).addEventListener("change", this, false);

        this.changeInputTextElem = doc.getElementById("changeInputTextId");
        ((EventTarget)changeInputTextElem).addEventListener("click", this, false);

        this.removeInputTextElem = doc.getElementById("removeInputTextId");
        ((EventTarget)removeInputTextElem).addEventListener("click", this, false);
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == inputTextElem)
            outText(type + ": " + inputTextElem.getValue() + " ");
        else if (currTarget == changeInputTextElem)
            inputTextElem.setValue("Value From Server");
        else if (currTarget == removeInputTextElem)
            inputTextElem.removeAttribute("value");
    }
}
