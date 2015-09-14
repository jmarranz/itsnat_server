/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import javax.swing.text.PlainDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;
import test.web.shared.BrowserUtil2;


/**
 *
 * @author jmarranz
 */
public class TestHTMLInputText extends TestHTMLInputTextBased implements EventListener
{
    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLInputText(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        super(itsNatDoc);

        initInputText(request);
    }

    public void initInputText(ItsNatServletRequest request)
    {
        org.w3c.dom.Document doc = itsNatDoc.getDocument();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("textFieldId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputText input = (ItsNatHTMLInputText)componentMgr.findItsNatComponent(inputElem);
        PlainDocument dataModel = new PlainDocument();
        input.setDocument(dataModel);

        input.setText("Initial Text");

        input.addEventListener("change",this);
        input.addEventListener("keyup",this);
        input.addEventListener("keypress", this);
        
        dataModel.addDocumentListener(this);
    }

    public void handleEvent(Event evt)
    {
        boolean updateAgainToTest = true;
        String type = evt.getType();
        if (type.equals("change"))
            updateAgainToTest = true;
        else
            updateAgainToTest = false;

        handleEvent(evt,updateAgainToTest);
    }
}
