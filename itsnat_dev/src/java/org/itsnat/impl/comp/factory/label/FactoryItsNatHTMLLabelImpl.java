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

package org.itsnat.impl.comp.factory.label;

import org.itsnat.impl.comp.factory.FactoryItsNatHTMLComponentImpl;
import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.label.ItsNatHTMLLabel;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.label.ItsNatHTMLLabelImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLLabelElement;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatHTMLLabelImpl extends FactoryItsNatHTMLComponentImpl
{
    public final static FactoryItsNatHTMLLabelImpl SINGLETON = new FactoryItsNatHTMLLabelImpl();

    /**
     * Creates a new instance of FactoryItsNatHTMLAnchorImpl
     */
    public FactoryItsNatHTMLLabelImpl()
    {
    }

    public ItsNatHTMLElementComponent createItsNatHTMLComponent(HTMLElement element,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        return createItsNatHTMLLabel((HTMLLabelElement)element,artifacts,execCreateFilters,compMgr);
    }

    public String getLocalName()
    {
        return "label";
    }

    public boolean isFormControl()
    {
        return false; // Aunque un <label> esté dentro de un form no es un control, no nos interesa por ejemplo que sea editable por defecto un simple label etc
    }

    public String getCompType()
    {
        return null;
    }

    public ItsNatHTMLLabel createItsNatHTMLLabel(HTMLLabelElement element,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        ItsNatHTMLLabel comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatHTMLLabel)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatHTMLLabelImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatHTMLLabel)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

}
