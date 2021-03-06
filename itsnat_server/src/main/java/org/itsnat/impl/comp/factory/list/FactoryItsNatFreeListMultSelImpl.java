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

package org.itsnat.impl.comp.factory.list;

import org.itsnat.comp.ItsNatFreeComponent;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.factory.FactoryItsNatFreeComponentImpl;
import org.itsnat.impl.comp.list.ItsNatFreeListMultSelImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatFreeListMultSelImpl extends FactoryItsNatFreeComponentImpl
{
    public final static FactoryItsNatFreeListMultSelImpl SINGLETON = new FactoryItsNatFreeListMultSelImpl();

    /**
     * Creates a new instance of FactoryItsNatFreeListMultSelImpl
     */
    public FactoryItsNatFreeListMultSelImpl()
    {
    }

    public ItsNatFreeComponent createItsNatFreeComponent(Element elem,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        return createItsNatFreeListMultSel(elem,null,artifacts,execCreateFilters,compMgr);
    }

    public String getKey()
    {
        return "freeListMultSel";
    }

    public ItsNatFreeListMultSel createItsNatFreeListMultSel(Element element,ItsNatListStructure structure,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        ItsNatFreeListMultSel comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatFreeListMultSel)processBeforeCreateItsNatComponentListener(element,getCompType(),getUseStructureParamArray(structure),artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatFreeListMultSelImpl(element,structure,artifacts,compMgr);
        if (doFilters) comp = (ItsNatFreeListMultSel)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }
}
