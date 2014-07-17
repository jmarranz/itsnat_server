/*
 * TestInputTextFormatted.java
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
import java.beans.VetoableChangeListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.text.PlainDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatFormattedTextField.ItsNatFormatter;
import org.itsnat.comp.text.ItsNatFormatterFactoryDefault;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class TestHTMLInputTextFormattedWithFactory extends TestHTMLInputTextBased implements EventListener,PropertyChangeListener,VetoableChangeListener
{

    /** Creates a new instance of TestInputTextFormatted */
    public TestHTMLInputTextFormattedWithFactory(ItsNatHTMLDocument itsNatDoc)
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
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("textFormattedWithFactoryFieldId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputTextFormatted inputComp = (ItsNatHTMLInputTextFormatted)componentMgr.findItsNatComponent(inputElem);

        ItsNatFormatterFactoryDefault factory = (ItsNatFormatterFactoryDefault)inputComp.createDefaultItsNatFormatterFactory();

        ItsNatFormatter dispFormatter = inputComp.createItsNatFormatter(DateFormat.getDateInstance(DateFormat.LONG ,Locale.US));
        factory.setDisplayFormatter(dispFormatter);
        ItsNatFormatter editFormatter = inputComp.createItsNatFormatter(DateFormat.getDateInstance(DateFormat.SHORT,Locale.US));
        factory.setEditFormatter(editFormatter);

        inputComp.setItsNatFormatterFactory(factory);

        try{ inputComp.setValue(new Date()); } catch(PropertyVetoException ex) { throw new RuntimeException(ex); }

        inputComp.addPropertyChangeListener("value",this);

        inputComp.addVetoableChangeListener(this);

        inputComp.addEventListener("change",this);

        PlainDocument dataModel = (PlainDocument)inputComp.getDocument();
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

    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException
    {
        Date newDate = (Date)evt.getNewValue();
        if (newDate.compareTo(new Date()) > 0)
            throw new PropertyVetoException("Future date is not allowed",evt);
    }
}
