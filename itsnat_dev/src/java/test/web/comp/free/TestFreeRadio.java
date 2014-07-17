/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import org.itsnat.comp.button.ItsNatButtonGroup;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.event.ChangeEvent;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.css.ElementCSSInlineStyle;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLElement;
import test.web.comp.TestRadioBase;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestFreeRadio extends TestRadioBase implements EventListener
{
    protected ElementCSSInlineStyle elemStyle;
    protected ItsNatFreeRadioButton radioComp1;
    protected ItsNatFreeRadioButton radioComp2;
    protected ItsNatFreeRadioButton radioComp3;

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestFreeRadio(ItsNatHTMLDocument itsNatDoc)
    {
        super(null,itsNatDoc);
    }

    public TestFreeRadio(String idButton,HTMLElement elem,ItsNatHTMLDocument itsNatDoc)
    {
        super(idButton,itsNatDoc);

        this.elemStyle = (ElementCSSInlineStyle)elem;
    }

    public void init(String id)
    {
        Document doc = itsNatDoc.getDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        HTMLElement elem1 = (HTMLElement)doc.getElementById(id + "1");
        this.radioComp1 = (ItsNatFreeRadioButton)componentMgr.findItsNatComponent(elem1);

        HTMLElement elem2 = (HTMLElement)doc.getElementById(id + "2");
        this.radioComp2 = (ItsNatFreeRadioButton)componentMgr.findItsNatComponent(elem2);

        HTMLElement elem3 = (HTMLElement)doc.getElementById(id + "3");
        this.radioComp3 = (ItsNatFreeRadioButton)componentMgr.findItsNatComponent(elem3);

        ToggleButtonModel dataModel1 = new ToggleButtonModel();
        radioComp1.setButtonModel(dataModel1);
        ToggleButtonModel dataModel2 = new ToggleButtonModel();
        radioComp2.setButtonModel(dataModel2);
        ToggleButtonModel dataModel3 = new ToggleButtonModel();
        radioComp3.setButtonModel(dataModel3);

        ButtonGroup group = new ButtonGroup();
        ItsNatButtonGroup htmlGroup = componentMgr.getItsNatButtonGroup(group);
        htmlGroup.addButton(radioComp1);
        htmlGroup.addButton(radioComp2);
        htmlGroup.addButton(radioComp3);

        TestUtil.checkError(htmlGroup == componentMgr.getItsNatButtonGroup(group));
        TestUtil.checkError(htmlGroup == componentMgr.getItsNatButtonGroup(htmlGroup.getName()));

        TestUtil.checkError(radioComp1.getItsNatButtonGroup().getButtonCount() == 3);

        TestFreeRadio listener1 = new TestFreeRadio("1",elem1,itsNatDoc);
        radioComp1.addEventListener("click",listener1);
        dataModel1.addChangeListener(listener1);

        TestFreeRadio listener2 = new TestFreeRadio("2",elem2,itsNatDoc);
        radioComp2.addEventListener("click",listener2);
        dataModel2.addChangeListener(listener2);

        TestFreeRadio listener3 = new TestFreeRadio("3",elem3,itsNatDoc);
        radioComp3.addEventListener("click",listener3);
        dataModel3.addChangeListener(listener3);
    }

    public void handleEvent(Event evt)
    {
        outText("OK " + evt.getType() + " "); // Para que se vea
    }

    public void stateChanged(ChangeEvent e)
    {
        super.stateChanged(e);

        ButtonModel model = (ButtonModel)e.getSource();
        FreeToggleButtonUtil.updateDecoration(model,elemStyle);
    }
}
