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

package org.itsnat.impl.comp.factory.text;

import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.factory.FactoryItsNatHTMLComponentImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.comp.text.ItsNatHTMLTextAreaImpl;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatHTMLTextAreaImpl extends FactoryItsNatHTMLComponentImpl
{
    public final static FactoryItsNatHTMLTextAreaImpl SINGLETON = new FactoryItsNatHTMLTextAreaImpl();

    /**
     * Creates a new instance of FactoryItsNatHTMLAnchorImpl
     */
    public FactoryItsNatHTMLTextAreaImpl()
    {
    }

    public ItsNatHTMLElementComponent createItsNatHTMLComponent(HTMLElement element,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        return createItsNatHTMLTextArea((HTMLTextAreaElement)element,artifacts,execCreateFilters,compMgr);
    }

    public String getLocalName()
    {
        return "textarea";
    }

    public boolean isFormControl()
    {
        return true;
    }

    public String getCompType()
    {
        return null;
    }

    public ItsNatHTMLTextArea createItsNatHTMLTextArea(HTMLTextAreaElement element,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        ItsNatHTMLTextArea comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatHTMLTextArea)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatHTMLTextAreaImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatHTMLTextArea)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }
}
