/*
 * TestFreeComboBox.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import org.itsnat.comp.list.ItsNatComboBox;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import java.awt.event.ItemEvent;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLElement;
import test.web.comp.TestComboBoxBase;
import test.web.comp.TestSelectionUtil;

/**
 *
 * @author jmarranz
 */
public class TestFreeComboBox extends TestComboBoxBase
{
    protected ItsNatFreeComboBox combo;

    /**
     * Creates a new instance of TestFreeComboBox
     */
    public TestFreeComboBox(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initCombo();
    }

    public ItsNatComboBox getItsNatComboBox()
    {
        return combo;
    }

    public void initCombo()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLElement parentElem = (HTMLElement)doc.getElementById("freeComboId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.combo = (ItsNatFreeComboBox)componentMgr.findItsNatComponent(parentElem);

        combo.addEventListener("click",this);

        super.initCombo();

        decorateSelection(combo.getSelectedIndex(),true);
    }

    public void itemStateChanged(ItemEvent e)
    {
        super.itemStateChanged(e);

        Object itemValue = e.getItem();
        int index = combo.indexOf(itemValue);
        int status = e.getStateChange();
        decorateSelection(index,status == ItemEvent.SELECTED);
    }

    public void decorateSelection(int index,boolean selected)
    {
        Element option = combo.getItsNatListUI().getElementAt(index);
        TestSelectionUtil.decorateSelection(option,selected);
    }
}
