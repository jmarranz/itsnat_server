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

package org.itsnat.feashow.features.comp.tables;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableUI;
import org.w3c.dom.Element;

public class SVGCircleTableRowSelectionDecoration implements ListSelectionListener
{
    protected ItsNatTable comp;

    public SVGCircleTableRowSelectionDecoration(ItsNatTable comp)
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
            Element circleElem = tableUI.getCellContentElementAt(row,i);
            if (circleElem == null) return;
            decorateSelection(circleElem,selected);
        }
    }

    public void decorateSelection(Element circleElem,boolean selected)
    {
        String fillColor;
        if (selected)
            fillColor = "#ff0000";
        else
            fillColor = "#0000ff";

        circleElem.setAttribute("fill",fillColor);
    }

}
