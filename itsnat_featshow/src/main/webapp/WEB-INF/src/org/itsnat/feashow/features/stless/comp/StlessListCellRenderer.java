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

import javax.swing.DefaultListModel;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.comp.list.ItsNatListMultSel;
import org.w3c.dom.Element;


public class StlessListCellRenderer implements ItsNatListCellRenderer
{
    public StlessListCellRenderer()
    {
    }

    @Override
    public void renderListCell(ItsNatList list, int index, Object value, boolean isSelected, boolean hasFocus, Element cellContentElem, boolean isNew)
    {
        ItsNatListMultSel listMult = (ItsNatListMultSel)list;

        DefaultListModel listModel = (DefaultListModel)listMult.getListModel();
        City city = (City)listModel.getElementAt(index);

        cellContentElem.setTextContent(city.getName());
        cellContentElem.setAttribute("cityId", String.valueOf(city.getId()));
    }

    @Override    
    public void unrenderListCell(ItsNatList list,int index,Element cellContentElem)
    {
    }
}
