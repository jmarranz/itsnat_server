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
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ElementListStructure;
import org.itsnat.core.domutil.ElementTable;
import org.itsnat.core.domutil.ElementTableStructure;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatTableStructureCoreAdapterImpl implements ElementTableStructure,ElementListStructure,Serializable
{
    protected ItsNatTableStructure structure;
    protected ItsNatTable tableComp;
    protected ItsNatTableHeader tableHeaderComp;

    /**
     * Creates a new instance of ItsNatTableStructureCoreAdapterImpl
     */
    public ItsNatTableStructureCoreAdapterImpl(ItsNatTableStructure structure,ItsNatTable tableComp,ItsNatTableHeader tableHeaderComp)
    {
        this.structure = structure;
        this.tableComp = tableComp;
        this.tableHeaderComp = tableHeaderComp;
    }

    public Element getContentElement(ElementList list, int index, Element elem)
    {
        return structure.getHeaderColumnContentElement(tableHeaderComp,index,elem);
    }

    public Element getRowContentElement(ElementTable table, int row, Element rowElem)
    {
        return structure.getRowContentElement(tableComp,row,rowElem);
    }

    public Element getCellContentElement(ElementTable table, int row, int col, Element cellElem)
    {
        return structure.getCellContentElement(tableComp,row,col,cellElem);
    }
}
