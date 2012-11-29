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

import java.io.Serializable;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableHeader;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.domutil.ElementListStructureDefaultImpl;
import org.itsnat.impl.core.domutil.ElementTableStructureDefaultImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatTableStructureDefaultImpl implements ItsNatTableStructure,Serializable
{
    protected final static ItsNatTableStructureDefaultImpl SINGLETON = new ItsNatTableStructureDefaultImpl();

    /** Creates a new instance of ItsNatTableStructureDefaultImpl */
    private ItsNatTableStructureDefaultImpl()
    {
    }

    public static ItsNatTableStructureDefaultImpl newItsNatTableStructureDefault()
    {
        // No se guarda estado, usamos el SINGLETON en este falso método factoría
        return SINGLETON;
    }

    public Element getHeadElement(ItsNatTable table,Element tableElem)
    {
        if (tableElem == null) tableElem = table.getElement();
        if (tableElem instanceof HTMLTableElement)
        {
            HTMLTableElement htmlTableElem = (HTMLTableElement)tableElem;
            return htmlTableElem.getTHead(); // Si no tiene <thead> devolverá null
        }
        else
        {
            Element firstChild = ItsNatTreeWalker.getFirstChildElement(tableElem);
            Element secondChild = ItsNatTreeWalker.getNextSiblingElement(firstChild); // Si no es null es el body
            if (secondChild == null) // No hay header, sólo body, firstChild es la fila patrón
                return null;
            else
                return firstChild; //  secondChild es el body
        }
    }

    public Element getBodyElement(ItsNatTable table,Element tableElem)
    {
        if (tableElem == null) tableElem = table.getElement();
        if (tableElem instanceof HTMLTableElement)
        {
            HTMLTableElement htmlTableElem = (HTMLTableElement)tableElem;

            // Buscamos el tbody, debe existir
            HTMLTableSectionElement tBody = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElementWithTagNameNS(htmlTableElem,NamespaceUtil.XHTML_NAMESPACE,"tbody");
            if (tBody == null) throw new ItsNatDOMException("Missing <tbody>",htmlTableElem);
            return tBody;
        }
        else
        {
            // En el caso de tabla sólo con body, éste sólo debería tener una sóla fila de patrón
            Element firstChild = ItsNatTreeWalker.getFirstChildElement(tableElem);
            Element secondChild = ItsNatTreeWalker.getNextSiblingElement(firstChild);
            if (secondChild == null) // No hay header, sólo body, firstChild es la fila patrón
                return tableElem;
            else
                return secondChild; // firstChild es el header
        }
    }

    public Element getHeaderColumnContentElement(ItsNatTableHeader tableHeader, int index, Element parentElem)
    {
        if (parentElem == null) parentElem = tableHeader.getItsNatTableHeaderUI().getElementAt(index);
        return ElementListStructureDefaultImpl.getContentElement(index,parentElem);
    }

    public Element getRowContentElement(ItsNatTable table, int row, Element rowElem)
    {
        if (rowElem == null) rowElem = table.getItsNatTableUI().getRowElementAt(row);
        return ElementTableStructureDefaultImpl.getRowContentElement(row,rowElem);
    }

    public Element getCellContentElement(ItsNatTable table, int row, int col, Element cellElem)
    {
        if (cellElem == null) cellElem = table.getItsNatTableUI().getCellElementAt(row,col);
        return ElementTableStructureDefaultImpl.getCellContentElement(row,col,cellElem);
    }

}
