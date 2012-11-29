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

import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.feashow.features.comp.shared.Circle;
import org.w3c.dom.Element;

public class CircleListCellRenderer implements ItsNatListCellRenderer
{

    public CircleListCellRenderer()
    {
    }

    public void renderListCell(ItsNatList list, int index, Object value, boolean isSelected, boolean cellHasFocus, Element cellElem,boolean isNew)
    {
        Circle circle = (Circle)value;

        int radio = circle.getRadio();
        int cx = index * radio + 2*radio;
        cellElem.setAttribute("cx",Integer.toString(cx));
        cellElem.setAttribute("r",Integer.toString(radio));
    }

    public void unrenderListCell(ItsNatList list,int index,Element cellContentElem)
    {
    }
}
