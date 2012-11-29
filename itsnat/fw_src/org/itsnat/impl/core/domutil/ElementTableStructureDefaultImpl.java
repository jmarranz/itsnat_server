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

package org.itsnat.impl.core.domutil;

import java.io.Serializable;
import org.itsnat.core.domutil.ElementTableStructure;
import org.itsnat.core.domutil.ElementTable;
import org.w3c.dom.Element;

/**

 * @author jmarranz
 */
public class ElementTableStructureDefaultImpl implements ElementTableStructure,Serializable
{
    public static final ElementTableStructureDefaultImpl SINGLETON = new ElementTableStructureDefaultImpl();

    /**
     * Creates a new instance of ElementListStructureDefaultImpl
     */
    private ElementTableStructureDefaultImpl()
    {
    }

    public static ElementTableStructureDefaultImpl newElementTableStructureDefault()
    {
        // A día de hoy no se guarda estado por lo que el SINGLETON ayuda a disminuir el número de objetos
        return SINGLETON;
    }

    public static Element getRowContentElement(int row, Element rowElem)
    {
        return rowElem;
    }

    public static Element getCellContentElement(int row, int col, Element cellElem)
    {
        return cellElem;
    }

    public Element getRowContentElement(ElementTable table, int row, Element rowElem)
    {
        if (rowElem == null) rowElem = table.getRowElementAt(row);
        return getRowContentElement(row,rowElem);
    }

    public Element getCellContentElement(ElementTable table, int row, int col, Element cellElem)
    {
        if (cellElem == null) cellElem = table.getCellElementAt(row,col);
        return getCellContentElement(row,col,cellElem);
    }

}
