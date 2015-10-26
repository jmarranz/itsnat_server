/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.comp.mgr.droid;

import java.util.HashMap;
import java.util.Map;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.comp.list.ItsNatListCellEditor;
import org.itsnat.comp.table.ItsNatTableCellEditor;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.android.factory.widget.FactoryDroidCheckBoxImpl;
import org.itsnat.impl.comp.android.factory.FactoryItsNatDroidComponentImpl;
import org.itsnat.impl.comp.android.factory.widget.FactoryDroidTextViewDefaultImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatComponentImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDroidDocComponentManagerImpl extends ItsNatStfulDocComponentManagerImpl
{
    protected static final Map<String,FactoryItsNatDroidComponentImpl> DROID_FACTORIES = new HashMap<String,FactoryItsNatDroidComponentImpl>(); // No sincronizamos porque va a ser siempre usada en modo lectura
    
    static
    {
        addDroidFactory(FactoryDroidCheckBoxImpl.SINGLETON);        
        addDroidFactory(FactoryDroidTextViewDefaultImpl.SINGLETON);        
    }
    
    public ItsNatStfulDroidDocComponentManagerImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }
    
    protected static void addDroidFactory(FactoryItsNatDroidComponentImpl factory)
    {
        DROID_FACTORIES.put(factory.getKey(),factory);
    }    
    
    @Override
    protected FactoryItsNatComponentImpl getFactoryItsNatComponent(Element elem,String compType)
    {
        FactoryItsNatComponentImpl factory = super.getFactoryItsNatComponent(elem,compType);
        if (factory != null) return factory;

        return getDroidFactoryStatic(elem,compType);
    }        
    
    protected static FactoryItsNatComponentImpl getDroidFactoryStatic(Element elem,String compType)
    {
        String key = FactoryItsNatDroidComponentImpl.getKey(elem,compType);

        return DROID_FACTORIES.get(key);
    }    
    
    @Override
    public ItsNatModalLayer createItsNatModalLayer(Element element,boolean clean,int zIndex,float opacity, String background, NameValue[] artifacts)
    {
        throw new ItsNatException("Not supported.");
    }

    @Override
    public ItsNatModalLayer createItsNatModalLayer(Element element,boolean clean,float opacity, String background, NameValue[] artifacts)
    {
        throw new ItsNatException("Not supported.");
    }

    @Override
    public ItsNatModalLayer createItsNatModalLayer(Element element,NameValue[] artifacts)
    {
        throw new ItsNatException("Not supported.");
    }

    @Override
    public ItsNatLabelEditor createDefaultItsNatLabelEditor(ItsNatComponent compEditor)
    {
        return null; // Permite usar componentes con elementos XML
    }

    @Override
    public ItsNatListCellEditor createDefaultItsNatListCellEditor(ItsNatComponent compEditor)
    {
        return null; // Permite usar componentes con elementos XML
    }

    @Override
    public ItsNatTableCellEditor createDefaultItsNatTableCellEditor(ItsNatComponent compEditor)
    {
        return null; // Permite usar componentes con elementos XML
    }

    @Override
    public ItsNatTreeCellEditor createDefaultItsNatTreeCellEditor(ItsNatComponent compEditor)
    {
        return null; // Permite usar componentes con elementos XML
    }    
}
