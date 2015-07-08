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
import org.w3c.dom.html.HTMLInputElement;
import test.web.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestHTMLInputCheckBox extends TestBaseHTMLDocument implements EventListener
{
    protected HTMLInputElement inputCheckBoxElem;
    protected Element changeCheckBoxElem;

    public TestHTMLInputCheckBox(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        Document doc = itsNatDoc.getDocument();

        this.inputCheckBoxElem = (HTMLInputElement)doc.getElementById("checkBoxId");
        ((EventTarget)inputCheckBoxElem).addEventListener("click", this, false);

        this.changeCheckBoxElem = doc.getElementById("changeCheckBoxId");
        ((EventTarget)changeCheckBoxElem).addEventListener("click", this, false);
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == inputCheckBoxElem)
            outText(type + ": " + inputCheckBoxElem.getChecked() + " ");
        else if (currTarget == changeCheckBoxElem)
            inputCheckBoxElem.setChecked(!inputCheckBoxElem.getChecked());
    }
}
