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

package org.itsnat.feashow.features.stless.comp;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.w3c.dom.Element;

public class StlessListSelectionListener implements ListSelectionListener
{
    protected StlessFreeListExampleInitialDocument parent;

    public StlessListSelectionListener(StlessFreeListExampleInitialDocument parent)
    {
        this.parent = parent;
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
        
        ItsNatFreeListMultSel listComp = parent.getItsNatFreeListMultSel();
        
        int index = listComp.getSelectedIndex(); // First selected  
        if (index != -1)  
        {  
            City city = (City)listComp.getListModel().getElementAt(index);  
            parent.getItemItsNatHTMLInputText().setText(city.getName());  
            parent.getPosItsNatHTMLInputText().setText(Integer.toString(index));  
        }          
    }

    private void decorateSelection(int index,boolean selected)
    {
        ItsNatFreeListMultSel listComp = parent.getItsNatFreeListMultSel();
        
        Element elem = listComp.getItsNatListUI().getContentElementAt(index);
        if (elem == null) return;

        if (selected)
        {
            setAttribute(elem,"style","background:rgb(0,0,255); color:white;");
            setAttribute(elem,"selected","true");                      
        }
        else
        {
            elem.removeAttribute("style");
            elem.removeAttribute("selected");
        }
    }

    public static void setAttribute(Element elem,String name,String value)
    {
        elem.removeAttribute(name); // Ensure is changed in client
        elem.setAttribute(name,value);
    }
}
