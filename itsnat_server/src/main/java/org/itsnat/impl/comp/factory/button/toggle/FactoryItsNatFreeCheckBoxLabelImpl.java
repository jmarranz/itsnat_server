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
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBoxLabel;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.button.toggle.ItsNatFreeCheckBoxLabelImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatFreeComponentImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatFreeCheckBoxLabelImpl extends FactoryItsNatFreeComponentImpl
{
    public final static FactoryItsNatFreeCheckBoxLabelImpl SINGLETON = new FactoryItsNatFreeCheckBoxLabelImpl();

    /**
     * Creates a new instance of FactoryItsNatFreeCheckBoxDefaultImpl
     */
    public FactoryItsNatFreeCheckBoxLabelImpl()
    {
    }

    public ItsNatFreeComponent createItsNatFreeComponent(Element elem,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        return createItsNatFreeCheckBoxLabel(elem,artifacts,execCreateFilters,compMgr);
    }

    public String getKey()
    {
        return "freeCheckBoxLabel";
    }

    public ItsNatFreeCheckBoxLabel createItsNatFreeCheckBoxLabel(Element element,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        ItsNatFreeCheckBoxLabel comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatFreeCheckBoxLabel)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatFreeCheckBoxLabelImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatFreeCheckBoxLabel)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }
}
