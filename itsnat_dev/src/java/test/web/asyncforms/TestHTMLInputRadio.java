/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.asyncforms;

import org.itsnat.comp.button.toggle.ItsNatHTMLInputRadio;
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
public class TestHTMLInputRadio extends TestBaseHTMLDocument implements EventListener
{
    protected HTMLInputElement inputRadioElem1;
    protected HTMLInputElement inputRadioElem2;
    protected Element changeRadioElem;

    public TestHTMLInputRadio(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        Document doc = itsNatDoc.getDocument();

        this.inputRadioElem1 = (HTMLInputElement)doc.getElementById("radioId1");
        ((EventTarget)inputRadioElem1).addEventListener("click", this, false);

        this.inputRadioElem2 = (HTMLInputElement)doc.getElementById("radioId2");
        ((EventTarget)inputRadioElem2).addEventListener("click", this, false);

        this.changeRadioElem = doc.getElementById("changeRadioId");
        ((EventTarget)changeRadioElem).addEventListener("click", this, false);
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();
        EventTarget currTarget = evt.getCurrentTarget();
        if ((currTarget == inputRadioElem1) || (currTarget == inputRadioElem2))
        {
            outText(type + ": ");
            outText(inputRadioElem1.getChecked() + " ");
            outText(inputRadioElem2.getChecked() + " ");
        }
        else if (currTarget == changeRadioElem)
        {
            // Nota: en radio buttons si se quiere que haya otro botón
            // seleccionado hay que seleccionarlo explícitamente,
            // la no selección del actualmente seleccionado deja a todos
            // sin seleccionar, lo cual es un estado válido también.
            // Para probar que todas las combinaciones son válidas
            // hacemos la siguiente secuencia: (sel-botón1,sel-botón2)
            // (false,true) => (true,false) => (false,false) => volver

            boolean sel1 = inputRadioElem1.getChecked();
            boolean sel2 = inputRadioElem2.getChecked();
            if (!sel1 && sel2) //  (false,true)
                inputRadioElem1.setChecked(true); // (true,false)
            else if (sel1 && !sel2) // (true,false)
                inputRadioElem1.setChecked(false); // (false,false)
            else if (!sel1 && !sel2) // (false,false)
                inputRadioElem2.setChecked(true); // (false,true)

        }
    }
}
