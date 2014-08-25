/*
 * TestComboBoxBase.java
 *
 * Created on 3 de diciembre de 2006, 19:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp;

import org.itsnat.comp.list.ItsNatComboBox;
import org.itsnat.core.html.ItsNatHTMLDocument;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import test.web.shared.TestBaseHTMLDocument;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public abstract class TestComboBoxBase extends TestBaseHTMLDocument implements EventListener,ListDataListener,ItemListener
{
    /**
     * Creates a new instance of TestComboBoxBase
     */
    public TestComboBoxBase(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public abstract ItsNatComboBox getItsNatComboBox();

    public void initCombo()
    {
        ItsNatComboBox combo = getItsNatComboBox();
        DefaultComboBoxModel dataModel = new DefaultComboBoxModel();
        combo.setComboBoxModel(dataModel);

        dataModel.addElement("One");
        dataModel.addElement("Two");
        dataModel.addElement("Three");
        dataModel.addElement("Five");
        dataModel.addElement("BAD");
        dataModel.removeElement("BAD");
        dataModel.insertElementAt("Four",3);

        dataModel.setSelectedItem("Three");
        combo.setSelectedIndex(4); // Five

        dataModel.insertElementAt("Zero",0); // Para testear el desplazamiendo del seleccionado
        int sel = combo.getSelectedIndex();
        TestUtil.checkError(sel == 5);

        dataModel.addListDataListener(this);
        combo.addItemListener(this);
    }

    public void handleEvent(Event evt)
    {
        ItsNatComboBox combo = getItsNatComboBox();
        int selected = combo.getSelectedIndex();

        outText("OK " + evt.getType() + "(sel. " + selected + ") "); // Para que se vea

        MutableComboBoxModel model = (MutableComboBoxModel)combo.getComboBoxModel();
        int size = model.getSize();
        model.insertElementAt(new Integer(size),0); // Para ver si se manifiesta en el navegador y genera un evento Swing ListDataEvent
    }


    public void intervalAdded(ListDataEvent e)
    {
        int index0 = e.getIndex0();
        int index1 = e.getIndex1();

        outText("OK added " + index0 + " " + index1 + " "); // Para que se vea en el navegador
    }

    public void intervalRemoved(ListDataEvent e)
    {
    }

    public void contentsChanged(ListDataEvent e)
    {
    }

    public void itemStateChanged(ItemEvent e)
    {
        Object itemValue = e.getItem();

        ItsNatComboBox combo = getItsNatComboBox();
        int index = combo.indexOf(itemValue);
        int status = e.getStateChange();
        //HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        String msg = "ERROR ";
        if (status == ItemEvent.DESELECTED)
            msg = "OK deselected " + index + " ";
        else if (status == ItemEvent.SELECTED)
            msg = "OK selected " + index + " ";

        outText(msg);
    }
}
