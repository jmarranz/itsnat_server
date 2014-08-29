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

import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLButtonDefaultImpl;
import org.itsnat.impl.comp.mgr.web.ItsNatStfulWebDocComponentManagerImpl;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatHTMLButtonDefaultImpl extends FactoryItsNatHTMLButtonImpl
{
    public final static FactoryItsNatHTMLButtonDefaultImpl SINGLETON = new FactoryItsNatHTMLButtonDefaultImpl();

    /**
     * Creates a new instance of FactoryItsNatHTMLButtonImpl
     */
    public FactoryItsNatHTMLButtonDefaultImpl()
    {
    }

    protected ItsNatHTMLElementComponent createItsNatHTMLComponent(HTMLElement element, String compType, NameValue[] artifacts, boolean execCreateFilters, ItsNatStfulWebDocComponentManagerImpl compMgr)
    {
        return createItsNatHTMLButtonDefault((HTMLButtonElement)element,artifacts,execCreateFilters,compMgr);
    }

    public String getCompType()
    {
        return null;
    }

    public ItsNatHTMLButton createItsNatHTMLButtonDefault(HTMLButtonElement element,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulWebDocComponentManagerImpl compMgr)
    {
        ItsNatHTMLButton comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatHTMLButton)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatHTMLButtonDefaultImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatHTMLButton)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

}
