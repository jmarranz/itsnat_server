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

package org.itsnat.impl.comp.factory.tree;

import org.itsnat.comp.ItsNatFreeComponent;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.comp.tree.ItsNatTreeStructure;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.factory.FactoryItsNatFreeComponentImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.tree.ItsNatFreeTreeImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatFreeTreeImpl extends FactoryItsNatFreeComponentImpl
{
    public final static FactoryItsNatFreeTreeImpl SINGLETON = new FactoryItsNatFreeTreeImpl();

    /**
     * Creates a new instance of FactoryItsNatFreeTreeImpl
     */
    public FactoryItsNatFreeTreeImpl()
    {
    }

    public ItsNatFreeComponent createItsNatFreeComponent(Element elem,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        return createItsNatFreeTree(elem,artifacts,execCreateFilters,compMgr);
    }

    public String getKey()
    {
        return "freeTree";
    }

    public ItsNatFreeTree createItsNatFreeTree(Element element,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        ItsNatFreeTree comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatFreeTree)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatFreeTreeImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatFreeTree)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

    public ItsNatFreeTree createItsNatFreeTree(Element element,boolean treeTable,boolean rootless,ItsNatTreeStructure structure,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        ItsNatFreeTree comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatFreeTree)processBeforeCreateItsNatComponentListener(element,getCompType(),getParams(treeTable,rootless,structure),artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatFreeTreeImpl(treeTable,rootless,element,structure,artifacts,compMgr);
        if (doFilters) comp = (ItsNatFreeTree)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

    private NameValue[] getParams(boolean treeTable,boolean rootless,ItsNatTreeStructure structure)
    {
        NameValue treeTableParam = new NameValue("treeTable",Boolean.valueOf(treeTable));
        NameValue rootlessParam = new NameValue("rootless",Boolean.valueOf(rootless));
        NameValue useStructureParam = getUseStructureParam(structure);
        if (useStructureParam != null)
            return new NameValue[]{treeTableParam,rootlessParam,useStructureParam};
        else
            return new NameValue[]{treeTableParam,rootlessParam};
    }
}
