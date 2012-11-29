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

package org.itsnat.impl.comp.factory;

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatFreeComponent;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class FactoryItsNatFreeComponentImpl extends FactoryItsNatComponentImpl
{
    public FactoryItsNatFreeComponentImpl()
    {
    }

    public String getCompType()
    {
        return getKey();
    }

    public ItsNatComponent createItsNatComponent(Element elem,String compType,NameValue[] artifacts,boolean ignoreIsComponentAttr,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        return createItsNatFreeComponent(elem,compType,artifacts,execCreateFilters,compMgr);
    }

    public abstract ItsNatFreeComponent createItsNatFreeComponent(Element elem,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr);
}
