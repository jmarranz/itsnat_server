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
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListUI;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLTableCellElement;

public class PocketIEFreeListSelectionDecorator implements ListSelectionListener
{
    protected ItsNatList comp;

    public PocketIEFreeListSelectionDecorator(ItsNatList comp)
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

    public void decorateSelection(int index,boolean selected)
    {
        ItsNatListUI compUI = comp.getItsNatListUI();
        HTMLTableCellElement td = (HTMLTableCellElement)compUI.getContentElementAt(index);
        if (td == null) return;

        HTMLAnchorElement link = (HTMLAnchorElement)ItsNatTreeWalker.getFirstChildElement(td);
        if (selected)
        {
            setAttribute(td,"style","background:rgb(0,0,255);");
            setAttribute(link,"style","color:white;");
        }
        else
        {
            td.removeAttribute("style");
            setAttribute(link,"style","color:black;");
        }
    }

    public static void setAttribute(Element elem,String name,String value)
    {
        String old = elem.getAttribute(name);
        if (old.equals(value)) return; // Avoids redundant operations

        elem.setAttribute(name,value);
    }
}
