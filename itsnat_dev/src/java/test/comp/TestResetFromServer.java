/*
 * TestBlurFocusSelectClick.java
 *
 * Created on 16 de noviembre de 2006, 13:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.ItsNatHTMLForm;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.core.CommMode;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;
import test.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestResetFromServer extends TestBaseHTMLDocument implements EventListener
{
    /** Creates a new instance of TestBlurFocusSelectClick */
    public TestResetFromServer(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    public void load()
    {
        org.w3c.dom.Document doc = itsNatDoc.getDocument();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("resetFromServerId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputButton input = (ItsNatHTMLInputButton)componentMgr.findItsNatComponent(inputElem);

        // Es necesario que el evento click sea síncrono pues se hace un reset en el retorno
        // que es anulado "sincronamente" (preventDefault) por el evento submit en otro lugar
        input.setEventListenerParams("click", true,CommMode.XHR_SYNC, null, null, -1);
        input.addEventListener("click",this);
    }

    public void handleEvent(Event evt)
    {
        // Evento "click"
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputButton input = (ItsNatHTMLInputButton)componentMgr.findItsNatComponent((Node)evt.getCurrentTarget());
        outText("OK " + evt.getType() + " "); // Para que se vea

        HTMLFormElement formElem = input.getHTMLInputElement().getForm();

        ItsNatHTMLForm form = (ItsNatHTMLForm)componentMgr.findItsNatComponent(formElem);
        form.reset();
        // Por otro lado hay un listener del form que cancela el reset, para
        // saber que la llamada se ha realizado en el cliente es precisamente
        // ver si sale el mensaje "reset"
    }

}
