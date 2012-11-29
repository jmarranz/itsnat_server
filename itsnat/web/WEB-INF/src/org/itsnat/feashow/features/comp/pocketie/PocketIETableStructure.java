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

import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableHeader;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;

public class PocketIETableStructure implements ItsNatTableStructure
{
    protected ItsNatTableStructure defaultStruct;

    public PocketIETableStructure(ItsNatTableStructure defaultStruct)
    {
        this.defaultStruct = defaultStruct;
    }

    public Element getHeadElement(ItsNatTable table, Element tableElem)
    {
        return defaultStruct.getHeadElement(table, tableElem);
    }

    public Element getBodyElement(ItsNatTable table, Element tableElem)
    {
        return defaultStruct.getBodyElement(table, tableElem);
    }

    public Element getRowContentElement(ItsNatTable table, int row, Element rowElem)
    {
        return defaultStruct.getRowContentElement(table, row, rowElem);
    }

    public Element getCellContentElement(ItsNatTable table, int row, int col, Element cellElem)
    {
        return (HTMLAnchorElement)ItsNatTreeWalker.getFirstChildElement(cellElem);
    }

    public Element getHeaderColumnContentElement(ItsNatTableHeader tableHeader, int column, Element itemElem)
    {
        return (HTMLAnchorElement)ItsNatTreeWalker.getFirstChildElement(itemElem);
    }

}
