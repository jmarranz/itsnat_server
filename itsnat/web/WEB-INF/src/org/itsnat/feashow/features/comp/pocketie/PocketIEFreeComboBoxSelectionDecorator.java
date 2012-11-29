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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.itsnat.comp.list.ItsNatComboBox;
import org.itsnat.comp.list.ItsNatListUI;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLTableCellElement;


public class PocketIEFreeComboBoxSelectionDecorator implements ItemListener
{
    protected ItsNatComboBox comp;

    public PocketIEFreeComboBoxSelectionDecorator(ItsNatComboBox comp)
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
        ItsNatListUI compUI = comp.getItsNatListUI();
        HTMLTableCellElement td = (HTMLTableCellElement)compUI.getContentElementAt(index);
        if (td == null) return;

        HTMLAnchorElement link = (HTMLAnchorElement)ItsNatTreeWalker.getFirstChildElement(td);
        if (selected)
        {
            setAttribute(td,"style","background:rgb(0,0,255); color:white;");
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
