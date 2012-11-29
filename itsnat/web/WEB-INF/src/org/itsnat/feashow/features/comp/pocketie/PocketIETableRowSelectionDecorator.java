/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.comp.pocketie;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableUI;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;

public class PocketIETableRowSelectionDecorator implements ListSelectionListener
{
    protected ItsNatTable comp;

    public PocketIETableRowSelectionDecorator(ItsNatTable comp)
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
            decorateSelection(i,selModel.isSelectedIndex(i));
    }

    public void decorateSelection(int row,boolean selected)
    {
        ItsNatTableUI tableUI = comp.getItsNatTableUI();
        HTMLTableRowElement rowElem = (HTMLTableRowElement)tableUI.getRowElementAt(row);
        if (rowElem == null) return;

        if (selected)
            setAttribute(rowElem,"style","background:rgb(0,0,255);");
        else
            rowElem.removeAttribute("style");

        // Free Table
        int cols = comp.getTableModel().getColumnCount();
        for(int i = 0; i < cols; i++)
        {
            HTMLTableCellElement td = (HTMLTableCellElement)tableUI.getCellContentElementAt(row,i);
            if (td == null) continue;

            HTMLAnchorElement link = (HTMLAnchorElement)ItsNatTreeWalker.getFirstChildElement(td);
            decorateSelection(link,selected);
        }
    }

    public void decorateSelection(HTMLAnchorElement link,boolean selected)
    {
        if (selected)
            setAttribute(link,"style","background:rgb(0,0,255); color:white;");
        else
            setAttribute(link,"style","color:black;");
    }

    public static void setAttribute(Element elem,String name,String value)
    {
        String old = elem.getAttribute(name);
        if (old.equals(value)) return; // Avoids redundant operations

        elem.setAttribute(name,value);
    }
}
