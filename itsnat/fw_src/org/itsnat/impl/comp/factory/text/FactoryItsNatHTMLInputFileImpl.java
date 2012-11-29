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

import org.itsnat.impl.comp.factory.FactoryItsNatHTMLInputImpl;
import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.text.ItsNatHTMLInputFile;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.comp.text.ItsNatHTMLInputFileImpl;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatHTMLInputFileImpl extends FactoryItsNatHTMLInputImpl
{
    public final static FactoryItsNatHTMLInputFileImpl SINGLETON = new FactoryItsNatHTMLInputFileImpl();

    /** Creates a new instance of FactoryItsNatHTMLInputTextImpl */
    public FactoryItsNatHTMLInputFileImpl()
    {
    }

    public ItsNatHTMLElementComponent createItsNatHTMLComponent(HTMLElement element,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        return createItsNatHTMLInputFile((HTMLInputElement)element,artifacts,execCreateFilters,compMgr);
    }

    public String getTypeAttr()
    {
        return "file";
    }

    public String getCompType()
    {
        return null;
    }

    public ItsNatHTMLInputFile createItsNatHTMLInputFile(HTMLInputElement element,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        ItsNatHTMLInputFile comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatHTMLInputFile)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatHTMLInputFileImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatHTMLInputFile)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }
}
