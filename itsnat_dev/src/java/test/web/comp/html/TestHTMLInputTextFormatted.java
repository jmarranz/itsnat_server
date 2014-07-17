/*
 * TestHTMLInputTextFormatted.java
 *
 * Created on 23 de enero de 2007, 20:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.core.html.ItsNatHTMLDocument;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Date;
import javax.swing.text.PlainDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class TestHTMLInputTextFormatted extends TestHTMLInputTextBased implements EventListener,PropertyChangeListener
{

    /** Creates a new instance of TestHTMLInputTextFormatted */
    public TestHTMLInputTextFormatted(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        try
        {
        initInputText();
        }
        catch(PropertyVetoException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void initInputText() throws PropertyVetoException
    {
        org.w3c.dom.Document doc = itsNatDoc.getDocument();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("textFormattedFieldId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputTextFormatted input = (ItsNatHTMLInputTextFormatted)componentMgr.findItsNatComponent(inputElem);

        PlainDocument dataModel = new PlainDocument();
        input.setDocument(dataModel);

        input.setValue(new Date());

        input.addEventListener("change",this);

        input.addPropertyChangeListener("value",this);

        dataModel.addDocumentListener(this);
    }

    public void handleEvent(Event evt)
    {
        handleEvent(evt,false);
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        Date value = (Date)evt.getNewValue();

        outText("OK value changed to: " + value + " ");
    }
}
