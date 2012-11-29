/*
 * ComboBoxSelectionDecorator.java
 *
 * Created on 11 de junio de 2007, 18:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.lists;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.list.ItsNatListMultSel;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ListSelectionDecorator implements ListSelectionListener
{
    protected ItsNatListMultSel comp;

    public ListSelectionDecorator(ItsNatListMultSel comp)
    {
        this.comp = comp;
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        int first = e.getFirstIndex();
        int last = e.getLastIndex();

        ListSelectionModel selModel = (ListSelectionModel)e.getSource();

        for(int i = first; i <= last; i++)
        {
            decorateSelection(i,selModel.isSelectedIndex(i));
        }
    }

    public void decorateSelection(int index,boolean selected)
    {
        Element elem = comp.getItsNatListUI().getContentElementAt(index);
        if (elem == null) return;

        if (selected)
            elem.setAttribute("style","background:rgb(0,0,255); color:white;");
        else
            elem.removeAttribute("style");
    }
}
