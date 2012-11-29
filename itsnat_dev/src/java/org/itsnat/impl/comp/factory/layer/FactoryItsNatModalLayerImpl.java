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

package org.itsnat.impl.comp.factory.layer;

import org.itsnat.comp.ItsNatFreeComponent;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.factory.FactoryItsNatFreeComponentImpl;
import org.itsnat.impl.comp.layer.ItsNatModalLayerHTMLImpl;
import org.itsnat.impl.comp.layer.ItsNatModalLayerSVGImpl;
import org.itsnat.impl.comp.layer.ItsNatModalLayerXULImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.ItsNatHTMLDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.ItsNatSVGDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.ItsNatXULDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatModalLayerImpl extends FactoryItsNatFreeComponentImpl
{
    public final static FactoryItsNatModalLayerImpl SINGLETON = new FactoryItsNatModalLayerImpl();

    /**
     * Creates a new instance of FactoryItsNatFreeLabelImpl
     */
    public FactoryItsNatModalLayerImpl()
    {
    }

    public ItsNatFreeComponent createItsNatFreeComponent(Element elem,String compType,NameValue[] artifacts,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        if (compMgr instanceof ItsNatHTMLDocComponentManagerImpl)
            return createItsNatModalLayerHTML(elem,artifacts,execCreateFilters,(ItsNatHTMLDocComponentManagerImpl)compMgr);
        else if (compMgr instanceof ItsNatSVGDocComponentManagerImpl)
            return createItsNatModalLayerSVG(elem,artifacts,execCreateFilters,(ItsNatSVGDocComponentManagerImpl)compMgr);
        else
            throw new ItsNatException("Not supported.");
    }

    public String getKey()
    {
        return "modalLayer";
    }

    public ItsNatModalLayer createItsNatModalLayerHTML(Element element,NameValue[] artifacts,boolean execCreateFilters,ItsNatHTMLDocComponentManagerImpl compMgr)
    {
        ItsNatModalLayer comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatModalLayerHTMLImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

    public ItsNatModalLayer createItsNatModalLayerHTML(Element element,boolean clean, int zIndex, float opacity, String background, NameValue[] artifacts,boolean execCreateFilters,ItsNatHTMLDocComponentManagerImpl compMgr)
    {
        ItsNatModalLayer comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processBeforeCreateItsNatComponentListener(element,getCompType(),getParams(clean,zIndex,opacity,background),artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatModalLayerHTMLImpl(element,clean,zIndex,opacity,background,artifacts,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

    public ItsNatModalLayer createItsNatModalLayerSVG(Element element,NameValue[] artifacts,boolean execCreateFilters,ItsNatSVGDocComponentManagerImpl compMgr)
    {
        ItsNatModalLayer comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatModalLayerSVGImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

    public ItsNatModalLayer createItsNatModalLayerSVG(Element element,boolean clean,int zIndex, float opacity, String background, NameValue[] artifacts,boolean execCreateFilters,ItsNatSVGDocComponentManagerImpl compMgr)
    {
        ItsNatModalLayer comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processBeforeCreateItsNatComponentListener(element,getCompType(),getParams(clean,zIndex,opacity,background),artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatModalLayerSVGImpl(element,clean,zIndex,opacity,background,artifacts,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

    public ItsNatModalLayer createItsNatModalLayerXUL(Element element,NameValue[] artifacts,boolean execCreateFilters,ItsNatXULDocComponentManagerImpl compMgr)
    {
        ItsNatModalLayer comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processBeforeCreateItsNatComponentListener(element,getCompType(),null,artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatModalLayerXULImpl(element,artifacts,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }

    public ItsNatModalLayer createItsNatModalLayerXUL(Element element,boolean clean,int zIndex, float opacity, String background, NameValue[] artifacts,boolean execCreateFilters,ItsNatXULDocComponentManagerImpl compMgr)
    {
        ItsNatModalLayer comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener(execCreateFilters,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processBeforeCreateItsNatComponentListener(element,getCompType(),getParams(clean,zIndex,opacity,background),artifacts,compMgr);
        if (comp == null)
            comp = new ItsNatModalLayerXULImpl(element,clean,zIndex,opacity,background,artifacts,compMgr);
        if (doFilters) comp = (ItsNatModalLayer)processAfterCreateItsNatComponentListener(comp,compMgr);
        registerItsNatComponent(execCreateFilters,comp,compMgr);
        return comp;
    }


    private NameValue[] getParams(boolean clean,int zIndex, float opacity, String background)
    {
        return new NameValue[]{
            new NameValue("cleanBelow",Boolean.valueOf(clean)),
            new NameValue("zIndex",new Integer(zIndex)),
            new NameValue("opacity",new Float(opacity)),
            new NameValue("background",background)
        };
    }
}
