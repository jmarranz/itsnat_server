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
import org.itsnat.comp.text.ItsNatHTMLInputPassword;
import javax.swing.text.PlainDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class TestHTMLInputPassword extends TestHTMLInputTextBased implements EventListener
{


    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLInputPassword(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initInputPassword();
    }

    public void initInputPassword()
    {
        org.w3c.dom.Document doc = itsNatDoc.getDocument();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("passwordInputId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputPassword input = (ItsNatHTMLInputPassword)componentMgr.findItsNatComponent(inputElem);
        PlainDocument dataModel = new PlainDocument();
        input.setDocument(dataModel);

        input.addEventListener("change",this);

        input.setText("Initial Passwd");

        input.addEventListener("change",this);

        dataModel.addDocumentListener(this);
    }

    public void handleEvent(Event evt)
    {
        handleEvent(evt,true);
    }
}
