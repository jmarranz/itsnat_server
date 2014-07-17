/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.comp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.label.ItsNatLabel;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLElement;
import test.web.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public abstract class TestLabelEditorsBase extends TestBaseHTMLDocument implements EventListener,PropertyChangeListener
{
    protected ItsNatHTMLInputCheckBox useSingleClickComp;
    protected ItsNatLabel[] labels;

    public TestLabelEditorsBase(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public void load(String[] ids)
    {
        try
        {
            label1(ids[0]);
            label2(ids[1]);
            label3(ids[2]);
            label4(ids[3]);
            label5(ids[4]);
            label6(ids[5]);
        }
        catch(PropertyVetoException ex)
        {
            throw new RuntimeException(ex);
        }

        ItsNatHTMLComponentManager componentMgr = itsNatDoc.getItsNatHTMLComponentManager();
        this.useSingleClickComp = (ItsNatHTMLInputCheckBox)componentMgr.findItsNatComponentById("testItemEditorSingleClickId");
        useSingleClickComp.addEventListener("click",this);
    }


    public ItsNatLabel getItsNatLabel(String id)
    {
        Document doc = itsNatDoc.getDocument();
        HTMLElement labelElem = (HTMLElement)doc.getElementById(id);
        ItsNatHTMLComponentManager componentMgr = itsNatDoc.getItsNatHTMLComponentManager();
        return (ItsNatLabel)componentMgr.findItsNatComponent(labelElem);
    }

    public ItsNatHTMLComponentManager getComponentMgr()
    {
        return itsNatDoc.getItsNatHTMLComponentManager();
    }

    public void label1(String id) throws PropertyVetoException
    {
        ItsNatLabel label = getItsNatLabel(id);
        label.setValue("Any Text");
        ItsNatHTMLInputText textInput = getComponentMgr().createItsNatHTMLInputText(null,null);
        labelShared(1,label,textInput);
    }

    public void label2(String id) throws PropertyVetoException
    {
        ItsNatLabel label = getItsNatLabel(id);
        label.setValue(Boolean.TRUE);
        ItsNatHTMLInputCheckBox checkBox = getComponentMgr().createItsNatHTMLInputCheckBox(null,null);
        labelShared(2,label,checkBox);
    }

    public void label3(String id) throws PropertyVetoException
    {
        ItsNatLabel label = getItsNatLabel(id);
        label.setValue(new Integer(3));
        ItsNatHTMLSelectComboBox comboBox = itsNatDoc.getItsNatHTMLComponentManager().createItsNatHTMLSelectComboBox(null,null);
        DefaultComboBoxModel model = (DefaultComboBoxModel)comboBox.getComboBoxModel();
        for(int i=0; i < 5; i++) model.addElement(new Integer(i));
        labelShared(3,label,comboBox);
    }

    public void label4(String id) throws PropertyVetoException
    {
        ItsNatLabel label = getItsNatLabel(id);
        label.setValue("Any \n Text");
        ItsNatHTMLTextArea textArea = getComponentMgr().createItsNatHTMLTextArea(null,null);
        labelShared(4,label,textArea);
    }

    public void label5(String id) throws PropertyVetoException
    {
        ItsNatLabel label = getItsNatLabel(id);
        label.setValue(new Date());
        ItsNatHTMLInputTextFormatted textInputFormatted = getComponentMgr().createItsNatHTMLInputTextFormatted(null,null);
        labelShared(5,label,textInputFormatted);
    }

    public void label6(String id) throws PropertyVetoException
    {
        ItsNatLabel label = getItsNatLabel(id);
        label.setValue(new Float(5.4));
        ItsNatHTMLInputTextFormatted textInputFormatted = getComponentMgr().createItsNatHTMLInputTextFormatted(null,null);
        labelShared(6,label,textInputFormatted);
    }

    public void labelShared(int i,ItsNatLabel label,ItsNatHTMLElementComponent compEditor)
    {
        ItsNatHTMLComponentManager componentMgr = itsNatDoc.getItsNatHTMLComponentManager();
        ItsNatLabelEditor editor = componentMgr.createDefaultItsNatLabelEditor(compEditor);
        label.setItsNatLabelEditor(editor);
        label.addEventListener("dblclick",this);
        label.addPropertyChangeListener("value",this);

        labels[i - 1] = label;
    }

    public void handleEvent(Event evt)
    {
        if (evt.getCurrentTarget() == useSingleClickComp.getElement())
        {
            boolean sel = useSingleClickComp.isSelected();
            String eventType = sel? "click" : "dblclick";
            for(int i = 0; i < labels.length; i++)
            {
                labels[i].setEditorActivatorEvent(eventType);
            }
        }
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        outText("OK " + evt.getOldValue() + " " + evt.getNewValue());
    }
}
