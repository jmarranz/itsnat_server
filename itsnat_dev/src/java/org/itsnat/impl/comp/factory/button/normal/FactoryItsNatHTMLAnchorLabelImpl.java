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
import org.itsnat.comp.button.normal.ItsNatHTMLAnchorLabel;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLAnchorLabelImpl;
import org.itsnat.impl.comp.mgr.web.ItsNatStfulWebDocComponentManagerImpl;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatHTMLAnchorLabelImpl extends FactoryItsNatHTMLAnchorImpl
{
    public final static FactoryItsNatHTMLAnchorLabelImpl SINGLETON = new FactoryItsNatHTMLAnchorLabelImpl();

    /**
     * Creates a new instance of FactoryItsNatHTMLAnchorImpl
     */
    public FactoryItsNatHTMLAnchorLabelImpl()
    {
    }

    protected ItsNatHTMLElementComponent createItsNatHTMLComponent(HTMLElement element, String compType, NameValue[] artifacts, boolean execCreateFilters, ItsNatStfulWebDocComponentManagerImpl compMgr)
    {
        return createItsNatHTMLAnchorLabel((HTMLAnchorElement)element,artifacts,execCreateFilters,compMgr);
    }

    public String getCompType()
    {
        return "buttonLabel";
    }

    public ItsNatHTMLAnchorLabel createItsNatHTMLAnchorLabel(HTMLAnchorElement element,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulWebDocComponentManagerImpl compMgr)
    {
        ItsNatHTMLAnchorLabel comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatHTMLAnchorLabel)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatHTMLAnchorLabelImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatHTMLAnchorLabel)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

}
