/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.svg;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import javax.swing.DefaultComboBoxModel;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class TestSVGXHTMLComponent implements EventListener,ItemListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected ItsNatHTMLSelectComboBox selComp;
    protected ItsNatHTMLInputText inputComp;
    protected ItsNatHTMLInputButton buttonComp;

    public TestSVGXHTMLComponent(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        ItsNatHTMLComponentManager compMgr =
                (ItsNatHTMLComponentManager)itsNatDoc.getItsNatComponentManager();

        Document doc = itsNatDoc.getDocument();
        HTMLSelectElement select = (HTMLSelectElement)doc.getElementById("htmlSelectId");
        this.selComp = compMgr.createItsNatHTMLSelectComboBox(select,null);
        DefaultComboBoxModel dataModel = (DefaultComboBoxModel)selComp.getComboBoxModel();
        dataModel.addElement("One");
        dataModel.addElement("Two");
        dataModel.addElement("Three");
        selComp.addItemListener(this);

        HTMLInputElement input = (HTMLInputElement)doc.getElementById("htmlInputId");
        inputComp = compMgr.createItsNatHTMLInputText(input,null);

        HTMLInputElement button = (HTMLInputElement)doc.getElementById("htmlButtonId");
        buttonComp = compMgr.createItsNatHTMLInputButton(button,null);
        buttonComp.addEventListener("click",this);
    }

    public void itemStateChanged(ItemEvent e)
    {
         int state = e.getStateChange();
         if (state == ItemEvent.SELECTED)
             inputComp.setText((String)selComp.getSelectedItem());
    }

    public void handleEvent(Event evt)
    {
        // pressed buttonComp
        String value = inputComp.getText();
        if (value.equals(""))
        {
            itsNatDoc.addCodeToSend("alert('Empty');");
            return;
        }
        DefaultComboBoxModel dataModel = (DefaultComboBoxModel)selComp.getComboBoxModel();
        dataModel.addElement(value);
    }
}
