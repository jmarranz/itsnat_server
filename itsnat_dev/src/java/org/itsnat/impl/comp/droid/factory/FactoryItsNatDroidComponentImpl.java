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

package org.itsnat.impl.comp.droid.factory;

import org.itsnat.impl.comp.factory.*;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatElementComponent;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.droid.ItsNatStfulDroidDocComponentManagerImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class FactoryItsNatDroidComponentImpl extends FactoryItsNatComponentImpl
{
    public FactoryItsNatDroidComponentImpl()
    {
    }

    public static String getKey(Element element,String compType)
    {
        return getKey(DOMUtilInternal.getLocalName(element),compType);
    }

    private static String getKey(String localName,String compType)
    {
        if (compType != null) // En elementos HTML reconocidos es opcional, puede ser null
            return "DROID:" + localName + ":" + compType;
        else
            return "DROID:" + localName;
    }

    public String getKey()
    {
        return getKey(getLocalName(),getCompType());
    }

    public ItsNatComponent createItsNatComponent(Element element,String compType,NameValue[] artifacts,boolean autoBuildMode,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        ItsNatStfulDroidDocComponentManagerImpl stfulCompMgr = (ItsNatStfulDroidDocComponentManagerImpl)compMgr;
        return createItsNatDroidComponent(element,compType,artifacts,execCreateFilters,stfulCompMgr);
    }

    protected abstract ItsNatElementComponent createItsNatDroidComponent(Element element, String compType, NameValue[] artifacts, boolean execCreateFilters, ItsNatStfulDroidDocComponentManagerImpl compMgr);


    public abstract String getLocalName();

}
