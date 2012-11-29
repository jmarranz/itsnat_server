/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.extjsexam;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TabSelectionDecorator implements ItemListener
{
    protected ItsNatFreeComboBox comp;

    public TabSelectionDecorator(ItsNatFreeComboBox comp)
    {
        this.comp = comp;
    }

    public void itemStateChanged(ItemEvent e)
    {
        int state = e.getStateChange();
        int index = comp.indexOf(e.getItem());
        boolean selected = (state == ItemEvent.SELECTED);

        decorateSelection(index,selected);
    }

    public void decorateSelection(int index,boolean selected)
    {
        Element elem = comp.getItsNatListUI().getElementAt(index);

        if (selected)
            elem.setAttribute("class","x-tab-strip-active");
        else
            elem.setAttribute("class","");
    }
}

