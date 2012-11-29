/*
 * TestHTMLSelectComboBoxBase.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.html;

import test.comp.*;
import org.itsnat.comp.list.ItsNatComboBox;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLSelectElement;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public abstract class TestHTMLSelectComboBoxBase extends TestComboBoxBase
{
    protected ItsNatHTMLSelectComboBox combo;

    /**
     * Creates a new instance of TestHTMLSelectComboBoxBase
     */
    public TestHTMLSelectComboBoxBase(String id,ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initCombo(id);
    }

    public ItsNatComboBox getItsNatComboBox()
    {
        return combo;
    }

    public void initCombo(String id)
    {
        Document doc = itsNatDoc.getDocument();
        HTMLSelectElement selectElem = (HTMLSelectElement)doc.getElementById(id);
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.combo = (ItsNatHTMLSelectComboBox)componentMgr.findItsNatComponent(selectElem);
        combo.addEventListener("change",this);

        super.initCombo();
    }

    public void handleEvent(Event evt)
    {
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLSelectComboBox combo = (ItsNatHTMLSelectComboBox)componentMgr.findItsNatComponent((Node)evt.getCurrentTarget());
        int selected = Integer.parseInt((String)itsNatEvent.getExtraParam("selectedIndex"));
        TestUtil.checkError(selected == combo.getSelectedIndex());

        super.handleEvent(evt);
    }

}
