/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.extjsexam;

import inexp.extjsexam.tab.TabBase;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.itsnat.comp.list.ItsNatComboBox;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TabSelectionSwitcher implements ItemListener
{
    protected ExtJSExampleDocument extJSDoc;

    public TabSelectionSwitcher(ExtJSExampleDocument extJSDoc)
    {
        this.extJSDoc = extJSDoc;
    }

    public ItsNatComboBox getTabComboBox()
    {
        return extJSDoc.getTabComboBox();
    }

    public void itemStateChanged(ItemEvent e)
    {
        TabBase tab = (TabBase)e.getItem();
        Element tabPanelElem = extJSDoc.getTabPanelElement();
        int state = e.getStateChange();
        boolean selected = (state == ItemEvent.SELECTED);
        if (selected)
        {
            DocumentFragment frag = extJSDoc.loadDocumentFragment(tab.getFragmentName());
            tabPanelElem.appendChild(frag);
            tab.render();
        }
        else
        {
            tab.remove();
            ItsNatDOMUtil.removeAllChildren(tabPanelElem);
        }
    }
}

