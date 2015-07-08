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

import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableCellRenderer;
import org.itsnat.feashow.features.comp.shared.Circle;
import org.w3c.dom.Element;

public class CircleTableCellRenderer implements ItsNatTableCellRenderer
{
    public CircleTableCellRenderer()
    {
    }

    public void renderTableCell(ItsNatTable table, int row, int column, Object value, boolean isSelected, boolean hasFocus, Element cellElem,boolean isNew)
    {
        Circle circle = (Circle)value;

        int radio = circle.getRadio();
        int cx = column * radio + 2*radio;
        int cy = row * 2 * radio + 2*radio;
        cellElem.setAttribute("cx",Integer.toString(cx));
        cellElem.setAttribute("cy",Integer.toString(cy));
        cellElem.setAttribute("r",Integer.toString(radio));
    }

    public void unrenderTableCell(ItsNatTable table,int row,int column,Element cellContentElem)
    {
    }
}
