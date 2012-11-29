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

package org.itsnat.feashow.features.comp.lists;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.itsnat.comp.list.ItsNatComboBox;
import org.w3c.dom.Element;


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
            setAttribute(elem,"style","background:rgb(0,0,255); color:white;");
        else
            elem.removeAttribute("style");
    }

    public static void setAttribute(Element elem,String name,String value)
    {
        String old = elem.getAttribute(name);
        if (old.equals(value)) return; // Avoids redundant operations

        elem.setAttribute(name,value);
    }
}
