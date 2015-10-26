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

package org.itsnat.impl.comp.android.factory.widget;

import org.itsnat.comp.ItsNatElementComponent;
import org.itsnat.comp.android.widget.TextView;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.android.widget.TextViewDefaultImpl;
import static org.itsnat.impl.comp.factory.FactoryItsNatComponentImpl.hasBeforeAfterCreateItsNatComponentListener;
import static org.itsnat.impl.comp.factory.FactoryItsNatComponentImpl.processAfterCreateItsNatComponentListener;
import static org.itsnat.impl.comp.factory.FactoryItsNatComponentImpl.processBeforeCreateItsNatComponentListener;
import static org.itsnat.impl.comp.factory.FactoryItsNatComponentImpl.registerItsNatComponent;
import org.itsnat.impl.comp.mgr.droid.ItsNatStfulDroidDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FactoryDroidTextViewDefaultImpl extends FactoryDroidTextViewImpl
{
    public final static FactoryDroidTextViewDefaultImpl SINGLETON = new FactoryDroidTextViewDefaultImpl();

    /** Creates a new instance of FactoryItsNatHTMLInputTextImpl */
    public FactoryDroidTextViewDefaultImpl()
    {
    }
    
    @Override
    protected ItsNatElementComponent createItsNatDroidComponent(Element element, String compType, NameValue[] artifacts, boolean execCreateFilters, ItsNatStfulDroidDocComponentManagerImpl compMgr)    
    {
        return createTextView(element,artifacts,execCreateFilters,compMgr);
    }        

    public TextView createTextView(Element element,NameValue[] artifacts,boolean execCreateFilters,ItsNatStfulDroidDocComponentManagerImpl compMgr)
    {
        TextView comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (TextView)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new TextViewDefaultImpl(element,artifacts,compMgr);
        if (doFilters) comp = (TextView)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }    
    
    @Override
    public String getCompType()
    {
        return null;
    }

    @Override
    public String getLocalName()
    {
        return "TextView";
    }    
}
