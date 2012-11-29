/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.html;

import test.comp.*;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit;
import javax.swing.DefaultButtonModel;
import javax.swing.event.ChangeListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.CommMode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class TestHTMLInputSubmit extends TestButtonBase implements EventListener,ChangeListener
{

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLInputSubmit(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initButton();
    }

    public void initButton()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("submitButtonId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputSubmit input = (ItsNatHTMLInputSubmit)componentMgr.findItsNatComponent(inputElem);
        DefaultButtonModel dataModel = new DefaultButtonModel();
        input.setButtonModel(dataModel);

        input.setLabelValue("Submit Button");

        input.setEventListenerParams("click",false,CommMode.XHR_SYNC,null,null,-1); // Es necesario que sea síncrono pues sino no funciona la llamada preventDefault()
        input.addEventListener("click",this);

        dataModel.addChangeListener(this);
        dataModel.addActionListener(this);
    }

    public void handleEvent(Event evt)
    {
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;

        if (itsNatEvent.getCommMode() != CommMode.XHR_SYNC)
            throw new RuntimeException("This browser doesn't support AJAX syncronous");

        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputSubmit input = (ItsNatHTMLInputSubmit)componentMgr.findItsNatComponent((Node)evt.getCurrentTarget());
        outText("OK " + evt.getType() + " (canceled) "); // Para que se vea

        input.setLabelValue("Submit Button (canceled)");

        evt.preventDefault(); // Cancelamos el envío del formulario
    }


}
