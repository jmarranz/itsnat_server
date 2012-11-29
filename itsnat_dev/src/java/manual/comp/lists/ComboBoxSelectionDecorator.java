/*
 * ComboBoxSelectionDecorator.java
 *
 * Created on 11 de junio de 2007, 18:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.lists;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.itsnat.comp.list.ItsNatComboBox;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ComboBoxSelectionDecorator implements ItemListener
{
    protected ItsNatComboBox comp;

    public ComboBoxSelectionDecorator(ItsNatComboBox comp)
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
        Element elem = comp.getItsNatListUI().getContentElementAt(index);

        if (selected)
            elem.setAttribute("style","background:rgb(0,0,255); color:white;");
        else
            elem.removeAttribute("style");
    }
}
