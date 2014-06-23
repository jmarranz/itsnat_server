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

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.comp.list.ItsNatListCellEditor;
import org.itsnat.comp.table.ItsNatTableCellEditor;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDroidDocComponentManagerImpl extends ItsNatStfulDocComponentManagerImpl
{
    public ItsNatStfulDroidDocComponentManagerImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }
    
    public ItsNatModalLayer createItsNatModalLayer(Element element,boolean clean,int zIndex,float opacity, String background, NameValue[] artifacts)
    {
        throw new ItsNatException("Not supported.");
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element,boolean clean,float opacity, String background, NameValue[] artifacts)
    {
        throw new ItsNatException("Not supported.");
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element,NameValue[] artifacts)
    {
        throw new ItsNatException("Not supported.");
    }

    public ItsNatLabelEditor createDefaultItsNatLabelEditor(ItsNatComponent compEditor)
    {
        return null; // Permite usar componentes con elementos XML
    }

    public ItsNatListCellEditor createDefaultItsNatListCellEditor(ItsNatComponent compEditor)
    {
        return null; // Permite usar componentes con elementos XML
    }

    public ItsNatTableCellEditor createDefaultItsNatTableCellEditor(ItsNatComponent compEditor)
    {
        return null; // Permite usar componentes con elementos XML
    }

    public ItsNatTreeCellEditor createDefaultItsNatTreeCellEditor(ItsNatComponent compEditor)
    {
        return null; // Permite usar componentes con elementos XML
    }    
}
