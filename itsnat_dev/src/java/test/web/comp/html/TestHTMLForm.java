/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.ItsNatHTMLForm;
import org.itsnat.core.CommMode;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLFormElement;
import test.web.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestHTMLForm extends TestBaseHTMLDocument implements EventListener
{
    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLForm(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initForm();
    }

    public void initForm()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLFormElement formElem = (HTMLFormElement)doc.getElementById("formId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLForm form = (ItsNatHTMLForm)componentMgr.findItsNatComponent(formElem);

        form.setEventListenerParams("submit",false,CommMode.XHR_SYNC,null,null,-1); // Es necesario que sea síncrono pues sino no funciona la llamada preventDefault()
        form.setEventListenerParams("reset",false,CommMode.XHR_SYNC,null,null,-1); // Es necesario que sea síncrono pues sino no funciona la llamada preventDefault()

        form.addEventListener("submit",this);
        form.addEventListener("reset",this);
    }

    public void handleEvent(Event evt)
    {
        if (evt.getTarget() instanceof Document)
        {
            // Es el caso del reset al formulario de test que hacemos con JavaScript en el onload (ej. myform.reset())
            // para evitar el llenado automático que hace el Firefox
            // ante un reload con los datos de la página anterior,
            // que hace que tras la carga el formulario no contenga los mismos datos que hay en el servidor.
            // http://weblogs.mozillazine.org/gerv/archives/2006/10/firefox_reload_behaviour.html

            // Este problema no existe con MSIE 6
            // La propiedad target representa el nodo que "disparó" el evento, al parece Firefox
            // considera que es el Document y no el form (ciertamente no se usa el nodo form)            //

            // Firefox al enviar el evento pone como target el Document
            // con el MSIE no se pasa por aquí, pues el target es el form
            // por tanto aunque en MSIE 6 se cancele el reset da igual pues en este navegador no hay autollenado
            // Nos interesa en Firefox por tanto que se haga el reset, no cancelamos
            return;
        }

        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;

        if (itsNatEvent.getCommMode() != CommMode.XHR_SYNC)
            throw new RuntimeException("This browser doesn't support AJAX syncronous");
        

        outText("OK " + evt.getType() + " (canceled) "); // Para que se vea

        evt.preventDefault(); // Cancelamos el envío del formulario o el reset
    }

}
