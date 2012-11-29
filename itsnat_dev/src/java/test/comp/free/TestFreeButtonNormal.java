/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.free;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormal;
import javax.swing.DefaultButtonModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLElement;
import test.comp.*;

/**
 *
 * @author jmarranz
 */
public class TestFreeButtonNormal extends TestButtonBase implements EventListener
{
    protected ItsNatFreeButtonNormal button;

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestFreeButtonNormal(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public void init(String id)
    {
        Document doc = itsNatDoc.getDocument();
        HTMLElement elem = (HTMLElement)doc.getElementById(id);
            ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.button = (ItsNatFreeButtonNormal)componentMgr.findItsNatComponent(elem);

        DefaultButtonModel dataModel = new DefaultButtonModel();
        button.setButtonModel(dataModel);

        button.addEventListener("click",this);

        dataModel.addChangeListener(this);
        dataModel.addActionListener(this);
    }

    public void handleEvent(Event evt)
    {
        outText("OK " + evt.getType() + " "); // Para que se vea
    }

}
