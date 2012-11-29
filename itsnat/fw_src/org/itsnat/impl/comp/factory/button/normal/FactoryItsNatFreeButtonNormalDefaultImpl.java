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

package org.itsnat.impl.comp.factory.button.normal;

import org.itsnat.comp.ItsNatFreeComponent;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormal;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.button.normal.ItsNatFreeButtonNormalDefaultImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatFreeComponentImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatFreeButtonNormalDefaultImpl extends FactoryItsNatFreeComponentImpl
{
    public final static FactoryItsNatFreeButtonNormalDefaultImpl SINGLETON = new FactoryItsNatFreeButtonNormalDefaultImpl();
    /**
     * Creates a new instance of FactoryItsNatFreeButtonNormalDefaultImpl
     */
    public FactoryItsNatFreeButtonNormalDefaultImpl()
    {
    }

    public ItsNatFreeComponent createItsNatFreeComponent(Element elem,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        return createItsNatFreeButtonNormalDefault(elem,artifacts,execCreateFilters,compMgr);
    }

    public ItsNatFreeButtonNormal createItsNatFreeButtonNormalDefault(Element element,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        ItsNatFreeButtonNormal comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatFreeButtonNormal)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatFreeButtonNormalDefaultImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatFreeButtonNormal)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

    public String getKey()
    {
        return "freeButtonNormal";
    }
}
