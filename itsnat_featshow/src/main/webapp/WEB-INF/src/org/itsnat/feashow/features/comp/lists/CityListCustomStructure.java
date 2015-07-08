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
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;

public class CityListCustomStructure implements ItsNatListStructure
{
    public CityListCustomStructure()
    {
    }

    public Element getContentElement(ItsNatList list, int index, Element parentElem)
    {
        HTMLTableRowElement rowElem = (HTMLTableRowElement)parentElem;
        HTMLTableCellElement firstCellElem = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
        HTMLTableCellElement secondCellElem = (HTMLTableCellElement)ItsNatTreeWalker.getNextSiblingElement(firstCellElem);
        return secondCellElem;
    }
}
