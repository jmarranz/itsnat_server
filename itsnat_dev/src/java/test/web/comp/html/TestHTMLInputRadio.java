/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.comp.button.ItsNatButtonGroup;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputRadio;
import org.itsnat.core.html.ItsNatHTMLDocument;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;
import test.web.shared.TestUtil;
import test.web.comp.TestRadioBase;

/**
 *
 * @author jmarranz
 */
public class TestHTMLInputRadio extends TestRadioBase implements EventListener
{
    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLInputRadio(ItsNatHTMLDocument itsNatDoc)
    {
        super(null,itsNatDoc);

        initRadioButtons();
    }

    public TestHTMLInputRadio(String idButton,ItsNatHTMLDocument itsNatDoc)
    {
        super(idButton,itsNatDoc);
    }

    public void initRadioButtons()
    {
        Document doc = itsNatDoc.getDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        HTMLInputElement inputElem1 = (HTMLInputElement)doc.getElementById("radioId1");
        ItsNatHTMLInputRadio input1 = (ItsNatHTMLInputRadio)componentMgr.findItsNatComponent(inputElem1);
        HTMLInputElement inputElem2 = (HTMLInputElement)doc.getElementById("radioId2");
        ItsNatHTMLInputRadio input2 = (ItsNatHTMLInputRadio)componentMgr.findItsNatComponent(inputElem2);
        HTMLInputElement inputElem3 = (HTMLInputElement)doc.getElementById("radioId3");
        ItsNatHTMLInputRadio input3 = (ItsNatHTMLInputRadio)componentMgr.findItsNatComponent(inputElem3);

        // Chequeamos que forman ya grupo a través del atributo name
        ItsNatButtonGroup itsNatGrp = input1.getItsNatButtonGroup();
        ButtonGroup group = itsNatGrp.getButtonGroup();
        TestUtil.checkError(input2.getItsNatButtonGroup() == itsNatGrp);
        TestUtil.checkError(input3.getItsNatButtonGroup() == itsNatGrp);
        TestUtil.checkError(input2.getItsNatButtonGroup().getButtonGroup() == group);
        TestUtil.checkError(input3.getItsNatButtonGroup().getButtonGroup() == group);

        TestUtil.checkError(input1.getToggleButtonModel().getGroup() == group);
        TestUtil.checkError(input2.getToggleButtonModel().getGroup() == group);
        TestUtil.checkError(input3.getToggleButtonModel().getGroup() == group);

        ToggleButtonModel dataModel1 = new ToggleButtonModel();
        input1.setButtonModel(dataModel1);
        ToggleButtonModel dataModel2 = new ToggleButtonModel();
        input2.setButtonModel(dataModel2);
        ToggleButtonModel dataModel3 = new ToggleButtonModel();
        input3.setButtonModel(dataModel3);

        group = new ButtonGroup();
        ItsNatButtonGroup htmlGroup = componentMgr.getItsNatButtonGroup(group);
        htmlGroup.addButton(input1);
        htmlGroup.addButton(input2);
        htmlGroup.addButton(input3);

        TestUtil.checkError(htmlGroup == componentMgr.getItsNatButtonGroup(group));
        TestUtil.checkError(htmlGroup == componentMgr.getItsNatButtonGroup(htmlGroup.getName()));

        TestUtil.checkError(input1.getItsNatButtonGroup().getButtonCount() == 3);

        TestHTMLInputRadio listener1 = new TestHTMLInputRadio("1",itsNatDoc);
        input1.addEventListener("click",listener1);
        dataModel1.addChangeListener(listener1);

        TestHTMLInputRadio listener2 = new TestHTMLInputRadio("2",itsNatDoc);
        input2.addEventListener("click",listener2);
        dataModel2.addChangeListener(listener2);

        TestHTMLInputRadio listener3 = new TestHTMLInputRadio("3",itsNatDoc);
        input3.addEventListener("click",listener3);
        dataModel3.addChangeListener(listener3);
    }

    public void handleEvent(Event evt)
    {
        outText("OK " + evt.getType() + " "); // Para que se vea
    }
}
