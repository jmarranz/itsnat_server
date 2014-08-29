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

import org.itsnat.comp.ItsNatElementComponent;
import org.itsnat.comp.droid.CheckBox;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.droid.CheckBoxImpl;
import org.itsnat.impl.comp.mgr.droid.ItsNatStfulDroidDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FactoryCheckBoxImpl extends FactoryItsNatDroidComponentImpl
{
    public final static FactoryCheckBoxImpl SINGLETON = new FactoryCheckBoxImpl();

    /** Creates a new instance of FactoryItsNatHTMLInputTextImpl */
    public FactoryCheckBoxImpl()
    {
    }
    
    @Override
    protected ItsNatElementComponent createItsNatDroidComponent(Element element, String compType, NameValue[] artifacts, boolean execCreateFilters, ItsNatStfulDroidDocComponentManagerImpl compMgr)    
    {
        return createCheckBox(element,artifacts,execCreateFilters,compMgr);
    }        

    public CheckBox createCheckBox(Element element,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDroidDocComponentManagerImpl compMgr)
    {
        CheckBox comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (CheckBox)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new CheckBoxImpl(element,artifacts,compMgr);
        if (doFilters) comp = (CheckBox)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }    
    
    public String getCompType()
    {
        return null;
    }

    @Override
    public String getLocalName()
    {
        return "CheckBox";
    }    
}
