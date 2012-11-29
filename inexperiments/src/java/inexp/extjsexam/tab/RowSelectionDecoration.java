/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.extjsexam.tab;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.table.ItsNatFreeTable;
import org.itsnat.comp.table.ItsNatTableUI;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */

public class RowSelectionDecoration implements ListSelectionListener
{
    protected ItsNatFreeTable comp;
    protected TabContainingTable tab;

    public RowSelectionDecoration(ItsNatFreeTable comp,TabContainingTable tab)
    {
        this.comp = comp;
        this.tab = tab;
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

        // table/tbody/tr/td/em/button
        Element remItemElem = tab.getRemoveItemElement(); // button
        Element table = (Element)remItemElem.getParentNode().getParentNode().getParentNode().getParentNode();
        String cssClass = table.getAttribute("class");

        if (comp.getRowSelectionModel().isSelectionEmpty())
        {
            if (cssClass.indexOf("x-item-disabled") == -1)
            {
                cssClass = cssClass + " x-item-disabled";
                table.setAttribute("class",cssClass);
                remItemElem.setAttribute("disabled","disabled");
            }
        }
        else
        {
            if (cssClass.indexOf("x-item-disabled") != -1)
            {
                cssClass = cssClass.replaceFirst(" x-item-disabled", "");
                table.setAttribute("class",cssClass);
                remItemElem.removeAttribute("disabled");
            }
        }
    }

    public void decorateSelection(int row,boolean selected)
    {
        ItsNatTableUI tableUI = comp.getItsNatTableUI();
        Element divElem = tableUI.getRowElementAt(row);
        if (divElem == null) return;
        String cssClass = divElem.getAttribute("class");

        if (selected)
            cssClass = cssClass + " x-grid3-row-selected";
        else
            cssClass = cssClass.replaceFirst(" x-grid3-row-selected", "");

        divElem.setAttribute("class",cssClass);
    }

}
