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

package org.itsnat.impl.comp.mgr;

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.comp.list.ItsNatListCellEditor;
import org.itsnat.comp.table.ItsNatTableCellEditor;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.factory.layer.FactoryItsNatModalLayerImpl;
import org.itsnat.impl.core.doc.ItsNatXULDocumentImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatXULDocComponentManagerImpl extends ItsNatOtherNSDocComponentManagerImpl
{

    /**
     * Creates a new instance of ItsNatOtherNSDocComponentManagerImpl
     */
    public ItsNatXULDocComponentManagerImpl(ItsNatXULDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element,boolean clean,float opacity, String background, NameValue[] artifacts)
    {
        return createItsNatModalLayer(element,clean,getItsNatModalLayers().size() + 1,opacity,background,artifacts);
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatModalLayerImpl.SINGLETON.createItsNatModalLayerXUL(element,artifacts,true,this);
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element,boolean clean,int zIndex, float opacity, String background, NameValue[] artifacts)
    {
        return FactoryItsNatModalLayerImpl.SINGLETON.createItsNatModalLayerXUL(element,clean,zIndex,opacity,background,artifacts,true,this);
    }

    /* POR AHORA NO HAY COMPONENTE EDITOR POR DEFECTO XUL POR LO QUE DETECTAMOS
     * EL CASO NULO
     */
    public ItsNatLabelEditor createDefaultItsNatLabelEditor(ItsNatComponent compEditor)
    {
        if (compEditor == null) return null; // No hay editor por defecto en SVG
        return super.createDefaultItsNatLabelEditor(compEditor);
    }

    public ItsNatListCellEditor createDefaultItsNatListCellEditor(ItsNatComponent compEditor)
    {
        if (compEditor == null) return null; // No hay editor por defecto en SVG
        return super.createDefaultItsNatListCellEditor(compEditor);
    }

    public ItsNatTableCellEditor createDefaultItsNatTableCellEditor(ItsNatComponent compEditor)
    {
        if (compEditor == null) return null; // No hay editor por defecto en SVG
        return super.createDefaultItsNatTableCellEditor(compEditor);
    }

    public ItsNatTreeCellEditor createDefaultItsNatTreeCellEditor(ItsNatComponent compEditor)
    {
        if (compEditor == null) return null; // No hay editor por defecto en SVG
        return super.createDefaultItsNatTreeCellEditor(compEditor);
    }
}
