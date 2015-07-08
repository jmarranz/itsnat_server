/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.comp.table;

import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.table.ItsNatHTMLTableHeader;
import org.itsnat.comp.table.ItsNatHTMLTableHeaderUI;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLTableHeaderUIImpl extends ItsNatTableHeaderUIImpl implements ItsNatHTMLTableHeaderUI
{

    /**
     * Creates a new instance of ItsNatHTMLTableHeaderUIImpl
     */
    public ItsNatHTMLTableHeaderUIImpl(ItsNatHTMLTableHeaderImpl parentComp)
    {
        super(parentComp,getUniqueHTMLTableRowElement(parentComp));
    }

    public static HTMLTableRowElement getUniqueHTMLTableRowElement(ItsNatHTMLTableHeaderImpl parentComp)
    {
        HTMLTableSectionElement tHeadElement = parentComp.getHTMLTableSectionElement();

        HTMLTableRowElement rowElement = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElementWithTagNameNS(tHeadElement,NamespaceUtil.XHTML_NAMESPACE,"tr");
        if (rowElement == null)
            throw new ItsNatException("Header needs almost a row",parentComp);
        return rowElement;
    }

    public HTMLTableSectionElement getHTMLTableSectionElement()
    {
        return getItsNatHTMLTableHeader().getHTMLTableSectionElement();
    }

    public HTMLTableRowElement getHTMLTableRowElement()
    {
        return getItsNatHTMLTableHeader().getHTMLTableRowElement();
    }

    public ItsNatHTMLTableHeader getItsNatHTMLTableHeader()
    {
        return (ItsNatHTMLTableHeader)parentComp;
    }

    public HTMLTableCellElement getHTMLTableCellElementAt(int column)
    {
        return (HTMLTableCellElement)getElementAt(column);
    }

    public ItsNatHTMLElementComponent getItsNatHTMLElementComponent()
    {
        return (ItsNatHTMLElementComponent)parentComp;
    }

}
