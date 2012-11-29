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

package org.itsnat.impl.comp.factory.list;

import org.itsnat.impl.comp.factory.FactoryItsNatHTMLComponentImpl;
import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.list.ItsNatHTMLSelectComboBoxImpl;
import org.itsnat.impl.comp.list.ItsNatHTMLSelectMultImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatHTMLSelectImpl extends FactoryItsNatHTMLComponentImpl
{
    public final static FactoryItsNatHTMLSelectImpl SINGLETON = new FactoryItsNatHTMLSelectImpl();

    /**
     * Creates a new instance of FactoryItsNatHTMLAnchorImpl
     */
    public FactoryItsNatHTMLSelectImpl()
    {
    }

    public ItsNatHTMLElementComponent createItsNatHTMLComponent(HTMLElement element,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        HTMLSelectElement select = (HTMLSelectElement)element;
        if (select.getMultiple())
            return createItsNatHTMLSelectMult(select, artifacts,execCreateFilters,compMgr);
        else
            return createItsNatHTMLSelectComboBox(select, artifacts,execCreateFilters,compMgr);
    }

    public String getLocalName()
    {
        return "select";
    }

    public boolean isFormControl()
    {
        return true;
    }

    public String getCompType()
    {
        return null;
    }

    public ItsNatHTMLSelectMult createItsNatHTMLSelectMult(HTMLSelectElement element,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        ItsNatHTMLSelectMult comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatHTMLSelectMult)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatHTMLSelectMultImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatHTMLSelectMult)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

    public ItsNatHTMLSelectComboBox createItsNatHTMLSelectComboBox(HTMLSelectElement element,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        ItsNatHTMLSelectComboBox comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatHTMLSelectComboBox)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatHTMLSelectComboBoxImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatHTMLSelectComboBox)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }
}
