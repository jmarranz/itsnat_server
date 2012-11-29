/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.html;

import test.comp.*;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.core.html.ItsNatHTMLDocument;
import javax.swing.DefaultButtonModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLButtonElement;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public abstract class TestHTMLButton extends TestButtonBase implements EventListener
{
    protected ItsNatHTMLButton button;

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLButton(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public void init(String id)
    {
        Document doc = itsNatDoc.getDocument();
        HTMLButtonElement elem = (HTMLButtonElement)doc.getElementById(id);
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.button = (ItsNatHTMLButton)componentMgr.findItsNatComponent(elem);
        DefaultButtonModel dataModel = new DefaultButtonModel();
        button.setButtonModel(dataModel);

        button.setEnabled(false); // Para testear la propagación al DOM (getDisabled)
        TestUtil.checkError(elem.getDisabled());
        button.setEnabled(true);

        button.addEventListener("click",this);

        dataModel.addChangeListener(this);
        dataModel.addActionListener(this);
    }

    public void handleEvent(Event evt)
    {
        outText("OK " + evt.getType() + " "); // Para que se vea
    }

}
