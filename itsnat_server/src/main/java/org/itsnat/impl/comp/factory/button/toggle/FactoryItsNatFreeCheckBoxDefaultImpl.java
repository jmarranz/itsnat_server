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

package org.itsnat.impl.comp.factory.button.toggle;

import org.itsnat.comp.ItsNatFreeComponent;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBox;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.button.toggle.ItsNatFreeCheckBoxDefaultImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatFreeComponentImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatFreeCheckBoxDefaultImpl extends FactoryItsNatFreeComponentImpl
{
    public final static FactoryItsNatFreeCheckBoxDefaultImpl SINGLETON = new FactoryItsNatFreeCheckBoxDefaultImpl();

    /**
     * Creates a new instance of FactoryItsNatFreeCheckBoxDefaultImpl
     */
    public FactoryItsNatFreeCheckBoxDefaultImpl()
    {
    }

    public ItsNatFreeComponent createItsNatFreeComponent(Element elem,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        return createItsNatFreeCheckBox(elem,artifacts,execCreateFilters,compMgr);
    }

    public String getKey()
    {
        return "freeCheckBox";
    }

    public ItsNatFreeCheckBox createItsNatFreeCheckBox(Element element,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        ItsNatFreeCheckBox comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatFreeCheckBox)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatFreeCheckBoxDefaultImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatFreeCheckBox)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }
}
