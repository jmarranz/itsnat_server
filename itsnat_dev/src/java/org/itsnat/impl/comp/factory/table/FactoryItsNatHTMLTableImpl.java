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

package org.itsnat.impl.comp.factory.table;

import org.itsnat.impl.comp.factory.FactoryItsNatHTMLComponentImpl;
import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.comp.table.ItsNatHTMLTableImpl;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableElement;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatHTMLTableImpl extends FactoryItsNatHTMLComponentImpl
{
    public final static FactoryItsNatHTMLTableImpl SINGLETON = new FactoryItsNatHTMLTableImpl();

    /**
     * Creates a new instance of FactoryItsNatHTMLAnchorImpl
     */
    public FactoryItsNatHTMLTableImpl()
    {
    }

    public ItsNatHTMLElementComponent createItsNatHTMLComponent(HTMLElement element,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        return createItsNatHTMLTable((HTMLTableElement)element,null,artifacts,execCreateFilters,compMgr);
    }

    public String getLocalName()
    {
        return "table";
    }

    public boolean isFormControl()
    {
        return false;
    }

    public String getCompType()
    {
        return null;
    }

    public ItsNatHTMLTable createItsNatHTMLTable(HTMLTableElement element,ItsNatTableStructure structure,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        ItsNatHTMLTable comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatHTMLTable)processBeforeCreateItsNatComponentListener(element,getCompType(),getUseStructureParamArray(structure),artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatHTMLTableImpl(element,structure,artifacts,compMgr);
        if (doFilters) comp = (ItsNatHTMLTable)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }
}
