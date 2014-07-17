/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBox;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.event.ChangeEvent;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.css.ElementCSSInlineStyle;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLElement;
import test.web.comp.TestCheckBoxBase;

/**
 *
 * @author jmarranz
 */
public abstract class TestFreeCheckBox extends TestCheckBoxBase implements EventListener
{
    protected ElementCSSInlineStyle elemStyle;
    protected ItsNatFreeCheckBox button;

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestFreeCheckBox(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public void init(String id)
    {
        Document doc = itsNatDoc.getDocument();
        HTMLElement elem = (HTMLElement)doc.getElementById(id);
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.button = (ItsNatFreeCheckBox)componentMgr.findItsNatComponent(elem);

        ToggleButtonModel dataModel = new ToggleButtonModel();
        button.setButtonModel(dataModel);

        button.addEventListener("click",this);

        dataModel.addChangeListener(this);

        this.elemStyle = (ElementCSSInlineStyle)elem;
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
