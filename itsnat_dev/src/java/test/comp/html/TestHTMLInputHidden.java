/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.html;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.text.ItsNatHTMLInputHidden;
import javax.swing.text.PlainDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class TestHTMLInputHidden extends TestHTMLInputTextBased implements EventListener
{

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLInputHidden(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initHidden();
    }

    public void initHidden()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("hiddenInputId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputHidden input = (ItsNatHTMLInputHidden)componentMgr.findItsNatComponent(inputElem);

        PlainDocument dataModel = new PlainDocument();
        input.setDocument(dataModel);

        input.setText("Initial Password");

        input.addEventListener("change",this); // No se llama nunca, para comprobarlo

        dataModel.addDocumentListener(this);
    }

    public void handleEvent(Event evt)
    {
        handleEvent(evt,true);
    }
}
