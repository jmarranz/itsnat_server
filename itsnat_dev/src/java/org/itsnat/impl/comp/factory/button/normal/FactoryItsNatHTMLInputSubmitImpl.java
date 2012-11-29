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

import org.itsnat.impl.comp.factory.FactoryItsNatHTMLInputImpl;
import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLInputSubmitImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatHTMLInputSubmitImpl extends FactoryItsNatHTMLInputImpl
{
    public final static FactoryItsNatHTMLInputSubmitImpl SINGLETON = new FactoryItsNatHTMLInputSubmitImpl();

    /** Creates a new instance of FactoryItsNatHTMLInputTextImpl */
    public FactoryItsNatHTMLInputSubmitImpl()
    {
    }

    public ItsNatHTMLElementComponent createItsNatHTMLComponent(HTMLElement element,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        return createItsNatHTMLInputSubmit((HTMLInputElement)element,artifacts,execCreateFilters,compMgr);
    }

    public String getTypeAttr()
    {
        return "submit";
    }

    public String getCompType()
    {
        return null;
    }

    public ItsNatHTMLInputSubmit createItsNatHTMLInputSubmit(HTMLInputElement element,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        ItsNatHTMLInputSubmit comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatHTMLInputSubmit)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatHTMLInputSubmitImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatHTMLInputSubmit)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }
}
