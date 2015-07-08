/*
 * TableRowSelectionDecoration.java
 *
 * Created on 13 de junio de 2007, 18:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.tables;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableUI;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TableRowSelectionDecoration implements ListSelectionListener
{
    protected ItsNatTable comp;

    public TableRowSelectionDecoration(ItsNatTable comp)
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

    public void decorateSelection(int row,boolean selected)
    {
        ItsNatTableUI tableUI = comp.getItsNatTableUI();
        int cols = comp.getTableModel().getColumnCount();
        for(int i = 0; i < cols; i++)
        {
            Element cellElem = tableUI.getCellContentElementAt(row,i);
            if (cellElem == null) return;
            decorateSelection(cellElem,selected);
        }
    }

    public void decorateSelection(Element elem,boolean selected)
    {
        if (selected)
            elem.setAttribute("style","background:rgb(0,0,255); color:white;");
        else
            elem.removeAttribute("style");
    }
}
