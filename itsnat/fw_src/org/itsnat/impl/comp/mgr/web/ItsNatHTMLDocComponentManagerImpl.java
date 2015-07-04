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

package org.itsnat.impl.comp.mgr.web;

import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.factory.layer.FactoryItsNatModalLayerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.Element;

/**
 * Leer notas de HTMLElementGroupManagerImpl sobre la dependencia
 * con ItsNatHTMLDocument
 *
 * @author jmarranz
 */
public class ItsNatHTMLDocComponentManagerImpl extends ItsNatStfulWebDocComponentManagerImpl
{

    /** Creates a new instance of ItsNatHTMLDocComponentManagerImpl */
    public ItsNatHTMLDocComponentManagerImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element,int zIndex, float opacity, String background, NameValue[] artifacts)
    {
        return createItsNatModalLayer(element,false,zIndex,opacity,background,artifacts);
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element,boolean clean, float opacity, String background, NameValue[] artifacts)
    {
        int zIndex = getItsNatModalLayers().size() + 1;
        return createItsNatModalLayer(element,clean,zIndex,opacity,background,artifacts);
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatModalLayerImpl.SINGLETON.createItsNatModalLayerHTML(element,artifacts,true,this);
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element,boolean clean, int zIndex, float opacity, String background, NameValue[] artifacts)
    {
        return FactoryItsNatModalLayerImpl.SINGLETON.createItsNatModalLayerHTML(element,clean,zIndex,opacity,background,artifacts,true,this);
    }
}
