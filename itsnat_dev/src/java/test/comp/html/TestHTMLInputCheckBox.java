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
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.core.html.ItsNatHTMLDocument;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class TestHTMLInputCheckBox extends TestCheckBoxBase implements EventListener
{

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLInputCheckBox(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initCheckBox();
    }

    public void initCheckBox()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("checkboxId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputCheckBox input = (ItsNatHTMLInputCheckBox)componentMgr.findItsNatComponent(inputElem);
        ToggleButtonModel dataModel = new ToggleButtonModel();
        input.setButtonModel(dataModel);

        input.addEventListener("click",this);
        // Los demás tipos de eventos ya están testeados con el tipo de botón normal (default)

        dataModel.addChangeListener(this);
    }

    public void handleEvent(Event evt)
    {
        outText("OK " + evt.getType() + " "); // Para que se vea
    }


}
